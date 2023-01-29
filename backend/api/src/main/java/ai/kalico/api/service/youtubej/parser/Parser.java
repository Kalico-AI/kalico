package com.kalico.api.service.youtubej.parser;

import com.kalico.api.service.youtubej.downloader.request.RequestChannelUploads;
import com.kalico.api.service.youtubej.downloader.request.RequestPlaylistInfo;
import com.kalico.api.service.youtubej.downloader.request.RequestSearchContinuation;
import com.kalico.api.service.youtubej.downloader.request.RequestSearchResult;
import com.kalico.api.service.youtubej.downloader.request.RequestSearchable;
import com.kalico.api.service.youtubej.downloader.request.RequestSubtitlesInfo;
import com.kalico.api.service.youtubej.downloader.request.RequestVideoInfo;
import com.kalico.api.service.youtubej.model.search.SearchResult;
import com.kalico.api.service.youtubej.model.videos.VideoInfo;
import java.util.List;

import com.kalico.api.service.youtubej.downloader.request.*;
import com.kalico.api.service.youtubej.downloader.response.Response;
import com.kalico.api.service.youtubej.model.playlist.PlaylistInfo;
import com.kalico.api.service.youtubej.model.subtitles.SubtitlesInfo;

public interface Parser {

    /* Video */

    Response<VideoInfo> parseVideo(RequestVideoInfo request);

    /* Playlist */

    Response<PlaylistInfo> parsePlaylist(RequestPlaylistInfo request);

    /* Channel uploads */

    Response<PlaylistInfo> parseChannelsUploads(RequestChannelUploads request);

    /* Subtitles */

    Response<List<SubtitlesInfo>> parseSubtitlesInfo(RequestSubtitlesInfo request);

    /* Search */

    Response<SearchResult> parseSearchResult(RequestSearchResult request);

    Response<SearchResult> parseSearchContinuation(RequestSearchContinuation request);

    Response<SearchResult> parseSearcheable(RequestSearchable request);

}
