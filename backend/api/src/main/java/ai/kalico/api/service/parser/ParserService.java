package com.kalico.api.service.parser;

import com.gargoylesoftware.htmlunit.WebClient;
import com.kalico.api.dto.VideoInfoDto;

/**
 * @author Bizuwork Melesse
 * created on 6/20/22
 */
public interface ParserService {
    /**
     * Parse HTML soup and generate structured data for
     * downstream consumption
     * @param soup html soup
     * @param url
     * @param contentId
     * @param client
     */
    VideoInfoDto processSoup(String soup, String url, String contentId, WebClient client);

    VideoInfoDto getMediaMetadata(String igUrl, String contentId);
}
