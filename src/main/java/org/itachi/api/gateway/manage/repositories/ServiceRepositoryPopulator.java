package org.itachi.api.gateway.manage.repositories;

import org.itachi.api.gateway.manage.repositories.jpa.JpaServiceRepository;
import org.springframework.stereotype.Component;

/**
 * Created by itachi on 2018/1/15.
 * User: itachi
 * Date: 2018/1/15
 * Time: 15:04
 *
 * @author itachi
 */
@Component
public class ServiceRepositoryPopulator extends RepositoryPopulator {

    public ServiceRepositoryPopulator() {
        super("services.json", JpaServiceRepository.class);
    }

}
