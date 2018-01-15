package org.itachi.api.gateway.manage.config.data;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

/**
 * Created by itachi on 2018/1/15.
 * User: itachi
 * Date: 2018/1/15
 * Time: 14:59
 *
 * @author itachi
 */
@Configuration
@Profile("mysql-local")
public class MySqlLocalDataSourceConfig extends AbstractLocalDataSourceConfig {

    @Bean
    public DataSource dataSource() {
        return createDataSource("jdbc:mysql://localhost/api_gateway", "com.mysql.jdbc.Driver", "", "");
    }

}