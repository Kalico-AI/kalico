package ai.kalico.api;

import ai.kalico.api.controller.ControllerConfiguration;
import ai.kalico.api.data.DataConfiguration;
import ai.kalico.api.props.PropConfiguration;
import ai.kalico.api.service.ServiceConfiguration;
import ai.kalico.api.service.cms.CmsService;
import ai.kalico.api.utils.mapper.OffsetDateTimeDeserializer;
import ai.kalico.api.utils.mapper.OffsetDateTimeSerializer;
import ai.kalico.api.utils.migration.FlywayMigration;
import ai.kalico.api.utils.migration.FlywayMigrationConfiguration;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.jackson.nullable.JsonNullableModule;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;

import javax.annotation.PostConstruct;
import java.time.OffsetDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Bizuwork Melesse
 * created on 2/13/21
 */
@Slf4j
@Configuration
@Import({
        ControllerConfiguration.class,
        DataConfiguration.class,
        PropConfiguration.class,
        ServiceConfiguration.class
})
@RequiredArgsConstructor
public class RootConfiguration {

  public static final ExecutorService executor = Executors.newCachedThreadPool();
}
