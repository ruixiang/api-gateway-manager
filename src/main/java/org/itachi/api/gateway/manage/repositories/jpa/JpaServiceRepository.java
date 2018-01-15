package org.itachi.api.gateway.manage.repositories.jpa;

import org.itachi.api.gateway.manage.domain.Service;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by itachi on 2018/1/15.
 * User: itachi
 * Date: 2018/1/15
 * Time: 15:25
 *
 * @author itachi
 */
@Repository
@Profile({"in-memory", "mysql", "postgres", "oracle", "sqlserver"})
public interface JpaServiceRepository extends JpaRepository<Service, String> {
}
