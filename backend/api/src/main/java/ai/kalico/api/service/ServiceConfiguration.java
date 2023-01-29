package ai.kalico.api.service;


import ai.kalico.api.data.DataConfiguration;
import ai.kalico.api.props.AWSProps;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import ai.kalico.api.props.InstagramProps;
import ai.kalico.api.service.instagram4j.IGClient;
import ai.kalico.api.service.instagram4j.utils.IGUtils;
import ai.kalico.api.service.instagram4j.utils.SerializableCookieJar;
import ai.kalico.api.service.mapper.MapperConfiguration;
import ai.kalico.api.service.utils.Seed;
import ai.kalico.api.utils.migration.FlywayMigration;
import ai.kalico.api.utils.migration.FlywayMigrationConfiguration;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
/**
 * @author Bizuwork Melesse
 * created on 2/13/21
 *
 */
@Slf4j
@Configuration
@ComponentScan
@RequiredArgsConstructor
@Import({
    DataConfiguration.class,
    MapperConfiguration.class,
    FlywayMigrationConfiguration.class})
public class ServiceConfiguration {
  private final AWSProps awsProps;

  @Bean
  public AmazonS3 amazonS3() {
    AWSCredentials credentials = new BasicAWSCredentials(
      awsProps.getAccessKey(),
      awsProps.getSecretKey()
    );
    return AmazonS3ClientBuilder
      .standard()
      .withCredentials(new AWSStaticCredentialsProvider(credentials))
      .withRegion(awsProps.getRegion())
      .build();
  }

  private final InstagramProps instagramProps;
  private final Seed seed;

  @Bean
  @Qualifier("FlywayDBMigration")
  public boolean dbMigration(FlywayMigration flywayMigration) {
    flywayMigration.migrate(false);
    seed.seed();
    return true;
  }

  @Bean
  public IGClient igClient(@Qualifier(value = "FlywayDBMigration") Boolean dbMigrated, SerializableCookieJar cookieJar) {
    IGUtils.serializableCookieJar = cookieJar;
    if (dbMigrated) {
      if (instagramProps.getDoLogin() != null && instagramProps.getDoLogin()) {
        return IGClient.builder()
            .username(instagramProps.getUsername())
            .password(instagramProps.getPassword())
            .build();
      }
      return IGClient.builder().build();
    }
    throw new BeanCreationException("Failed to create igClient bean because DB migration has not happened yet");
  }
}
