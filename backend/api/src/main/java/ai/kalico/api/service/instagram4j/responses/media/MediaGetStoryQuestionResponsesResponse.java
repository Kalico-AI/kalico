package com.kalico.api.service.instagram4j.responses.media;

import com.kalico.api.service.instagram4j.models.media.reel.ResponderInfo;
import com.kalico.api.service.instagram4j.responses.IGPaginatedResponse;
import com.kalico.api.service.instagram4j.responses.IGResponse;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MediaGetStoryQuestionResponsesResponse extends IGResponse implements IGPaginatedResponse {
    private ResponderInfo responder_info;

    @Override
    public String getNext_max_id() {
        return this.responder_info.getMax_id();
    }

    @Override
    public boolean isMore_available() {
        return this.responder_info.isMore_available();
    }
}
