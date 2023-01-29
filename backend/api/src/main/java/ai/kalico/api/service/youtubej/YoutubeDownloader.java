package com.kalico.api.service.youtubej;



import com.kalico.api.service.youtubej.cipher.CachedCipherFactory;
import com.kalico.api.service.youtubej.downloader.request.RequestChannelUploads;
import com.kalico.api.service.youtubej.downloader.request.RequestPlaylistInfo;
import com.kalico.api.service.youtubej.downloader.request.RequestSearchContinuation;
import com.kalico.api.service.youtubej.downloader.request.RequestSearchResult;
import com.kalico.api.service.youtubej.downloader.request.RequestSearchable;
import com.kalico.api.service.youtubej.downloader.request.RequestSubtitlesInfo;
import com.kalico.api.service.youtubej.downloader.request.RequestVideoFileDownload;
import com.kalico.api.service.youtubej.downloader.request.RequestVideoInfo;
import com.kalico.api.service.youtubej.downloader.request.RequestVideoStreamDownload;
import com.kalico.api.service.youtubej.downloader.request.RequestWebpage;
import com.kalico.api.service.youtubej.model.Utils;
import java.io.File;
import java.io.IOException;
import java.util.List;

import com.kalico.api.service.youtubej.downloader.Downloader;
import com.kalico.api.service.youtubej.downloader.DownloaderImpl;
import com.kalico.api.service.youtubej.downloader.request.*;
import com.kalico.api.service.youtubej.downloader.response.Response;
import com.kalico.api.service.youtubej.downloader.response.ResponseImpl;
import com.kalico.api.service.youtubej.extractor.ExtractorImpl;
import com.kalico.api.service.youtubej.model.playlist.PlaylistInfo;
import com.kalico.api.service.youtubej.model.search.SearchResult;
import com.kalico.api.service.youtubej.model.subtitles.SubtitlesInfo;
import com.kalico.api.service.youtubej.model.videos.VideoInfo;
import com.kalico.api.service.youtubej.parser.Parser;
import com.kalico.api.service.youtubej.parser.ParserImpl;

public class YoutubeDownloader {

    private final Config config;
    private final Downloader downloader;
    private final Parser parser;

    public YoutubeDownloader() {
        this(Config.buildDefault());
    }

    public YoutubeDownloader(Config config) {
        this.config = config;
        this.downloader = new DownloaderImpl(config);
        this.parser = new ParserImpl(config, downloader, new ExtractorImpl(downloader), new CachedCipherFactory(downloader));
    }

    public YoutubeDownloader(Config config, Downloader downloader) {
        this(config, downloader, new ParserImpl(config, downloader, new ExtractorImpl(downloader), new CachedCipherFactory(downloader)));
    }

    public YoutubeDownloader(Config config, Downloader downloader, Parser parser) {
        this.config = config;
        this.parser = parser;
        this.downloader = downloader;
    }

    public Config getConfig() {
        return config;
    }

    public Response<VideoInfo> getVideoInfo(RequestVideoInfo request) {
        return parser.parseVideo(request);
    }

    public Response<List<SubtitlesInfo>> getSubtitlesInfo(RequestSubtitlesInfo request) {
        return parser.parseSubtitlesInfo(request);
    }

    public Response<PlaylistInfo> getChannelUploads(RequestChannelUploads request) {
        return parser.parseChannelsUploads(request);
    }

    public Response<PlaylistInfo> getPlaylistInfo(RequestPlaylistInfo request) {
        return parser.parsePlaylist(request);
    }

    public Response<SearchResult> search(RequestSearchResult request) {
        return parser.parseSearchResult(request);
    }

    public Response<SearchResult> searchContinuation(RequestSearchContinuation request) {
        return parser.parseSearchContinuation(request);
    }

    public Response<SearchResult> search(RequestSearchable request) {
        return parser.parseSearcheable(request);
    }

    public Response<File> downloadVideoFile(RequestVideoFileDownload request) {
        File outDir = request.getOutputDirectory();
        try {
            Utils.createOutDir(outDir);
        } catch (IOException e) {
            return ResponseImpl.error(e);
        }

        return downloader.downloadVideoAsFile(request);
    }

    public Response<Void> downloadVideoStream(RequestVideoStreamDownload request) {
        return downloader.downloadVideoAsStream(request);
    }

    public Response<String> downloadSubtitle(RequestWebpage request) {
        return downloader.downloadWebpage(request);
    }

}
