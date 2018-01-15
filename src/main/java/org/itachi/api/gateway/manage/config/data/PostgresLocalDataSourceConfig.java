package org.itachi.api.gateway.manage.config.data;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

/**
 * Created by itachi on 2018/1/15.
 * User: itachi
 * Date: 2018/1/15
 * Time: 15:01
 *
 * @author itachi
 */
@Configuration
@Profile("postgres-local")
public class PostgresLocalDataSourceConfig extends AbstractLocalDataSourceConfig {

    @Bean
    public DataSource dataSource() {
        return createDataSource("jdbc:postgresql://localhost/music",
                "org.postgresql.Driver", "postgres", "postgres");
    }

}
