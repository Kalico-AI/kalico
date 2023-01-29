package com.kalico.api.service.instagram4j.requests.music;

import com.kalico.api.service.instagram4j.IGClient;
import com.kalico.api.service.instagram4j.models.IGPayload;
import com.kalico.api.service.instagram4j.requests.IGPostRequest;
import com.kalico.api.service.instagram4j.responses.music.MusicGetResponse;

public class MusicGetGenresRequest extends IGPostRequest<MusicGetResponse> {

    @Override
    protected IGPayload getPayload(IGClient client) {
        return new IGPayload();
    }

    @Override
    public String path() {
        return "music/genres/";
    }

    @Override
    public Class<MusicGetResponse> getResponseType() {
        return MusicGetResponse.class;
    }

}
