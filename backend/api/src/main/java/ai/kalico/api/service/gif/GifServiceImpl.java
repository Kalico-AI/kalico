package com.kalico.api.service.gif;

import com.kalico.api.dto.Pair;
import com.kalico.api.props.AWSProps;
import com.kalico.api.props.DockerImageProps;
import com.kalico.api.service.aws.S3Service;
import com.kalico.api.service.mapper.BlogPostMapper;
import com.kalico.api.service.utils.AVAsyncHelper;
import com.kalico.api.service.utils.FWUtils;
import com.kalico.api.service.utils.ShellService;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

/**
 * @author Biz Melesse created on 1/12/23
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GifServiceImpl implements GifService {
  private final AVAsyncHelper asyncHelper;
  private final AWSProps awsProps;
  private final DockerImageProps dockerImageProps;
  private final S3Service s3Service;
  private final ShellService shell;
  private final BlogPostMapper blogPostMapper;

  @Override
  public String generateGif(String videoId, float start, float end) {
    // TODO: cleanup the local cache once done processing. We don't know when the user will
    //    be done so need to have a background task that runs every 24 hours or so
    String videoPath = asyncHelper.getVideoPath(videoId);
    String gifName = "";
    float duration = end - start;
    new File(FWUtils.getCanonicalPath(new File(videoPath).getParent())).mkdir();
    if (Files.exists(Path.of(videoPath))) {
      gifName = doGenerateGif(videoPath, videoId, String.valueOf(start), String.valueOf(duration));

    } else {
      log.info("Video with id={} does not exist in local cache. Attempting to download from "
          + "CDN", videoId);
      String url = blogPostMapper.getCdnUrl(awsProps.getCdn() + "/" + awsProps.getStreamFolder(),
          videoId, awsProps.getVideoFormat());
      downloadVideo(url, videoPath);
      gifName = doGenerateGif(videoPath, videoId, String.valueOf(start), String.valueOf(duration));
    }
    if (!ObjectUtils.isEmpty(gifName)) {
      String key = awsProps.getImageFolder() + "/" + videoId + "/" + gifName;
      String path = asyncHelper.getGifPath(videoId, gifName);
      s3Service.uploadImagesSync(awsProps.getBucket(), List.of(new Pair<>(path, key)), S3Service.GIF_TYPE);
      return String.format("%s/%s/%s/%s",
          awsProps.getCdn(), awsProps.getImageFolder(), videoId, gifName);
    }
    return "";
  }

  private String doGenerateGif(String videoPath, String videoId, String start, String duration) {
    log.info("Generating GIF for {}", videoPath);
    String gifName = asyncHelper.getGifName(videoId, start, duration);
    String gifPath = FWUtils.getCanonicalPath(asyncHelper.getGifPath(videoId, gifName));
    String workingDir = FWUtils.getCanonicalPath(new File(videoPath).getParent());
    String[] command = {
        "docker",
        "run",
        "-v",
        workingDir + ":" + workingDir,
        "-w",
        workingDir,
        dockerImageProps.getFfmpeg(),
        "-ss",
        start,
        "-t",
        duration,
        "-y",
        "-i",
        FWUtils.getCanonicalPath(videoPath),
        "-filter_complex",
        "[0:v] fps=10,scale=320:-1,split [a][b];[a] palettegen=max_colors=32 [p];[b][p] paletteuse",
        gifPath
    };
    log.info("Command: {}", String.join(" ", command));
    shell.exec(command);
    log.info("Done generating GIF for video={} s={} length={}", videoPath, start, duration);
    return gifName;
  }

  private void downloadVideo(String url, String videoPath) {
    log.info("Downloading HLS media from {}", url);
    String workingDir = FWUtils.getCanonicalPath(new File(videoPath).getParent());
    String[] command = {
        "docker",
        "run",
        "-v",
        workingDir + ":" + workingDir,
        "-w",
        workingDir,
        dockerImageProps.getFfmpeg(),
        "-y",
        "-i",
        url,
        "-bsf:a ",
        "aac_adtstoasc",
        "-vcodec",
        "copy",
        "-c",
        "copy",
        "-crf",
        "22",
        FWUtils.getCanonicalPath(videoPath)
    };
    log.info("Command: {}", String.join(" ", command));
    shell.exec(command);
    log.info("Done converting HLS media to mp4: {}", videoPath);
  }

  @Override
  @Async
  public void deleteGif(String url) {
    if (!ObjectUtils.isEmpty(url)) {
      String[] tokens = url.replace("\"", "").split(awsProps.getCdn());
      if (tokens.length > 1) {
        log.info("Deleted GIF at {} from S3", url);
        s3Service.deleteObject(awsProps.getBucket(), tokens[1]);
      }
    }
  }
}
