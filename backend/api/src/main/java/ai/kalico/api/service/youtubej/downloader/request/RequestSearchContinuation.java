package com.kalico.api.service.youtubej.downloader.request;

import com.kalico.api.service.youtubej.model.search.*;
import com.kalico.api.service.youtubej.model.search.ContinuatedSearchResult;
import com.kalico.api.service.youtubej.model.search.SearchContinuation;
import com.kalico.api.service.youtubej.model.search.SearchResult;

public class RequestSearchContinuation extends Request<RequestSearchContinuation, SearchResult> {

    private final SearchContinuation continuation;

    public RequestSearchContinuation(SearchResult result) {
        if (!result.hasContinuation()) {
            throw new IllegalArgumentException("Search result must have a continuation");
        }
        this.continuation = ((ContinuatedSearchResult) result).continuation();
    }

    public SearchContinuation continuation() {
        return continuation;
    }
}
