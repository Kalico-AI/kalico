package ai.kalico.api.service.instagram4j.actions.feed;

import ai.kalico.api.service.instagram4j.IGClient;
import ai.kalico.api.service.instagram4j.requests.IGPageRankTokenRequest;
import ai.kalico.api.service.instagram4j.requests.IGRequest;
import ai.kalico.api.service.instagram4j.responses.IGPageRankTokenResponse;
import ai.kalico.api.service.instagram4j.responses.IGResponse;

public class PageRankTokenIterator<T extends IGRequest<R> & IGPageRankTokenRequest, R extends IGResponse & IGPageRankTokenResponse>
        extends CursorIterator<IGRequest<R>, R> {
    
    public PageRankTokenIterator(IGClient client, T t) {
        super(client, t, (req, id) -> IGPageRankTokenResponse.setFromFormat((IGPageRankTokenRequest) req, id), (res) -> res.toNextId(), (res) -> res.isHas_more());
    }
    
}
