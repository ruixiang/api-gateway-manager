package org.itachi.api.gateway.manage.repositories;

import org.itachi.api.gateway.manage.repositories.jpa.JpaUserRepository;
import org.springframework.stereotype.Component;

/**
 * Created by itachi on 2018/1/16.
 * User: itachi
 * Date: 2018/1/16
 * Time: 14:53
 *
 * @author itachi
 */
@Component
public class AdminUserRepositoryPopulator extends RepositoryPopulator {

    public AdminUserRepositoryPopulator() {
        super("users.json", JpaUserRepository.class);
    }

}
