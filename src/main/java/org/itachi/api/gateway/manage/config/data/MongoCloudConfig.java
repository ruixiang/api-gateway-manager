package org.itachi.api.gateway.manage.config.data;

import org.springframework.cloud.config.java.AbstractCloudConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.MongoDbFactory;

/**
 * Created by itachi on 2018/1/15.
 * User: itachi
 * Date: 2018/1/15
 * Time: 14:55
 *
 * @author itachi
 */
@Configuration
@Profile("mongodb-cloud")
public class MongoCloudConfig extends AbstractCloudConfig {

    @Bean
    public MongoDbFactory mongoDbFactory() {
        return connectionFactory().mongoDbFactory();
    }

}