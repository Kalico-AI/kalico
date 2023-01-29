package com.kalico.api.service.instagram4j.responses.live;

import com.kalico.api.service.instagram4j.models.user.Profile;
import com.kalico.api.service.instagram4j.responses.IGResponse;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class LiveBroadcastGetViewerListResponse extends IGResponse {
    private List<Profile> users;
}
