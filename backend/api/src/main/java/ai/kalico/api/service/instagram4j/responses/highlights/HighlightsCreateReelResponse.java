package com.kalico.api.service.instagram4j.responses.highlights;

import com.kalico.api.service.instagram4j.models.feed.Reel;
import com.kalico.api.service.instagram4j.responses.IGResponse;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class HighlightsCreateReelResponse extends IGResponse {
    private Reel reel;
}
