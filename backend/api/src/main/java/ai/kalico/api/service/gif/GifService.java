package com.kalico.api.service.gif;

/**
 * @author Biz Melesse created on 1/12/23
 */
public interface GifService {

  /**
   * Generate a GIF of a video already stored on S3
   *
   * @param videoId
   * @param start start time
   * @param end end time
   * @return
   */
  String generateGif(String videoId, float start, float end);

  void deleteGif(String url);
}
