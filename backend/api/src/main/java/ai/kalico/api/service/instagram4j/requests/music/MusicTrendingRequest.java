package com.kalico.api.service.instagram4j.requests.music;

import com.kalico.api.service.instagram4j.IGClient;
import com.kalico.api.service.instagram4j.models.IGPayload;
import com.kalico.api.service.instagram4j.requests.IGPostRequest;
import com.kalico.api.service.instagram4j.requests.music.MusicSearchRequest.MusicQueryPayload;
import com.kalico.api.service.instagram4j.responses.music.MusicTrackResponse;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class MusicTrendingRequest extends IGPostRequest<MusicTrackResponse> {
    private String _cursor = "0";

    @Override
    protected IGPayload getPayload(IGClient client) {
        return new MusicQueryPayload(client.getSessionId(), _cursor);
    }

    @Override
    public String path() {
        return "music/trending/";
    }

    @Override
    public Class<MusicTrackResponse> getResponseType() {
        return MusicTrackResponse.class;
    }

}
