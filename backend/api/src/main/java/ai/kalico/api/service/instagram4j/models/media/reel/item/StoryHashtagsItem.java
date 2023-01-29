package com.kalico.api.service.instagram4j.models.media.reel.item;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Getter;
import lombok.Setter;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

@Getter @Setter
@SuperBuilder
@JsonInclude(Include.NON_NULL)
public class StoryHashtagsItem extends ReelMetadataItem {
    @NonNull
    private String tag_name;

    @Override
    public String key() {
        return "story_hashtags";
    }

}
