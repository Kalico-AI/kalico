package com.kalico.api.service.youtubej.downloader.request;

import com.kalico.api.service.youtubej.model.search.SearchResult;
import com.kalico.api.service.youtubej.model.search.query.Searchable;

public class RequestSearchable extends Request<RequestSearchable, SearchResult> {

    private final String searchPath;

    public RequestSearchable(Searchable searchable) {
        this.searchPath = searchable.searchPath();
    }

    public String searchPath() {
        return searchPath;
    }

}
