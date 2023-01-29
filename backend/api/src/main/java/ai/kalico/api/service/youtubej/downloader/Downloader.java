package com.kalico.api.service.youtubej.downloader;

import com.kalico.api.service.youtubej.downloader.request.RequestVideoFileDownload;
import com.kalico.api.service.youtubej.downloader.request.RequestVideoStreamDownload;
import com.kalico.api.service.youtubej.downloader.request.RequestWebpage;
import com.kalico.api.service.youtubej.downloader.response.Response;

import java.io.File;

public interface Downloader {

    Response<String> downloadWebpage(RequestWebpage request);

    Response<File> downloadVideoAsFile(RequestVideoFileDownload request);

    Response<Void> downloadVideoAsStream(RequestVideoStreamDownload request);

}
