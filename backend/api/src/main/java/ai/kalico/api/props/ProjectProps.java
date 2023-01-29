package ai.kalico.api.props;

import java.util.Set;
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
public class ProjectProps {
    private Boolean seedDb;
    private List<String> supportedDomains;
    private Set<String> supportedVideoFormats;
    private Set<String> supportedAudioFormats;





    private String baseSiteUrl;
    private String baseImageCdnUrl;
    private Double fps;
    private String urlNotSupportedMessage;

}
