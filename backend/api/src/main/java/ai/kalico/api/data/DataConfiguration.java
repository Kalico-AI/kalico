package com.kalico.api.data;


import com.kalico.api.data.postgres.PostgresDBDataConfiguration;
import com.kalico.api.props.PropConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author Bizuwork Melesse
 * created on 2/13/21
 *
 */
@Configuration
@ComponentScan
@Import({
        PostgresDBDataConfiguration.class,
        PropConfiguration.class
})
public class DataConfiguration {
}
