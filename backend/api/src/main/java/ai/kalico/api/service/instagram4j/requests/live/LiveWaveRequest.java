package com.kalico.api.service.instagram4j.requests.live;

import com.kalico.api.service.instagram4j.IGClient;
import com.kalico.api.service.instagram4j.models.IGPayload;
import com.kalico.api.service.instagram4j.requests.IGPostRequest;
import com.kalico.api.service.instagram4j.responses.IGResponse;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LiveWaveRequest extends IGPostRequest<IGResponse> {
    @NonNull
    private String broadcast_id, _viewer_id;

    @Override
    protected IGPayload getPayload(IGClient client) {
        return new IGPayload() {
            @Getter
            private String viewer_id = _viewer_id;
        };
    }

    @Override
    public String path() {
        return "live/" + broadcast_id + "/wave/";
    }

    @Override
    public Class<IGResponse> getResponseType() {
        return IGResponse.class;
    }

}
