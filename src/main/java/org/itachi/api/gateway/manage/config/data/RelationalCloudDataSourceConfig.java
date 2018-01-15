package org.itachi.api.gateway.manage.config.data;

import org.springframework.cloud.config.java.AbstractCloudConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

/**
 * Created by itachi on 2018/1/15.
 * User: itachi
 * Date: 2018/1/15
 * Time: 15:02
 *
 * @author itachi
 */
@Configuration
@Profile({"mysql-cloud", "postgres-cloud", "oracle-cloud", "sqlserver-cloud"})
public class RelationalCloudDataSourceConfig extends AbstractCloudConfig {

    @Bean
    public DataSource dataSource() {
        return connectionFactory().dataSource();
    }

}
