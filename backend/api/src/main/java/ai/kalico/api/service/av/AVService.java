package ai.kalico.api.service.av;


import ai.kalico.api.dto.VideoInfoDto;
import com.kalico.model.ContentPreviewResponse;

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
    ContentPreviewResponse downloadContentMetadata(String url);

    /**
     * Turn any mobile urls into regular urls and perform additional pre-processing as necessary
     *
     * @param url
     * @return
     */
    String normalizeUrl(String url);
}
