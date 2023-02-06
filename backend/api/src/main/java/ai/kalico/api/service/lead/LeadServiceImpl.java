package ai.kalico.api.service.lead;

import ai.kalico.api.RootConfiguration;
import ai.kalico.api.props.YouTubeProps;
import ai.kalico.api.props.ZenRowsProps;
import ai.kalico.api.service.utils.ScraperUtils;
import ai.kalico.api.service.youtubej.YoutubeDownloader;
import ai.kalico.api.service.youtubej.downloader.request.RequestSearchContinuation;
import ai.kalico.api.service.youtubej.downloader.request.RequestSearchResult;
import ai.kalico.api.service.youtubej.downloader.response.Response;
import ai.kalico.api.service.youtubej.model.search.SearchResult;
import ai.kalico.api.service.youtubej.model.search.SearchResultItem;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kalico.model.ChannelPageableResponse;
import com.kalico.model.ChannelRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

/**
 * @author Biz Melesse created on 2/5/23
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LeadServiceImpl implements LeadService {

  private final YoutubeDownloader youtubeDownloader = new YoutubeDownloader();
  private final ScraperUtils scraperUtils;
  private final ObjectMapper objectMapper;
  private final ZenRowsProps zenRowsProps;
  private final YouTubeProps youTubeProps;

  @Override
  public ChannelPageableResponse getChannelInfo(ChannelRequest channelRequest) {
    Response<SearchResult> response = youtubeDownloader.search(
        new RequestSearchResult(
        channelRequest.getQuery()));
    if (response.ok()) {
      SearchResult result = response.data();
      Set<String> channels = new HashSet<>(getChannels(result));
      while (result.hasContinuation() && channels.size() < youTubeProps.getMaxChannelResults()) {
        result = youtubeDownloader.searchContinuation(new RequestSearchContinuation(result)).data();
        channels.addAll(getChannels(result));
      }

      List<Map<String, String>>  channelDetails = getChannelDetails(new ArrayList<>(channels),
          zenRowsProps.getConcurrency());
      log.info(this.getClass().getName() + ".getChannelInfo: Fetched {} channel details for query: '{}' ",
          channelDetails.size(), channelRequest.getQuery());
      return new ChannelPageableResponse()
          .count(channelDetails.size())
          .records(channelDetails);
    }
    log.info(this.getClass().getName() + ".getChannelInfo: Fetched {} channel details for query: '{}' ",
        0, channelRequest.getQuery());
    return new ChannelPageableResponse()
        .count(0)
        .records(new ArrayList<>());
  }

  private List<Map<String, String>>  getChannelDetails(List<String> channels, int concurrency) {
    log.info(this.getClass().getName() + ".getChannelDetails: Fetching channel details for {} channels",
        channels.size());
    List<Map<String, String>> response = new ArrayList<>();
    if (!ObjectUtils.isEmpty(channels)) {
      List<List<String>> batches = getConcurrencyBatches(channels, concurrency);
      for (List<String> batch : batches) {
        List<CompletableFuture<Map<String, String>>> tasks = new ArrayList<>();
        for (final String channel : batch) {
          tasks.add(
              CompletableFuture.supplyAsync(() -> getChannelDetail(channel),
                  RootConfiguration.executor)
          );
        }
        List<Map<String, String>> batchedResult = tasks
            .stream()
            .map(CompletableFuture::join)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
        response.addAll(batchedResult);
      }
    }
    return response
        .stream()
        .filter(it -> it.size() > 0)
        .collect(Collectors.toList());
  }

  private List<List<String>> getConcurrencyBatches(List<String> channels, int concurrency) {
    // Batch up the task count up to the concurrency limit
    List<List<String>> batches = new ArrayList<>();
    List<String> batch = new ArrayList<>();
    for (String channel : channels) {
      if (batch.size() == concurrency) {
        batches.add(batch);
        batch = new ArrayList<>();
      }
      batch.add(channel);
    }
    batches.add(batch);
    return batches;
  }

  private Map<String, String> getChannelDetail(String channel) {
    String url = String.format("https://www.youtube.com/%s/about", channel);
    URI uri = scraperUtils.getZenRowsUri(url, false, false, false);
    final CloseableHttpClient httpClient = HttpClients.createDefault();
    if (uri != null) {
      HttpGet httpGet = new HttpGet(uri);
      CloseableHttpResponse httpResponse = null;
      try {
        httpResponse = httpClient.execute(httpGet);
        Header[] requestId = httpResponse.getHeaders("X-Request-Id");
        log.info("X-Request-Id: {} \turl: {}", requestId[0].getElements()[0].getName(), url);
        return parseChannelSoup(EntityUtils.toString(httpResponse.getEntity()));
      } catch (IOException e) {
        log.error(this.getClass().getName() + ".getChannelDetails: {}", e.getLocalizedMessage());
      }
    }
    return null;
  }

  private Map<String, String> parseChannelSoup(String soup) {
    Document doc = Jsoup.parse(soup);
    Map<String, String> data = new HashMap<>();
    try {
      String json = null;
      List<DataNode> dataNodes = doc.select("script").dataNodes();
      for (DataNode dataNode : dataNodes) {
        String wholeData = dataNode.getWholeData();
        if (wholeData.startsWith("var ytInitialData")) {
          json = wholeData
              .replace("var ytInitialData = ", "")
              .replace("\n", "")
              .strip();
          break;
        }
      }
      if (json != null) {
        JsonNode jsonNode = objectMapper.readValue(json, JsonNode.class);
        if (jsonNode.get("metadata") != null) {
          if (jsonNode.get("metadata").get("channelMetadataRenderer") != null) {
            JsonNode channelMeta = jsonNode.get("metadata").get("channelMetadataRenderer");
            String channelName = "", description = "", keywords = "", channelUrl = "", subscriberCount = "";
            if (channelMeta.get("title") != null) {
              channelName = channelMeta.get("title").asText();
            }
            if (channelMeta.get("description") != null) {
              description = channelMeta.get("description").asText();
            }
            if (channelMeta.get("keywords") != null) {
              keywords = channelMeta.get("keywords").asText();
            }
            if (channelMeta.get("vanityChannelUrl") != null) {
              channelUrl = channelMeta.get("vanityChannelUrl").asText();
            }
            JsonNode channelAboutMetadata = findNode(jsonNode.get("contents"),
                "channelAboutFullMetadataRenderer");
            if (ObjectUtils.isEmpty(channelUrl) && channelAboutMetadata.get("canonicalChannelUrl") != null) {
              channelUrl = channelAboutMetadata.get("canonicalChannelUrl").asText();
            }
            Map<String, String> links = new HashMap<>();
            if (channelAboutMetadata.get("primaryLinks") != null) {
              links = parseLinks(channelAboutMetadata.get("primaryLinks"));
            }
            JsonNode subscribeNode = findNode(jsonNode.get("header"), "subscriberCountText");
            if (subscribeNode != null) {
              subscriberCount = subscribeNode
                  .get("simpleText")
                  .asText()
                  .replace("subscribers", "")
                  .strip();
            }

            data.put("channelName", channelName);
            data.put("description", description);
            data.put("keywords", keywords);
            data.put("channelUrl", channelUrl);
            data.put("subscriberCount", subscriberCount);
            data.putAll(links);
          }
        }
      }
    } catch (JsonProcessingException | NullPointerException e) {
      log.error(this.getClass().getName() + ".parseChannelSoup: {}", e.getLocalizedMessage());
    }
    return data;
  }

  private Map<String, String>  parseLinks(JsonNode linkNode) {
    Map<String, String>  links = new HashMap<>();
    if (linkNode != null && linkNode.isArray()) {
      for (int i = 0; i < linkNode.size(); i++) {
        JsonNode link = linkNode.get(i);
        String name = null;
        if (link.get("title") != null) {
          if (link.get("title").get("simpleText") != null) {
            name = link.get("title").get("simpleText").asText();
          }
        }
        String url = null;
        if (link.get("navigationEndpoint") != null) {
          if (link.get("navigationEndpoint").get("urlEndpoint") != null) {
            if (link.get("navigationEndpoint").get("urlEndpoint").get("url") != null) {
              url = link.get("navigationEndpoint").get("urlEndpoint").get("url").asText();
            }
          }
        }
       if (url != null && name != null) {
         try {
           url = URLDecoder.decode(url, StandardCharsets.UTF_8.toString());
           // Remove the redirect tokens and extract the target url
           String[] tokens = url.split("=http");
           if (tokens.length > 1) {
             url = "http" + tokens[tokens.length - 1];
           }
           links.put(name, url);
         } catch (UnsupportedEncodingException e) {
           log.error(this.getClass().getName() + ".parseLinks: {}", e.getLocalizedMessage());
         }
       }
      }
    }
    return links;
  }

  private JsonNode findNode(JsonNode root, String target) {
    Iterator<String> fieldNames = root.fieldNames();
    JsonNode result = null;
    while (fieldNames.hasNext()) {
      String key = fieldNames.next();
      if (key.equals(target)) {
        return root.get(key);
      }
      JsonNode child = root.get(key);
      if (child.isArray()) {
        for (int i = 0; i < child.size(); i++) {
          result= findNode(child.get(i), target);
          if (result != null) {
            return result;
          }
        }
      }
      if (child.fieldNames().hasNext()) {
         result = findNode(child, target);
         if (result != null) {
           return result;
         }
      }
    }
    return result;
  }

  private List<String> getChannels(SearchResult result) {
    List<String> channels = new ArrayList<>();
    if (!ObjectUtils.isEmpty(result.items())) {
      for (SearchResultItem item : result.items()) {
        try {
          channels.add(item.asVideo().channelName());
        } catch (UnsupportedOperationException e) {
          log.error(this.getClass().getName() + ".getChannels: {}", e.getLocalizedMessage());
        }
      }
    }
    return channels;
  }
}
