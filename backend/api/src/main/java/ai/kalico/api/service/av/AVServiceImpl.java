package ai.kalico.api.service.av;

import ai.kalico.api.dto.VideoInfoDto;
import ai.kalico.api.service.download.DownloadService;
import ai.kalico.api.service.parser.InstagramParser;
import ai.kalico.api.service.utils.AVAsyncHelper;
import ai.kalico.api.service.youtubej.YoutubeDownloader;
import ai.kalico.api.service.youtubej.downloader.request.RequestVideoFileDownload;
import ai.kalico.api.service.youtubej.downloader.request.RequestVideoInfo;
import ai.kalico.api.service.youtubej.downloader.response.Response;
import ai.kalico.api.service.youtubej.model.videos.VideoInfo;
import ai.kalico.api.service.youtubej.model.videos.formats.Format;
import ai.kalico.api.service.scraper.ScraperService;
import ai.kalico.api.service.utils.Platform;
import com.kalico.model.GenericResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @author Bizuwork Melesse
 * created on 6/12/22
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AVServiceImpl implements AVService {
  private final YoutubeDownloader youtubeDownloader = new YoutubeDownloader();
  private final DownloadService downloadService;

  private final AVAsyncHelper asyncHelper;
  private final InstagramParser instagramParser;


  // Refer to https://gist.github.com/sidneys/7095afe4da4ae58694d128b1034e01e2 for YouTube
  // video stream format codes
  private final List<Integer> youTubeITags = List.of(
    37, //1080p
    22, //720p
    18 //360p
  );

  @Async
  @Override
  public void startVideoProcessing(String url, Function<VideoInfoDto, GenericResponse> callback) {
    log.info("Starting video processing for url {}", url);
    VideoInfoDto dto = getContent(url);
    if (callback != null) {
      callback.apply(dto);
    }
    if (dto != null) {
      try {
        if (dto.getPlatform() == Platform.YOUTUBE) {
          youtubeDownloader
              .getConfig()
              .getExecutorService()
              .submit(() -> processYouTubeVideo(dto))
              .get();
        } else if (dto.getPlatform() == Platform.INSTAGRAM) {
          youtubeDownloader
              .getConfig()
              .getExecutorService()
              .submit(() -> processInstagramVideo(dto))
              .get();
        }
      } catch (InterruptedException | ExecutionException e) {
        log.error("ContentServiceImpl.startVideoProcessing: {}", e.getLocalizedMessage());
        e.printStackTrace();
      }
    }
  }

  /**
   * Get the media content at the given URL.
   * Use WebClient to simulate a full browser and execute
   * any JavaScript code. Use JSoup for all other downstream
   * tasks such as the parsing of the HTML content and the
   * downloading of the media content from the source.
   *
   * @param url
   * @return
   */
  private VideoInfoDto getContent(String url) {
      Platform platform = getPlatform(url);
      log.info("About to process URL for {}: {}", platform.name(), url);
      VideoInfoDto videoInfoDto  = null;
      if (platform == Platform.YOUTUBE) {
           videoInfoDto = getYouTubeVideoInfo(url);
           if (videoInfoDto != null) {
             videoInfoDto.setPlatform(Platform.YOUTUBE);
             videoInfoDto.setUrl(url);
           }
        } else if (platform == Platform.INSTAGRAM) {
        videoInfoDto = instagramParser.getMediaMetadata(url, generateUid());
      }
      if (videoInfoDto != null) {
        videoInfoDto.setPermalink(url);
      }
      return videoInfoDto;
  }

  @Override
  public void processYouTubeVideo(VideoInfoDto videoInfoDto) {
      String videoId = videoInfoDto.getVideoInfo().details().videoId();
      RequestVideoFileDownload request = new RequestVideoFileDownload(videoInfoDto.getFormat())
          .saveTo(new File(asyncHelper.getParentPath(videoId)))
          .renameTo(videoId)
          .overwriteIfExists(true);
      String path = asyncHelper.getVideoPath(videoId);
      if (!Files.exists(Path.of(path))) {
        youtubeDownloader.downloadVideoFile(request);
      }
    submitAsyncTasks(path, videoId);
  }

  @Override
  public void processInstagramVideo(VideoInfoDto videoInfoDto) {
    String videoId = videoInfoDto.getVideoIdOverride();
    String path = asyncHelper.getVideoPath(videoId);
    if (!Files.exists(Path.of(path))) {
      downloadService.instagramReelDownload(
          videoInfoDto.getVideoIdOverride(),
          videoInfoDto.getPermalink(),
          videoInfoDto.getUrl(),
          asyncHelper.getVideoPath(videoId));
    }
    submitAsyncTasks(path, videoId);
  }

  private void submitAsyncTasks(String path, String videoId) {
    // Generate audio file for transcoding and perform audio to text
    asyncHelper.processAudio(videoId);

    // Generate images from frames and perform image to text
    asyncHelper.processImages(videoId);

    // Generate HLS files and upload them to S3
    asyncHelper.processHls(videoId, path);
  }

  private VideoInfoDto getYouTubeVideoInfo(String url) {
    YoutubeDownloader downloader = new YoutubeDownloader();
    String videoId = extractYouTubeVideoId(url);
    Response<VideoInfo> response = downloader.getVideoInfo(new RequestVideoInfo(videoId));
    if (response.ok()) {
      VideoInfo video = response.data();
      // Loop through the itags and get the first highest quality version found
      Format format = null;
      for (Integer itag : youTubeITags) {
        format = video.findFormatByItag(itag);
        if (format != null) {
          break;
        }
      }
      VideoInfoDto dto = new VideoInfoDto();
      dto.setVideoInfo(video);
      dto.setFormat(format);
      return dto;
    }
    return null;
  }

  @Override
  public String extractYouTubeVideoId(String url) {
    String vId = null;
    String regex = "http(?:s)?://(?:www\\.)?youtu(?:\\.be/|be\\.com/(?:watch\\?v=|v/|embed/|user/(?:[\\w#]+/)+))([^&#?\\n]+)";
    Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
    Matcher matcher = pattern.matcher(url);
    if (matcher.matches()){
      vId = matcher.group(1);
    }
    return vId;
  }

  private Platform getPlatform(String url) {
    URL parsedUrl = null;
    try {
      parsedUrl = new URL(url);
    } catch (MalformedURLException e) {
      e.printStackTrace();
    }
    if (parsedUrl != null) {
      if (parsedUrl.getHost().toLowerCase().contains("youtube")) {
        return Platform.YOUTUBE;
      }
      else if (parsedUrl.getHost().toLowerCase().contains("instagram")) {
        return Platform.INSTAGRAM;
      }
    }
    return Platform.INSTAGRAM;
  }

  public String generateUid() {
    StringBuilder builder = new StringBuilder();
    // An ID length of N gives 62^N unique IDs
    int contentIdLength = 7;
    for (int i = 0; i < contentIdLength; i++) {
      builder.append(getRandomCharacter());
    }
    return builder.toString();
  }

  public Character getRandomCharacter() {
    Random random = new Random();
    String uidAlphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnoqprstuvwxyz0123456789";
    int index = random.nextInt(uidAlphabet.length());
    return uidAlphabet.charAt(index);
  }
}
