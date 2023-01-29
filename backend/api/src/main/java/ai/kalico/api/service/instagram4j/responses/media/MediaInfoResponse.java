package com.kalico.api.service.instagram4j.responses.media;

import com.kalico.api.service.instagram4j.models.media.timeline.TimelineMedia;
import com.kalico.api.service.instagram4j.responses.IGResponse;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MediaInfoResponse extends IGResponse {
    private List<TimelineMedia> items;
    private int num_results;
    private boolean more_available;
}
