package com.kalico.api.service.instagram4j.responses.feed;

import com.kalico.api.service.instagram4j.models.feed.Reel;
import com.kalico.api.service.instagram4j.models.media.timeline.TimelineMedia;
import com.kalico.api.service.instagram4j.responses.IGPaginatedResponse;
import com.kalico.api.service.instagram4j.responses.IGResponse;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class FeedTagResponse extends IGResponse implements IGPaginatedResponse {
    private List<TimelineMedia> ranked_items;
    private List<TimelineMedia> items;
    private Reel story;
    private int num_results;
    private String next_max_id;
    private boolean more_available;
}
