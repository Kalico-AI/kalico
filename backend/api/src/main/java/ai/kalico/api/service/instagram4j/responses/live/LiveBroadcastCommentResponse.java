package com.kalico.api.service.instagram4j.responses.live;

import com.kalico.api.service.instagram4j.models.media.timeline.Comment;
import com.kalico.api.service.instagram4j.responses.IGResponse;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class LiveBroadcastCommentResponse extends IGResponse {
    private Comment comment;
}
