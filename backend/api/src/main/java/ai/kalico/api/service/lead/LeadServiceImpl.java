package ai.kalico.api.service.lead;

import ai.kalico.api.service.youtubej.YoutubeDownloader;
import com.kalico.model.ChannelPageableResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Biz Melesse created on 2/5/23
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LeadServiceImpl implements LeadService {

  private final YoutubeDownloader youtubeDownloader = new YoutubeDownloader();

  @Override
  public ChannelPageableResponse getChannelInfo(String query, Integer page, Integer limit) {
    return new ChannelPageableResponse();
  }
}
