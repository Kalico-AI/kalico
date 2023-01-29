package com.kalico.api.service.instagram4j.actions.feed;

import com.kalico.api.service.instagram4j.IGClient;
import com.kalico.api.service.instagram4j.requests.IGPaginatedRequest;
import com.kalico.api.service.instagram4j.requests.IGRequest;
import com.kalico.api.service.instagram4j.responses.IGPaginatedResponse;
import com.kalico.api.service.instagram4j.responses.IGResponse;

public class FeedIterator<T extends IGRequest<R> & IGPaginatedRequest, R extends IGResponse & IGPaginatedResponse>
        extends CursorIterator<IGRequest<R>, R> {

    public FeedIterator(IGClient client, T t) {
        super(client, t, (request, id) -> t.setMax_id(id), (e) -> e.getNext_max_id(), (e) -> e.isMore_available());
    }

}
