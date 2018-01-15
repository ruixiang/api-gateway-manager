package org.itachi.api.gateway.manage.config.data;

import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;

/**
 * Created by itachi on 2018/1/15.
 * User: itachi
 * Date: 2018/1/15
 * Time: 14:47
 *
 * @author itachi
 */
public class AbstractLocalDataSourceConfig {

    protected DataSource createDataSource(String jdbcUrl, String driverClass, String userName, String password) {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl(jdbcUrl);
        dataSource.setDriverClassName(driverClass);
        dataSource.setUsername(userName);
        dataSource.setPassword(password);
        return dataSource;
    }
}
