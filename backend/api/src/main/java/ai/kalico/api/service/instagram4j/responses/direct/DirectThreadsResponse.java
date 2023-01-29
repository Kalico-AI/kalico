package com.kalico.api.service.instagram4j.responses.direct;

import com.kalico.api.service.instagram4j.models.direct.IGThread;
import com.kalico.api.service.instagram4j.responses.IGResponse;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class DirectThreadsResponse extends IGResponse {
    private IGThread thread;
}
