package com.kalico.api.service.instagram4j.responses.media;

import com.kalico.api.service.instagram4j.models.media.timeline.Comment;
import com.kalico.api.service.instagram4j.responses.IGResponse;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MediaCommentResponse extends IGResponse {
    private Comment comment;
}
