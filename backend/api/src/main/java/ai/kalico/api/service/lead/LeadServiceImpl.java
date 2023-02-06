package ai.kalico.api.service.lead;

import ai.kalico.api.RootConfiguration;
import ai.kalico.api.service.utils.ScraperUtils;
import ai.kalico.api.service.youtubej.YoutubeDownloader;
import ai.kalico.api.service.youtubej.downloader.request.RequestSearchResult;
import ai.kalico.api.service.youtubej.downloader.response.Response;
import ai.kalico.api.service.youtubej.model.search.ContinuatedSearchResult;
import ai.kalico.api.service.youtubej.model.search.SearchResult;
import ai.kalico.api.service.youtubej.model.search.SearchResultItem;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kalico.model.ChannelPageableResponse;
import com.kalico.model.ChannelRequest;
import com.kalico.model.SearchContinuationDto;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
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

  @Override
  public ChannelPageableResponse getChannelInfo(ChannelRequest channelRequest) {
    Response<SearchResult> response = youtubeDownloader.search(
        new RequestSearchResult(
        channelRequest.getQuery()));
    if (response.ok()) {
      SearchResult result = response.data();
      SearchContinuationDto continuation = getContinuation(result);
      List<Map<String, String>>  channelDetails = getChannelDetails(getChannels(result));
      return new ChannelPageableResponse()
          .continuation(continuation)
          .count(channelDetails.size())
          .records(channelDetails);
    }
    return new ChannelPageableResponse()
        .count(0)
        .records(new ArrayList<>());
  }

  private  List<Map<String, String>>  getChannelDetails(List<String> channels) {
    if (!ObjectUtils.isEmpty(channels)) {
      List<CompletableFuture<Map<String, String>>> tasks = new ArrayList<>();
      for (final String channel : channels) {
        tasks.add(
            CompletableFuture.supplyAsync(() -> getChannelDetail(channel),
                RootConfiguration.executor)
        );
      }
      return tasks
          .stream()
          .map(CompletableFuture::join)
          .filter(Objects::nonNull)
          .collect(Collectors.toList());
    }
    return new ArrayList<>();
  }

  private Map<String, String> getChannelDetail(String channel) {
    String url = String.format("https://www.youtube.com/%s/about", channel);
    URI uri = scraperUtils.getZenRowsUri(url, false, false);
    final CloseableHttpClient httpClient = HttpClients.createDefault();
    if (uri != null) {
      HttpGet httpGet = new HttpGet(uri);
      HttpEntity httpEntity = null;
      try {
        httpEntity = httpClient.execute(httpGet).getEntity();
        JsonNode data = objectMapper.readValue(httpEntity.getContent(), JsonNode.class);
        Map<String, String> detail = new HashMap<>();
        int x = 1;
//        YouTubeChannelDetail detail = new YouTubeChannelDetail();
      } catch (IOException e) {
        log.error(this.getClass().getCanonicalName() + ".getChannelDetails: {}", e.getLocalizedMessage());
      }
    }
    return null;
  }

  private List<String> getChannels(SearchResult result) {
    List<String> channels = new ArrayList<>();
    if (!ObjectUtils.isEmpty(result.items())) {
      for (SearchResultItem item : result.items()) {
        try {
          channels.add(item.asVideo().channelName());
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
