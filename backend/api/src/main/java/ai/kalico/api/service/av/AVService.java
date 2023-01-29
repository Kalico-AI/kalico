package ai.kalico.api.service.av;


import ai.kalico.api.dto.VideoInfoDto;

/**
 * @author Bizuwork Melesse
 * created on 6/12/22
 */
public interface AVService {
    void processMedia(String url, Long projectId, String file, String fileExtension);
    String extractYouTubeVideoId(String url);
    void processInstagramVideo(VideoInfoDto videoInfoDto);
    void processYouTubeVideo(VideoInfoDto videoInfoDto);
    void processUploadedVideo(String file, String fileExtension, String mediaId);
    void processUploadedAudio(String file, String fileExtension, String mediaId);
}
