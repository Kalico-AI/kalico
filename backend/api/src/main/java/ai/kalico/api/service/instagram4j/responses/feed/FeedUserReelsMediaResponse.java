package com.kalico.api.service.instagram4j.responses.feed;

import com.kalico.api.service.instagram4j.models.feed.Reel;
import com.kalico.api.service.instagram4j.responses.IGResponse;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class FeedUserReelsMediaResponse extends IGResponse {
    @JsonUnwrapped
    private Reel reel;
}
