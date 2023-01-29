package com.kalico.api;

import com.kalico.api.controller.ControllerConfiguration;
import com.kalico.api.data.DataConfiguration;
import com.kalico.api.props.PropConfiguration;
import com.kalico.api.service.ServiceConfiguration;
import com.kalico.api.service.cms.CmsService;
import com.kalico.api.utils.mapper.OffsetDateTimeDeserializer;
import com.kalico.api.utils.mapper.OffsetDateTimeSerializer;
import com.kalico.api.utils.migration.FlywayMigration;
import com.kalico.api.utils.migration.FlywayMigrationConfiguration;
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
