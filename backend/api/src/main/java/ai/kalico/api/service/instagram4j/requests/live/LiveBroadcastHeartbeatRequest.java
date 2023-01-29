package com.kalico.api.service.instagram4j.requests.live;

import com.kalico.api.service.instagram4j.IGClient;
import com.kalico.api.service.instagram4j.models.IGPayload;
import com.kalico.api.service.instagram4j.requests.IGPostRequest;
import com.kalico.api.service.instagram4j.responses.live.LiveBroadcastHeartbeatResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LiveBroadcastHeartbeatRequest extends IGPostRequest<LiveBroadcastHeartbeatResponse> {
    @NonNull
    private String broadcast_id;

    @Override
    protected IGPayload getPayload(IGClient client) {
        return new IGPayload();
    }

    @Override
    public String path() {
        return "live/" + broadcast_id + "/heartbeat_and_get_viewer_count/";
    }

    @Override
    public Class<LiveBroadcastHeartbeatResponse> getResponseType() {
        return LiveBroadcastHeartbeatResponse.class;
    }

}
