package ai.kalico.api.props;


import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author Bizuwork Melesse
 * created on 2/13/21
 *
 */
@Configuration
@ComponentScan
@EnableConfigurationProperties(value = {
    AWSProps.class,
    FirebaseProps.class,
    FlywayProps.class,
    PostgresDataSourceProps.class,
    SourceProps.class,
    SwaggerUIProps.class,
    DefaultUserProps.class,
    BlogPostProps.class,
    InstagramProps.class,
    AuthorizedUserProps.class,
    DockerImageProps.class
})
public class PropConfiguration {
}
