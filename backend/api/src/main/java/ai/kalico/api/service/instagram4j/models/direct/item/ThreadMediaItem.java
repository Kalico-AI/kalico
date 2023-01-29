package com.kalico.api.service.instagram4j.models.direct.item;

import com.kalico.api.service.instagram4j.models.media.thread.ThreadMedia;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@JsonTypeName("media")
public class ThreadMediaItem extends ThreadItem {
    private ThreadMedia media;
}
