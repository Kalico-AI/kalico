package com.kalico.api.service.utils;

import com.kalico.api.data.postgres.entity.SampledImageEntity;
import com.kalico.api.data.postgres.entity.VideoContentEntity;
import com.kalico.api.data.postgres.repo.SampledImageRepo;
import com.kalico.api.data.postgres.repo.VideoContentRepo;
import com.kalico.api.dto.Pair;
import com.kalico.api.props.AWSProps;
import com.kalico.api.props.DockerImageProps;
import com.kalico.api.service.aws.S3Service;
import com.kalico.api.service.download.DownloadService;
import com.kalico.api.service.ocr.OcrRequest;
import com.kalico.api.service.ocr.OcrService;
import com.kalico.api.service.stt.SpeechToTextService;
import com.kalico.api.service.stt.SttRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Biz Melesse created on 12/31/22
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AVAsyncHelper {
  private final ShellService shell;
  private final SpeechToTextService stt;
  private final OcrService ocr;
  private final AWSProps awsProps;
  private final S3Service s3Service;
  private final VideoContentRepo videoContentRepo;
  private final SampledImageRepo sampledImageRepo;
  private final DockerImageProps dockerImageProps;
  private final DownloadService downloadService;

  @Async
  public void uploadImages(OcrRequest ocrRequest) {
    List<String> fileNames;
    try {
      fileNames = getFileNames(".jpg", ocrRequest.getOcrPath());
      List<Pair<String, String>> fileNameKeyList = new ArrayList<>();
      List<String> keys = new ArrayList<>();
      for (String fileName : fileNames) {
        String[] tokens = fileName.split("/");
        if (tokens.length > 0) {
          String shortName = tokens[tokens.length - 1];
          String key = awsProps.getImageFolder() + "/" + ocrRequest.getVideoId() + "/" + shortName;
          Pair<String, String> p = new Pair<>(fileName, key);
          fileNameKeyList.add(p);
          keys.add(key);
        }
      }
      if (fileNameKeyList.size() > 0) {
        s3Service.uploadImagesAsync(awsProps.getBucket(), fileNameKeyList, S3Service.IMAGE_TYPE);
      }
      createSampledImageRecord(ocrRequest.getVideoId(), keys);
    } catch (IOException e) {
      log.error("AVAsyncHelper.uploadImages {}", e.getLocalizedMessage());
    }
  }

  @Async
  public void saveOcrTextToDB(OcrRequest request, String path) {
    try {
      List<String> fileNames = getFileNames(".txt", path);
      fileNames = fileNames
          .stream()
          .sorted().collect(Collectors.toList());
      StringBuilder builder = new StringBuilder("<br>");
      String prevText = "";
      for (String fileName : fileNames) {
        File file = new File(fileName);
        String ocrText = new String(Files.readAllBytes(file.toPath()));
        if (isSimilar(prevText, ocrText) || ocrText.trim().length() < 2) {
          prevText = ocrText;
          continue;
        }
        prevText = ocrText;
        String[] tokens = fileName.split("/");
        if (tokens.length > 0) {
          String shortName = tokens[tokens.length - 1].replace(".txt", "");
          String formattedText = shortName + "<br>----------<br>" + ocrText.replace("\n", "<br>") + "<br>";
          builder.append(formattedText);
        }
      }
      VideoContentEntity entity = videoContentRepo.findByVideoId(request.getVideoId());
      if (entity != null) {
        entity.setOnScreenText(builder.toString());
        videoContentRepo.save(entity);
      }

    } catch (IOException e) {
      log.error("AVAsyncHelper.saveOcrTextToDB {}", e.getLocalizedMessage());
    }

  }

  private boolean isSimilar(String prevText, String currText) {
    double threshold = 0.60;
    LevenshteinDistance d = new LevenshteinDistance();
    int distance = d.apply(prevText, currText);
    double ratio = 1.0 - ((double) distance) / (Math.max(prevText.length(), currText.length()));
    return ratio >= threshold;
  }

  @Transactional
  public void createSampledImageRecord(String videoId, List<String> keys) {
    VideoContentEntity videoContentEntity = videoContentRepo.findByVideoId(videoId);
    List<SampledImageEntity> sampledImageEntities = new ArrayList<>();
    if (videoContentEntity != null) {
      for (String key : keys) {
        SampledImageEntity sampledImage = new SampledImageEntity();
        sampledImage.setBlogPostId(videoContentEntity.getBlogPostId());
        sampledImage.setImageKey(key);
        sampledImageEntities.add(sampledImage);
      }
      if (sampledImageEntities.size() > 0) {
        // Delete any existing images
        List<SampledImageEntity> toDelete = sampledImageRepo.findByBlogPostIdOrderByImageKeyAsc(videoContentEntity.getBlogPostId());
        if (toDelete.size() > 0) {
          sampledImageRepo.deleteAll(toDelete);
        }
        sampledImageRepo.saveAll(sampledImageEntities);
      }
    }
  }

  @SneakyThrows
  @Async
  public void processAudio(String videoId) {
    String videoPath = getVideoPath(videoId);
    String audioPath = getAudioPath(videoId);
    log.trace("Extracting audio track from video file {}", videoPath);
    if (Files.exists(Path.of(videoPath)) && !Files.exists(Path.of(audioPath))) {
      //-i input file
      //-vn no video
      //-ac audio channel (1 = mono, 2 = stereo)
      //-ar audio sample rate
      //-ab audio bitrate
      //-f output format
      //-acodec bitrate
      log.info("FFMPEG audio extraction in progress for videoId {}", videoId);
      String workingDir = FWUtils.getCanonicalPath(new File(videoPath).getParent());
      String[] command = {
          "docker",
          "run",
          "-v",
          workingDir + ":" + workingDir,
          "-w",
          workingDir,
          dockerImageProps.getFfmpeg(),
          "-i",
          FWUtils.getCanonicalPath(videoPath),
          "-vn",
          "-ac",
          "1",
          "-ar",
          "16000",
          "-acodec",
          "pcm_s16le",
          "-f",
          "wav",
          FWUtils.getCanonicalPath(audioPath)
      };
      log.info("Command: {}", String.join(" ", command));
      shell.exec(command);
      log.info("FFMPEG audio extraction done for videoId {}", videoId);
    }
    else {
      log.trace("Failed to locate video file at {}", videoPath);
    }
    runStt(videoId, audioPath);
  }

  private void runStt(String videoId, String audioPath) {
    if (!Files.exists(Path.of(getTranscriptPath(videoId)))) {
      SttRequest sttRequest = new SttRequest();
      sttRequest.setPath(audioPath);
      sttRequest.setVideoId(videoId);
      sttRequest.setLanguage("English");
      stt.transcribe(sttRequest);
      saveTranscriptToDb(videoId);
    }
  }

  @Async
  public void processImages(String videoId) {
    OcrRequest ocrRequest = new OcrRequest();
    ocrRequest.setVideoId(videoId);
    ocrRequest.setVideoPath(getVideoPath(videoId));
    ocrRequest.setOcrPath(getOcrPath(videoId));
    ocrRequest.setOcrTesseractPath(getOCrModifiedImagePath(ocrRequest.getOcrPath()));
    ocrRequest.setLanguage("eng");
    ocr.runOcr(ocrRequest);
    saveOcrTextToDB(ocrRequest, ocrRequest.getOcrTesseractPath());
    uploadImages(ocrRequest);
  }

  private void saveTranscriptToDb(String videoId) {
    String transcriptPath = getTranscriptPath(videoId);
    if (Files.exists(Path.of(transcriptPath))) {
      File file = new File(transcriptPath);
      try {
        String transcript = new String(Files.readAllBytes(file.toPath()));
        VideoContentEntity entity = videoContentRepo.findByVideoId(videoId);
        if (entity != null) {
          entity.setRawTranscript(transcript.replace("\n", "<br>"));
          videoContentRepo.save(entity);
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  private String getOCrModifiedImagePath(String ocrPath) {
    return ocrPath + "/tesseract";
  }

  private String getOcrPath(String videoId) {
    return getParentPath(videoId) + "/" + "ocr";
  }

  public String getTranscriptPath(String videoId) {
    return getParentPath(videoId) + "/" + videoId + ".wav.vtt";
  }

  public String getAudioPath(String videoId) {
    return getParentPath(videoId) + "/" + videoId + ".wav";
  }

  public String getVideoPath(String videoId) {
    return getParentPath(videoId) + "/" + videoId + ".mp4";
  }

  public String getHlsPath(String videoId) {
    return getParentPath(videoId) + "/hls/" + videoId + ".m3u8";
  }

  public String getGifPath(String videoId, String gifName) {
    return String.format("%s/%s",
        getParentPath(videoId),
        gifName);
  }

  public String getGifName(String videoId, String start, String duration) {
    return String.format("%ss%sd%s.gif",
        videoId,
        start.replace(".", ""),
        duration.replace(".", ""));
  }

  public String getParentPath(String videoId) {
    final String baseFilePath = "/tmp/videos";
    return baseFilePath + "/" + videoId;
  }

  private List<String> getFileNames(String ext, String path) throws IOException {
    List<String> results = new ArrayList<>();
    File[] files = new File(path).listFiles();
    if (files != null) {
      for (File file : files) {
        if (file.isFile() && file.getName().contains(ext)) {
          results.add(FWUtils.getCanonicalPath(file.getPath()));
        }
      }
    }
    return results;
  }

  @Async
  public void processHls(String videoId, String path) {
    String hlsPath = getHlsPath(videoId);
    downloadService.generateHlsManifest(path, hlsPath);
    s3Service.uploadHlsData(awsProps.getBucket(), videoId, hlsPath, awsProps.getStreamFolder());
  }
}
