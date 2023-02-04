package ai.kalico.api.service.av;

import ai.kalico.api.data.postgres.entity.MediaContentEntity;
import ai.kalico.api.data.postgres.entity.ProjectEntity;
import ai.kalico.api.data.postgres.entity.UserEntity;
import ai.kalico.api.data.postgres.repo.MediaContentRepo;
import ai.kalico.api.data.postgres.repo.ProjectRepo;
import ai.kalico.api.data.postgres.repo.UserRepo;
import ai.kalico.api.dto.VideoInfoDto;
import ai.kalico.api.props.ProjectProps;
import ai.kalico.api.service.download.DownloadService;
import ai.kalico.api.service.parser.InstagramParser;
import ai.kalico.api.service.utils.AVAsyncHelper;
import ai.kalico.api.service.utils.KALUtils;
import ai.kalico.api.service.youtubej.YoutubeDownloader;
import ai.kalico.api.service.youtubej.downloader.request.RequestVideoFileDownload;
import ai.kalico.api.service.youtubej.downloader.request.RequestVideoInfo;
import ai.kalico.api.service.youtubej.downloader.response.Response;
import ai.kalico.api.service.youtubej.model.videos.VideoInfo;
import ai.kalico.api.service.youtubej.model.videos.formats.Format;
import ai.kalico.api.service.utils.Platform;
import com.kalico.model.ContentPreviewResponse;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Base64;
import java.util.Optional;
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
import org.springframework.util.ObjectUtils;

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
  private final MediaContentRepo mediaContentRepo;
  private final ProjectProps projectProps;
  private final ProjectRepo projectRepo;
  private final UserRepo userRepo;


  // Refer to https://gist.github.com/sidneys/7095afe4da4ae58694d128b1034e01e2 for YouTube
  // video stream format codes
  private final List<Integer> youTubeITags = List.of(
    37, //1080p
    22, //720p
    18 //360p
  );

  @Async
  @Override
  public void processMedia(String url, Long projectId, String file, String fileExtension) {
    log.info("Starting video processing for url {}", url);
    if (url != null) {
      url = normalizeUrl(url);
      VideoInfoDto dto = getContent(url);
      createMediaContentFromUrl(dto, projectId);
      try {
        if (dto.getPlatform() == Platform.YOUTUBE) {
          youtubeDownloader
              .getConfig()
              .getExecutorService()
              .submit(() -> processYouTubeVideo(dto, projectId))
              .get();
        } else if (dto.getPlatform() == Platform.INSTAGRAM) {
          youtubeDownloader
              .getConfig()
              .getExecutorService()
              .submit(() -> processInstagramVideo(dto, projectId))
              .get();
        }
      } catch (InterruptedException | ExecutionException e) {
        log.error("ContentServiceImpl.startVideoProcessing: {}", e.getLocalizedMessage());
        e.printStackTrace();
      }
    } else if (file != null && fileExtension != null) {
      String mediaId = createMediaContentFromUpload(projectId);
      if (isVideo(fileExtension)) {
        processUploadedVideo(file, fileExtension, mediaId, projectId);
      } else if (isAudio(fileExtension)) {
        processUploadedAudio(file, fileExtension, mediaId, projectId);
      } else {
        log.error("File extension not supported for mediaId={}", mediaId);
      }
    }
  }

  @Override
  public String normalizeUrl(String url) {
    if (url.toLowerCase().contains("youtube")) {
      // Turn youtube mobile url into regular url
      return url.replace("m.youtube", "www.youtube");
    }
    return url;
  }

  private boolean isAudio(String fileExtension) {
    return projectProps.getSupportedAudioFormats().contains(fileExtension.toLowerCase());
  }

  private boolean isVideo(String fileExtension) {
    return projectProps.getSupportedVideoFormats().contains(fileExtension.toLowerCase());
  }

  private String createMediaContentFromUpload(Long projectId) {
    MediaContentEntity mediaContent = new MediaContentEntity();
    mediaContent.setMediaId(KALUtils.generateUid());
    mediaContent.setProjectId(projectId);
    mediaContentRepo.save(mediaContent);
    return mediaContent.getMediaId();
  }

  private void createMediaContentFromUrl(VideoInfoDto dto, Long projectId) {
    MediaContentEntity mediaContent = new MediaContentEntity();
    mediaContent.setPermalink(dto.getPermalink());

    if (dto.getVideoInfo() != null && dto.getVideoInfo().details() != null) {
      mediaContent.setMediaId(dto.getVideoInfo().details().videoId());
      if (dto.getVideoInfo().details().title() != null) {
        mediaContent.setScrapedTitle(dto.getVideoInfo().details().title());
      }
      if (dto.getVideoInfo().details().description() != null) {
        mediaContent.setScrapedDescription(dto.getVideoInfo().details().description());
      }
    } else {
      if (!ObjectUtils.isEmpty(dto.getMediaIdOverride())) {
        mediaContent.setMediaId(dto.getMediaIdOverride());
        mediaContent.setScrapedDescription(dto.getCaption());
      }
    }
    mediaContent.setProjectId(projectId);
    mediaContentRepo.save(mediaContent);
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
      log.info("Fetching content metadata for {}", url);
      VideoInfoDto videoInfoDto  = null;
      if (platform == Platform.YOUTUBE) {
           videoInfoDto = getYouTubeVideoInfo(url);
           if (videoInfoDto != null) {
             videoInfoDto.setPlatform(Platform.YOUTUBE);
             videoInfoDto.setUrl(url);
           }
        } else if (platform == Platform.INSTAGRAM) {
        videoInfoDto = instagramParser.getMediaMetadata(url, KALUtils.generateUid());
      }
      if (videoInfoDto != null) {
        videoInfoDto.setPermalink(url);
      }
      return videoInfoDto;
  }

  @Override
  public void processYouTubeVideo(VideoInfoDto videoInfoDto, Long projectId) {
      String mediaId = videoInfoDto.getVideoInfo().details().videoId();
      RequestVideoFileDownload request = new RequestVideoFileDownload(videoInfoDto.getFormat())
          .saveTo(new File(asyncHelper.getParentPath(mediaId)))
          .renameTo(mediaId)
          .overwriteIfExists(true);
      String path = asyncHelper.getVideoPath(mediaId);
      if (!Files.exists(Path.of(path))) {
        youtubeDownloader.downloadVideoFile(request);
      }
    submitAsyncTasks(path, mediaId, true, projectId);
  }

  @Override
  public void processUploadedVideo(String file, String fileExtension, String mediaId, Long projectId) {
    String path = asyncHelper.getVideoPath(mediaId);
    if (!Files.exists(Path.of(path))) {
      saveFile(file, path);
    }
    submitAsyncTasks(path, mediaId, true, projectId);
  }

  @Override
  public void processUploadedAudio(String file, String fileExtension, String mediaId, Long projectId) {
    String path = asyncHelper.getAudioPath(mediaId);
    if (!Files.exists(Path.of(path))) {
      saveFile(file, path);
    }
    submitAsyncTasks(path, mediaId, false, projectId);
  }

  @Override
  public ContentPreviewResponse downloadContentMetadata(String url) {
    VideoInfoDto dto = getContent(url);
    if (dto != null) {
      ContentPreviewResponse response = new ContentPreviewResponse();
      try {
        if (dto.getVideoInfo() != null &&
            dto.getVideoInfo().details() != null &&
            dto.getVideoInfo().details().title() != null) {
          response.setTitle(dto.getVideoInfo().details().title());
        } else if (dto.getCaption() != null) {
          response.setTitle(dto.getCaption());
        }
      } catch (NullPointerException e) {
        // pass
      }

      try {
        if (dto.getVideoInfo() != null &&
            dto.getVideoInfo().details() != null &&
            dto.getVideoInfo().details().thumbnails() != null &&
            dto.getVideoInfo().details().thumbnails().get(0) != null) {
          List<String> thumbnails =  dto.getVideoInfo().details().thumbnails();
          int index = thumbnails.size() - 1;
          String thumbnail = thumbnails.get(index);
          for (String _thumbnail : thumbnails) {
            if (_thumbnail.contains("hqdefault") || _thumbnail.contains("hddefault")) {
              thumbnail = _thumbnail;
              break;
            }
          }
          response.setThumbnail(thumbnail); // The biggest thumbnail
        }
//        else {
//           Instagram thumbnails require same origin
//          if (dto.getImageUrl() != null)  {
//            response.setThumbnail(dto.getImageUrl());
//          }
//        }

      } catch (NullPointerException e) {
        // pass
      }

      try {
        if (dto.getVideoInfo() != null &&
            dto.getVideoInfo().formats() != null &&
            dto.getVideoInfo().formats().get(0).duration() != null) {
          long duration = dto.getVideoInfo().formats().get(0).duration();
          double minutes = duration/(1000 * 60.0);
          long seconds = Math.round((minutes % Math.floor(minutes) * 60));
          String durationStr = String.format("Duration: %s minutes %s seconds", Math.round(minutes), seconds);
          response.setDuration(durationStr);
        }
      } catch (NullPointerException e) {
        // pass
      }
      return response;
    }

    return new ContentPreviewResponse();
  }

  private void saveFile(String file, String path) {
    String rawFile = file;
    String delimiter = "base64,";
    String[] tokens = file.split(delimiter);
    if (tokens.length > 1 && !ObjectUtils.isEmpty(tokens[1])) {
      rawFile = tokens[1];
    }
    try {
      var bs = Base64.getDecoder().decode(rawFile);
      InputStream in = new ByteArrayInputStream(bs);
      if (!new File(path).exists()) {
        new File(path).mkdirs();
      }
      File output = new File(path);
      Files.copy(in, output.toPath(), StandardCopyOption.REPLACE_EXISTING);
    } catch (Exception e) {
      log.error("Error in AVServiceImpl.saveFile: {}", e.getLocalizedMessage());
    }
  }

  @Override
  public void processInstagramVideo(VideoInfoDto videoInfoDto, Long projectId) {
    String mediaId = videoInfoDto.getMediaIdOverride();
    String path = asyncHelper.getVideoPath(mediaId);
    if (!Files.exists(Path.of(path))) {
      downloadService.instagramReelDownload(
          videoInfoDto.getMediaIdOverride(),
          videoInfoDto.getPermalink(),
          videoInfoDto.getUrl(),
          asyncHelper.getVideoPath(mediaId));
    }
    submitAsyncTasks(path, mediaId, true, projectId);
  }

  private void submitAsyncTasks(String path, String mediaId, boolean isVideo, Long projectId) {
    // Generate audio file for transcoding and perform audio to text
    asyncHelper.processAudio(mediaId, projectId);

    if (isVideo) {
      if (isPremiumUser(projectId)) {
        // Generate images from frames and perform image to text
        asyncHelper.processImages(mediaId, projectId);

        // Generate HLS files and upload them to S3
        asyncHelper.processHls(mediaId, path);
      } else {
        log.info("Skipping video processing for projectId={}. Not a premium user.", projectId);
      }
    }
  }

  private boolean isPremiumUser(Long projectId) {
    // TODO: Avoid making multiple queries
    Optional<ProjectEntity> projectEntity = projectRepo.findById(projectId);
    if (projectEntity.isPresent()) {
      UserEntity userEntity = userRepo.findByFirebaseId(projectEntity.get().getUserId());
      if (userEntity != null) {
        return userEntity.getIsPremiumUser();
      }
    }
    return false;
  }

  private VideoInfoDto getYouTubeVideoInfo(String url) {
    YoutubeDownloader downloader = new YoutubeDownloader();
    String mediaId = extractYouTubeVideoId(url);
    Response<VideoInfo> response = downloader.getVideoInfo(new RequestVideoInfo(mediaId));
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
      log.error(e.getLocalizedMessage());
    }
    if (parsedUrl != null) {
      if (parsedUrl.getHost().toLowerCase().contains("youtube")) {
        return Platform.YOUTUBE;
      }
      else if (parsedUrl.getHost().toLowerCase().contains("instagram")) {
        return Platform.INSTAGRAM;
      }
    }
    return Platform.INVALID;
  }
}
