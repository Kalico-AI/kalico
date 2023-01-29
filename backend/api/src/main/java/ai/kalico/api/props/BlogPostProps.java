package com.kalico.api.props;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.List;

/**
 * @author Bizuwork Melesse
 * created on 11/05/2022
 */
@Primary
@Getter @Setter
@Configuration
@ConfigurationProperties(prefix = "blog")
public class BlogPostProps {
    private Boolean seedDb;
    private String baseSiteUrl;
    private String baseImageCdnUrl;
    private Double fps;
    private String urlNotSupportedMessage;
    private List<String> supportedDomains;
}
