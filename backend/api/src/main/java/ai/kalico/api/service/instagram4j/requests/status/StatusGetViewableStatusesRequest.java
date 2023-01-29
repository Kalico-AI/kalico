package com.kalico.api.service.instagram4j.requests.status;

import com.kalico.api.service.instagram4j.requests.IGGetRequest;
import com.kalico.api.service.instagram4j.responses.IGResponse;

public class StatusGetViewableStatusesRequest extends IGGetRequest<IGResponse> {

    @Override
    public String path() {
        return "status/get_viewable_statuses/";
    }

    @Override
    public Class<IGResponse> getResponseType() {
        return IGResponse.class;
    }

}
