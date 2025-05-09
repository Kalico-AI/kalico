package ai.kalico.api.service.youtubej.model.search;

import ai.kalico.api.service.youtubej.model.Utils;
import java.util.List;

import com.alibaba.fastjson.JSONObject;

public abstract class AbstractSearchResultList implements SearchResultItem {

    private String title;
    protected List<String> thumbnails;
    private String author;

    public AbstractSearchResultList() {}

    public AbstractSearchResultList(JSONObject json) {
        title = json.getJSONObject("title").getString("simpleText");
        author = Utils.parseRuns(json.getJSONObject("shortBylineText"));
    }

    @Override
    public String title() {
        return title;
    }

    public List<String> thumbnails() {
        return thumbnails;
    }

    public String author() {
        return author;
    }
}
