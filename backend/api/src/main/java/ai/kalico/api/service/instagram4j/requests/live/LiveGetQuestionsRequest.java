package com.kalico.api.service.instagram4j.requests.live;

import com.kalico.api.service.instagram4j.requests.IGGetRequest;
import com.kalico.api.service.instagram4j.responses.IGResponse;

public class LiveGetQuestionsRequest extends IGGetRequest<IGResponse> {

    @Override
    public String path() {
        return "live/get_questions/";
    }

    @Override
    public Class<IGResponse> getResponseType() {
        return IGResponse.class;
    }

}
