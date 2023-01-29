package com.kalico.api.service.instagram4j.requests.live;

import com.kalico.api.service.instagram4j.IGClient;
import com.kalico.api.service.instagram4j.models.IGPayload;
import com.kalico.api.service.instagram4j.requests.IGPostRequest;
import com.kalico.api.service.instagram4j.responses.IGResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LiveQuestionActivateRequest extends IGPostRequest<IGResponse> {
    @NonNull
    private String broadcast_id;
    @NonNull
    private String qid;

    @Override
    protected IGPayload getPayload(IGClient client) {
        return new IGPayload();
    }

    @Override
    public String path() {
        return String.format("live/%s/question/%s/activate/", broadcast_id, qid);
    }

    @Override
    public Class<IGResponse> getResponseType() {
        return IGResponse.class;
    }

}
