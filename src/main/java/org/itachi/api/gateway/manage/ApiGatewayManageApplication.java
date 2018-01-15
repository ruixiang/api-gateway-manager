package org.itachi.api.gateway.manage;

import org.itachi.api.gateway.manage.config.SpringApplicationContextInitializer;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

/**
 * Created by itachi on 2018/1/15.
 * User: itachi
 * Date: 2018/1/15
 * Time: 14:34
 *
 * @author itachi
 */
@SpringBootApplication
public class ApiGatewayManageApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        new SpringApplicationBuilder(ApiGatewayManageApplication.class).
                initializers(new SpringApplicationContextInitializer())
                .application()
                .run(args);
    }
}