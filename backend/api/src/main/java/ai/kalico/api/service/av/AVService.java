package ai.kalico.api.service.av;


import ai.kalico.api.dto.VideoInfoDto;

/**
 * @author Bizuwork Melesse
 * created on 6/12/22
 */
public interface AVService {
    void processMedia(String url, Long projectId, String file, String fileExtension);
    String extractYouTubeVideoId(String url);
    void processInstagramVideo(VideoInfoDto videoInfoDto, Long projectId);
    void processYouTubeVideo(VideoInfoDto videoInfoDto, Long projectId);
    void processUploadedVideo(String file, String fileExtension, String mediaId, Long projectId);
    void processUploadedAudio(String file, String fileExtension, String mediaId, Long projectId);
}
