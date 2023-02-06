package ai.kalico.api.service.lead;

import ai.kalico.api.RootConfiguration;
import ai.kalico.api.props.ZenRowsProps;
import ai.kalico.api.service.utils.ScraperUtils;
import ai.kalico.api.service.youtubej.YoutubeDownloader;
import ai.kalico.api.service.youtubej.downloader.request.RequestSearchResult;
import ai.kalico.api.service.youtubej.downloader.response.Response;
import ai.kalico.api.service.youtubej.model.search.ContinuatedSearchResult;
import ai.kalico.api.service.youtubej.model.search.SearchResult;
import ai.kalico.api.service.youtubej.model.search.SearchResultItem;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kalico.model.ChannelPageableResponse;
import com.kalico.model.ChannelRequest;
import com.kalico.model.SearchContinuationDto;
import java.io.IOException;
import java.net.URI;
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
import org.apache.http.HttpEntity;
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
  private final Set<String> excludedKeys = new HashSet<>(List.of(
      "rssUrl",
      "avatar",
      "channelUrl",
      "isFamilySafe",
      "androidDeepLink",
      "androidAppindexingLink",
      "iosAppindexingLink",
      "vanityChannelUrl",
      "subscribersAprox",
      "doubleclickTrackingUsername",
      "externalId",
      "channelConversionUrl"
  ));

  @Override
  public ChannelPageableResponse getChannelInfo(ChannelRequest channelRequest) {
    Response<SearchResult> response = youtubeDownloader.search(
        new RequestSearchResult(
        channelRequest.getQuery()));
    if (response.ok()) {
      SearchResult result = response.data();
      SearchContinuationDto continuation = getContinuation(result);
      List<Map<String, String>>  channelDetails = getChannelDetails(getChannels(result), zenRowsProps.getConcurrency());
      return new ChannelPageableResponse()
          .continuation(continuation)
          .count(channelDetails.size())
          .records(channelDetails);
    }
    return new ChannelPageableResponse()
        .count(0)
        .records(new ArrayList<>());
  }

  private List<Map<String, String>>  getChannelDetails(List<String> channels, int concurrency) {
    List<Map<String, String>> response = new ArrayList<>();
    if (!ObjectUtils.isEmpty(channels)) {
      List<CompletableFuture<Map<String, String>>> tasks = new ArrayList<>();
      for (final String channel : channels) {
        tasks.add(
            CompletableFuture.supplyAsync(() -> getChannelDetail(channel),
                RootConfiguration.executor)
        );
      }
      List<List<CompletableFuture<Map<String, String>>>> batches = getConcurrencyBatches(tasks, concurrency);
      for (List<CompletableFuture<Map<String, String>>> batch : batches) {
        response.addAll(batch
            .stream()
            .map(CompletableFuture::join)
            .filter(Objects::nonNull)
            .collect(Collectors.toList()));
      }
    }
    return response;
  }

  private List<List<CompletableFuture<Map<String, String>>>> getConcurrencyBatches(
      List<CompletableFuture<Map<String, String>>> tasks, int concurrency) {
    // Batch up the task count up to the concurrency limit
    List<List<CompletableFuture<Map<String, String>>>> batches = new ArrayList<>();
    List<CompletableFuture<Map<String, String>>> batch = new ArrayList<>();
    for (CompletableFuture<Map<String, String>> task : tasks) {
      if (batch.size() == concurrency) {
        batches.add(batch);
        batch = new ArrayList<>();
      }
      batch.add(task);
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
      HttpEntity httpEntity = null;
      try {
        httpEntity = httpClient.execute(httpGet).getEntity();
        Map<String, String> detail = parseChannelSoup(EntityUtils.toString(httpEntity));
//        for (Iterator<String> it = data.fieldNames(); it.hasNext(); ) {
//          String key = it.next();
//          if (!excludedKeys.contains(key)) {
//            if (key.equalsIgnoreCase("ownerUrls")) {
//              detail.put("channel", data.get(key).asText());
//            } else {
//              detail.put(key, data.get(key).asText());
//            }
//          }
//        }
        return detail;
      } catch (IOException e) {
        log.error(this.getClass().getCanonicalName() + ".getChannelDetails: {}", e.getLocalizedMessage());
      }
    }
    return null;
  }

  private Map<String, String> parseChannelSoup(String soup) {
    Document doc = Jsoup.parse(soup);
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
            String channelName = "", description = "", keywords = "", channelUrl = "";
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
            if (channelAboutMetadata.get("primaryLinks") != null) {
              List<String> links = parseLinks(channelAboutMetadata.get("primaryLinks"));
            }
            JsonNode subscribeNode = findNode(jsonNode.get("header"), "subscriberCountText");
            if (subscribeNode != null) {
              String subscriberCount = subscribeNode.get("simpleText").asText();
            }

            int x = 1;
          }
        }
        int x = 1;
      }
    } catch (JsonProcessingException | NullPointerException e) {

    }
    return null;
  }

  private List<String> parseLinks(JsonNode linkNode) {
    List<String> links = new ArrayList<>();
    if (linkNode != null && linkNode.isArray()) {
      for (int i = 0; i < linkNode.size(); i++) {
        JsonNode link = linkNode.get(i);
        String name = link.get("title").asText();
        String url = link.get("navigationEndpoint").get("urlEndpoint").get("url").asText();
        links.add(url);
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
          break;
        } catch (UnsupportedOperationException e) {
          // ignore
        }
      }
    }
    return channels;
  }

  private SearchContinuationDto getContinuation(SearchResult result) {
    if (result.hasContinuation()) {
      ContinuatedSearchResult continuatedSearchResult = (ContinuatedSearchResult) result;
      return new SearchContinuationDto()
          .token(continuatedSearchResult.continuation().token())
          .clientVersion(continuatedSearchResult.continuation().clientVersion())
          .clickTrackingParameters(continuatedSearchResult.continuation().clickTrackingParameters());
    }
   return null;
  }
}
