package ai.kalico.api.service.lead;

import ai.kalico.api.service.youtubej.YoutubeDownloader;
import ai.kalico.api.service.youtubej.downloader.request.RequestSearchResult;
import ai.kalico.api.service.youtubej.downloader.response.Response;
import ai.kalico.api.service.youtubej.model.search.ContinuatedSearchResult;
import ai.kalico.api.service.youtubej.model.search.SearchResult;
import com.kalico.model.ChannelPageableResponse;
import com.kalico.model.ChannelRequest;
import com.kalico.model.SearchContinuationDto;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

  @Override
  public ChannelPageableResponse getChannelInfo(ChannelRequest channelRequest) {
    Response<SearchResult> response = youtubeDownloader.search(
        new RequestSearchResult(
        channelRequest.getQuery()));
    if (response.ok()) {
      SearchResult result = response.data();
      SearchContinuationDto continuation = getContinuation(result);
      List<String> channels = getChannels(result);

    }
    return new ChannelPageableResponse()
        .totalRecords(0)
        .numPages(0)
        .records(new ArrayList<>());
  }

  private List<String> getChannels(SearchResult result) {
    if (!ObjectUtils.isEmpty(result.items())) {
      return result.items()
          .stream()
          .map(it -> it.asVideo().channelName())
          .collect(Collectors.toList());
    }
    return new ArrayList<>();
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
