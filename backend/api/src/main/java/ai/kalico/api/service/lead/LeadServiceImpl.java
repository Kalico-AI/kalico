package ai.kalico.api.service.lead;

import static com.amazonaws.util.StringUtils.UTF8;

import ai.kalico.api.RootConfiguration;
import ai.kalico.api.data.postgres.entity.LeadsEntity;
import ai.kalico.api.data.postgres.repo.LeadsRepo;
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
import com.kalico.model.YouTubeChannelDetail;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
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
  private final LeadsRepo leadsRepo;
  @Override
  public ChannelPageableResponse getChannelInfo(String query) {
    Response<SearchResult> response = youtubeDownloader.search(
        new RequestSearchResult(query));
    if (response.ok()) {
      SearchResult result = response.data();
      Set<String> channels = new HashSet<>(getChannels(result));
      while (result.hasContinuation() && channels.size() < youTubeProps.getMaxChannelResults()) {
        result = youtubeDownloader.searchContinuation(new RequestSearchContinuation(result)).data();
        channels.addAll(getChannels(result));
      }

      List<YouTubeChannelDetail>  channelDetails = submitBatchedRequests(new ArrayList<>(channels), 
          this::getChannelDetail);
      channelDetails = fetchEmailAddress(channelDetails, query);
      channelDetails.sort(Comparator.comparing(YouTubeChannelDetail::getSubscribersValue));

      ChannelPageableResponse fullResponse = new ChannelPageableResponse()
          .count(channelDetails.size())
          .records(channelDetails);
      saveResponse(fullResponse);
      log.info(this.getClass().getSimpleName()+ ".getChannelInfo: Fetched {} channel details for query: '{}' ",
          channelDetails.size(), query);
      return fullResponse;
    }
    log.info(this.getClass().getSimpleName()+ ".getChannelInfo: Fetched {} channel details for query: '{}' ",
        0, query);
    return new ChannelPageableResponse()
        .count(0)
        .records(new ArrayList<>());
  }

  @SneakyThrows
  private void saveResponse(ChannelPageableResponse fullResponse) {
    // Save the response locally
    String outputStr = objectMapper.writeValueAsString(fullResponse);
    try (InputStream in = new ByteArrayInputStream(outputStr.getBytes(UTF8))) {
      // Save the file to disk for processing
      String path = "/tmp/youtube-channels-request-" + LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) + ".json";
      if (!new File(path).exists()) {
        new File(path).mkdirs();
      }
      File output = new File(path);
      Files.copy(in, output.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }
  }

  private List<YouTubeChannelDetail> fetchEmailAddress(List<YouTubeChannelDetail> channelDetails, String query) {
    // Fetch the email address from the channel's Facebook page, if available
    List<YouTubeChannelDetail> channelsWithoutFacebookPage = channelDetails
        .stream()
        .filter(it -> it.getFacebook() == null)
        .collect(Collectors.toList());
    List<YouTubeChannelDetail> channelsWithFacebookPage = channelDetails
        .stream()
        .filter(it -> it.getFacebook() != null)
        .collect(Collectors.toList());
    if (youTubeProps.isEmailRequired()) {
      log.info(this.getClass().getSimpleName()+ ".fetchEmailAddress: Fetching email addresses for {} channels",
          channelsWithFacebookPage.size());
      for (YouTubeChannelDetail detail : channelsWithFacebookPage) {
       detail.setQuery(query);
      }
      return submitBatchedRequests(channelsWithFacebookPage, this::getFacebookPage);
    }
    List<YouTubeChannelDetail> allChannels = new ArrayList<>();
    allChannels.addAll(channelsWithFacebookPage);
    allChannels.addAll(channelsWithoutFacebookPage);
    return allChannels;
  }

  private List<YouTubeChannelDetail>  submitBatchedRequests(List<?> itemsToBatch, 
      Function<Object, YouTubeChannelDetail> callback) {
    log.info(this.getClass().getSimpleName()+ ".submitBatchedRequests: Making batched requests for {} items",
        itemsToBatch.size());
    List<YouTubeChannelDetail> response = new ArrayList<>();
    if (!ObjectUtils.isEmpty(itemsToBatch)) {
      List<List<?>> batches = batchObjects(itemsToBatch);
      for (List<?> batch : batches) {
        List<CompletableFuture<YouTubeChannelDetail>> tasks = new ArrayList<>();
        for (final Object o : batch) {
          tasks.add(
              CompletableFuture.supplyAsync(() -> callback.apply(o),
                  RootConfiguration.executor));
        }
        List<YouTubeChannelDetail> batchedResult = tasks
            .stream()
            .map(CompletableFuture::join)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
        response.addAll(batchedResult);
      }
    }
    return response;
  }

  private List<List<?>> batchObjects(List<?> objects) {
    // Batch up the task count up to the concurrency limit
    List<List<?>> batches = new ArrayList<>();
    List<Object> batch = new ArrayList<>();
    int concurrency = zenRowsProps.getConcurrency();
    for (Object o : objects) {
      if (batch.size() == concurrency) {
        batches.add(batch);
        batch = new ArrayList<>();
      }
      batch.add(o);
    }
    batches.add(batch);
    return batches;
  }

  private YouTubeChannelDetail getChannelDetail(Object channel) {
    String _channel = (String) channel;
    String url = String.format("https://www.youtube.com/%s/about", _channel);
    URI uri = scraperUtils.getZenRowsUri(url, false, false, false);
    final CloseableHttpClient httpClient = HttpClients.createDefault();
    if (uri != null) {
      HttpGet httpGet = new HttpGet(uri);
      CloseableHttpResponse httpResponse = null;
      try {
        httpResponse = httpClient.execute(httpGet);
        Header[] requestId = httpResponse.getHeaders("X-Request-Id");
        log.info("X-Request-Id: {} \turl: {}", requestId[0].getElements()[0].getName(), url);
        YouTubeChannelDetail detail = parseChannelSoup(EntityUtils.toString(httpResponse.getEntity()));
        if (detail != null) {
          detail.setChannelHandle(_channel);
        }
        return detail;
      } catch (IOException e) {
        log.error(this.getClass().getSimpleName()+ ".submitBatchedRequests: {}", e.getLocalizedMessage());
      }
    }
    return null;
  }

  private YouTubeChannelDetail getFacebookPage(Object detail) {
    YouTubeChannelDetail _detail = (YouTubeChannelDetail) detail;
    String url = _detail.getFacebook();
    if (url != null && leadsRepo.findByFacebook(url).isEmpty()) {
      URI uri = scraperUtils.getZenRowsUri(url, true, true, false);
      final CloseableHttpClient httpClient = HttpClients.createDefault();
      if (uri != null) {
        HttpGet httpGet = new HttpGet(uri);
        CloseableHttpResponse httpResponse = null;
        try {
          httpResponse = httpClient.execute(httpGet);
          Header[] requestId = httpResponse.getHeaders("X-Request-Id");
          log.info("X-Request-Id: {} \turl: {}", requestId[0].getElements()[0].getName(), url);
          String email = parseFacebookSoup(EntityUtils.toString(httpResponse.getEntity()));
          if (email == null) {
            return  null;
          }
          _detail.setEmail(email);
          try {
            // Save to the database
            LeadsEntity entity = mapDetailToEntity(_detail);
            leadsRepo.save(entity);
          } catch (Exception e) {
            log.warn(this.getClass().getSimpleName()+ ".getFacebookPage: {}", e.getLocalizedMessage());
          }
          return _detail;
        } catch (IOException e) {
          log.error(this.getClass().getSimpleName() + ".submitBatchedRequests: {}",
              e.getLocalizedMessage());
        }
      }
    }
    return null;
  }


  private YouTubeChannelDetail parseChannelSoup(String soup) {
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
            String channelName = "", description = "", keywords = "", channelUrl = "", subscribers = "";
            Long subscribersValue = -1L;
            if (channelMeta.get("title") != null) {
              channelName = channelMeta.get("title").asText();
            }
            if (channelMeta.get("description") != null) {
              description = channelMeta.get("description").asText();
              description = description.replace("\n", " ").strip();
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
              subscribers = subscribeNode
                  .get("simpleText")
                  .asText()
                  .replace("subscribers", "")
                  .strip();
              subscribersValue = getSubscriberNumericalValue(subscribers);
            }
            return new YouTubeChannelDetail()
                .channelName(channelName)
                .description(description)
                .keywords(keywords)
                .channelUrl(channelUrl)
                .subscribers(subscribers)
                .subscribersValue(subscribersValue)
                .twitter(links.get(SocialPlatforms.TWITTER))
                .website(links.get(SocialPlatforms.WEBSITE))
                .blog(links.get(SocialPlatforms.BLOG))
                .pinterest(links.get(SocialPlatforms.PINTEREST))
                .snapChat(links.get(SocialPlatforms.SNAPCHAT))
                .discord(links.get(SocialPlatforms.DISCORD))
                .tiktok(links.get(SocialPlatforms.TIKTOK))
                .facebook(links.get(SocialPlatforms.FACEBOOK))
                .instagram(links.get(SocialPlatforms.INSTAGRAM));
          }
        }
      }
    } catch (JsonProcessingException | NullPointerException e) {
      log.error(this.getClass().getSimpleName()+ ".parseChannelSoup: {}", e.getLocalizedMessage());
    }
    return null;
  }

  private String parseFacebookSoup(String soup) {
    soup = soup.replace("\\u0040", "@");
    // Limit the regex search space by taking 256 characters to the left and right of the @ symbol
    int maxChars = 256;
    int soupLength = soup.length();
    StringJoiner joiner = new StringJoiner("");
    int index = soup.indexOf('@');
    while (index > 0) {
      int start = index - maxChars;
      int end = index + maxChars;
      if (start < 0) {
        start = 0;
      }
      if (end >= soup.length()) {
        end = soupLength;
      }
      String substring = soup.substring(start, end);
      joiner.add(substring);
      index = soup.indexOf('@', index + 1);
    }
    String minifiedSoup = joiner.toString();
    String pattern = "[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})";
    Pattern emailPattern = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
    Matcher matcher = emailPattern.matcher(minifiedSoup);
    String email = null;
    // Find the first instance of an email address
    if (matcher.find()){
      email = matcher.group();
    }
    return email;
  }
  private Long getSubscriberNumericalValue(String subscribers) {
    long value = -1L;
    if (subscribers.toLowerCase().endsWith("m")) {
      value = Math.round(Double.parseDouble(subscribers.replace("M", "")) * 1000000);
    } else if (subscribers.toLowerCase().endsWith("k")) {
      value = Math.round(Double.parseDouble(subscribers.replace("K", "")) * 1000);
    }
    return value;
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
        name = getStandardFieldName(name);
       if (url != null && name != null)
       {
         try {
           url = URLDecoder.decode(url, StandardCharsets.UTF_8.toString());
           // Remove the redirect tokens and extract the target url
           String[] tokens = url.split("=");
           if (tokens.length > 1) {
             url = tokens[tokens.length - 1];
           }
           links.put(name, url);
         } catch (UnsupportedEncodingException e) {
           log.error(this.getClass().getSimpleName()+ ".parseLinks: {}", e.getLocalizedMessage());
         }
       }
      }
    }
    return links;
  }

  private String getStandardFieldName(String name) {
    // Standardize
    if (name != null) {
      if (name.toLowerCase().contains("facebook")) {
        name = SocialPlatforms.FACEBOOK;
      } else if (name.toLowerCase().contains("instagram")) {
        name = SocialPlatforms.INSTAGRAM;
      } else if (name.toLowerCase().contains("twitter")) {
        name = SocialPlatforms.TWITTER;
      } else if (name.toLowerCase().contains("website")) {
        name = SocialPlatforms.WEBSITE;
      } else if (name.toLowerCase().contains("blog")) {
        name = SocialPlatforms.BLOG;
      } else if (name.toLowerCase().contains("pinterest")) {
        name = SocialPlatforms.PINTEREST;
      } else if (name.toLowerCase().contains("snapchat")) {
        name = SocialPlatforms.SNAPCHAT;
      } else if (name.toLowerCase().contains("discord")) {
        name = SocialPlatforms.DISCORD;
      } else if (name.equalsIgnoreCase("fb")) {
        name = SocialPlatforms.FACEBOOK;
      } else if (name.equalsIgnoreCase("ig")) {
        name = SocialPlatforms.INSTAGRAM;
      } else if (name.toLowerCase().contains("tik tok")) {
        name = SocialPlatforms.TIKTOK;
      } else {
        name = null;
      }
    }
    return name;
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
    return null;
  }

  private List<String> getChannels(SearchResult result) {
    List<String> channels = new ArrayList<>();
    if (result != null && !ObjectUtils.isEmpty(result.items())) {
      for (SearchResultItem item : result.items()) {
        try {
          channels.add(item.asVideo().channelName());
        } catch (UnsupportedOperationException e) {
          log.warn(this.getClass().getSimpleName()+ ".getChannels: {} -- Ignore", e.getLocalizedMessage());
        }
      }
    }
    return channels;
  }

  private LeadsEntity mapDetailToEntity(YouTubeChannelDetail detail) {
    LeadsEntity entity = new LeadsEntity();
    entity.setEmail(detail.getEmail());
    entity.setDescription(detail.getDescription());
    entity.setKeywords(detail.getKeywords());
    entity.setChannelUrl(detail.getChannelUrl());
    entity.setChannelName(detail.getChannelName());
    entity.setChannelHandle(detail.getChannelHandle());
    entity.setSubscribers(detail.getSubscribers());
    entity.setSubscribersValue(detail.getSubscribersValue());
    entity.setFacebook(detail.getFacebook());
    entity.setInstagram(detail.getInstagram());
    entity.setTwitter(detail.getTwitter());
    entity.setWebsite(detail.getWebsite());
    entity.setBlog(detail.getBlog());
    entity.setSnapChat(detail.getSnapChat());
    entity.setDiscord(detail.getDiscord());
    entity.setTiktok(detail.getTiktok());
    entity.setPinterest(detail.getPinterest());
    entity.setQuery(detail.getQuery());
    return entity;
  }

  private static class SocialPlatforms {
    public static final String FACEBOOK = "Facebook";
    public static final String TWITTER = "Twitter";
    public static final String INSTAGRAM = "Instagram";
    public static final String WEBSITE = "Website";
    public static final String BLOG = "Blog";
    public static final String PINTEREST = "Pinterest";
    public static final String SNAPCHAT = "SnapChat";
    public static final String TIKTOK = "TikTok";
    public static final String DISCORD = "Discord";
  }
}
