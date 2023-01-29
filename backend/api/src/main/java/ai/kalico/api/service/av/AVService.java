package com.kalico.api.service.av;


import com.kalico.api.dto.VideoInfoDto;
import com.kalico.model.GenericResponse;
import java.util.function.Function;

/**
 * @author Bizuwork Melesse
 * created on 6/12/22
 */
public interface AVService {
    void startVideoProcessing(String url, Function<VideoInfoDto, GenericResponse> callback);
    String extractYouTubeVideoId(String url);
    void processInstagramVideo(VideoInfoDto videoInfoDto);
    void processYouTubeVideo(VideoInfoDto videoInfoDto);
}
