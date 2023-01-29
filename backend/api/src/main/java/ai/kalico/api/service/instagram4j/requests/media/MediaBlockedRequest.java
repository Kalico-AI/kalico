package com.kalico.api.service.instagram4j.requests.media;

import com.kalico.api.service.instagram4j.requests.IGGetRequest;
import com.kalico.api.service.instagram4j.responses.IGResponse;

public class MediaBlockedRequest extends IGGetRequest<IGResponse> {

    @Override
    public String path() {
        return "media/blocked/";
    }

    @Override
    public Class<IGResponse> getResponseType() {
        return IGResponse.class;
    }

}
