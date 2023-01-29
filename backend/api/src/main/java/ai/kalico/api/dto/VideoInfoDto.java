package com.kalico.api.dto;

import com.kalico.api.service.youtubej.model.videos.VideoInfo;
import com.kalico.api.service.youtubej.model.videos.formats.Format;
import com.kalico.api.service.utils.Platform;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Biz Melesse
 * created on 11/16/22
 */
@Getter
@Setter
public class VideoInfoDto {
  private Format format;
  private VideoInfo videoInfo;
  private Platform platform;
  private String url;
  private String imageUrl;
  private String caption;
  private String permalink;
  private String videoIdOverride;
}
