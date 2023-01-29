package com.kalico.api.service.instagram4j.requests.igtv;

import com.kalico.api.service.instagram4j.requests.IGGetRequest;
import com.kalico.api.service.instagram4j.responses.IGResponse;

public class IgtvCreationToolsRequest extends IGGetRequest<IGResponse> {

    @Override
    public String path() {
        return "igtv/igtv_creation_tools/";
    }

    @Override
    public Class<IGResponse> getResponseType() {
        return IGResponse.class;
    }
}
