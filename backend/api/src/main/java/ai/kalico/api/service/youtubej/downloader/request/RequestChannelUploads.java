package com.kalico.api.service.youtubej.downloader.request;

import com.kalico.api.service.youtubej.model.playlist.PlaylistInfo;

public class RequestChannelUploads extends Request<RequestPlaylistInfo, PlaylistInfo>  {

    private final String channelId;

    public RequestChannelUploads(String channelId) {
        this.channelId = channelId;
    }

    public String getChannelId() {
        return channelId;
    }

}
