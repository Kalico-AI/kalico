package com.kalico.api.service.instagram4j.responses.feed;

import com.kalico.api.service.instagram4j.models.feed.Reel;
import com.kalico.api.service.instagram4j.models.live.Broadcast;
import com.kalico.api.service.instagram4j.responses.IGResponse;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class FeedReelsTrayResponse extends IGResponse {
    private List<Reel> tray;
    private List<Broadcast> broadcasts;
}
