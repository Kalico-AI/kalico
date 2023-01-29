package com.kalico.api.service.instagram4j.responses.media;

import com.kalico.api.service.instagram4j.models.media.reel.ReelMedia;
import com.kalico.api.service.instagram4j.models.user.Profile;
import com.kalico.api.service.instagram4j.responses.IGPaginatedResponse;
import com.kalico.api.service.instagram4j.responses.IGResponse;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MediaListReelMediaViewerResponse extends IGResponse implements IGPaginatedResponse {
    private List<Profile> users;
    private String next_max_id;
    private int user_count;
    private int total_viewer_count;
    private ReelMedia updated_media;

    public boolean isMore_available() {
        return next_max_id != null;
    }
}
