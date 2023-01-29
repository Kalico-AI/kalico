package com.kalico.api.service.instagram4j.requests.challenge;

import com.kalico.api.service.instagram4j.IGClient;
import com.kalico.api.service.instagram4j.models.IGPayload;
import com.kalico.api.service.instagram4j.requests.IGPostRequest;
import com.kalico.api.service.instagram4j.responses.challenge.ChallengeStateResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ChallengeResetRequest extends IGPostRequest<ChallengeStateResponse> {
    @NonNull
    private String path;

    @Override
    public IGPayload getPayload(IGClient client) {
        return new IGPayload();
    }

    @Override
    public String path() {
        return path.replace("/challenge/", "challenge/reset/");
    }

    @Override
    public Class<ChallengeStateResponse> getResponseType() {
        return ChallengeStateResponse.class;
    }

}
