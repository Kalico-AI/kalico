package com.kalico.api.service.instagram4j.requests.media;

import com.kalico.api.service.instagram4j.IGClient;
import com.kalico.api.service.instagram4j.models.IGPayload;
import com.kalico.api.service.instagram4j.requests.IGPostRequest;
import com.kalico.api.service.instagram4j.responses.media.MediaCommentResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@AllArgsConstructor
public class MediaCommentRequest extends IGPostRequest<MediaCommentResponse> {
    @NonNull
    private String id, _comment_text;
    private String _replied_to_comment_id;

    @Override
    protected IGPayload getPayload(IGClient client) {
        return new MediaCommentPayload();
    }

    @Override
    public String path() {
        return "media/" + id + "/comment/";
    }

    @Override
    public Class<MediaCommentResponse> getResponseType() {
        return MediaCommentResponse.class;
    }

    @Getter @Setter
    @JsonInclude(Include.NON_NULL)
    private class MediaCommentPayload extends IGPayload {
        private String comment_text = _comment_text;
        private String replied_to_comment_id = _replied_to_comment_id;
    }

}
