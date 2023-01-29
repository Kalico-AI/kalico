package com.kalico.api.service.instagram4j.models.highlights;

import com.kalico.api.service.instagram4j.models.IGBaseModel;
import com.kalico.api.service.instagram4j.models.user.Profile;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Highlight extends IGBaseModel {
    private String id;
    private long latest_reel_media;
    private Profile user;
    private String title;
    private int media_count;
}
