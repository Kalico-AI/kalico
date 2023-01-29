package com.kalico.api.service.instagram4j.responses.media;

import com.kalico.api.service.instagram4j.models.media.timeline.Comment;
import com.kalico.api.service.instagram4j.models.media.timeline.Comment.Caption;
import com.kalico.api.service.instagram4j.responses.IGPaginatedResponse;
import com.kalico.api.service.instagram4j.responses.IGResponse;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MediaGetCommentsResponse extends IGResponse implements IGPaginatedResponse {
    private List<Comment> comments;
    private Caption caption;
    private String next_max_id;

    public boolean isMore_available() {
        return next_max_id != null;
    }
}
