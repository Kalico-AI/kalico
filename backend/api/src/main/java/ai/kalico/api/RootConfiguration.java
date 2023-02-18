package ai.kalico.api;

import ai.kalico.api.controller.ControllerConfiguration;
import ai.kalico.api.data.DataConfiguration;
import ai.kalico.api.props.PropConfiguration;
import ai.kalico.api.service.ServiceConfiguration;
import ai.kalico.api.utils.migration.FlywayMigration;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.springframework.context.annotation.Primary;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.annotation.RequestScope;

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
  private final FlywayMigration flywayMigration;
  public static final ExecutorService executor = Executors.newCachedThreadPool();

  @PostConstruct
  public void onStart() {
    flywayMigration.migrate(false);
  }

//  @Bean
//  @RequestScope
//  @Primary
//  public RequestHeaders requestHeader(HttpServletRequest httpServletRequest) {
//    // Normalize the headers by lower-casing all the keys
//    Map<String, String> normalized = new HashMap<>();
//    Enumeration<String> headers = httpServletRequest.getHeaderNames();
//    while (headers.hasMoreElements()) {
//      String header = headers.nextElement();
//      normalized.put(header.toLowerCase(), httpServletRequest.getHeader(header));
//    }
//    return new RequestHeaders(normalized);
//  }
//
//  @Getter
//  @Setter
//  @AllArgsConstructor
//  public static class RequestHeaders {
//    private Map<String, String> headers = new HashMap<>();
//  }
}
