package com.kalico.api.service.instagram4j.responses.media;

import com.kalico.api.service.instagram4j.responses.IGResponse;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MediaPermalinkResponse extends IGResponse {
    private String permalink;
}
