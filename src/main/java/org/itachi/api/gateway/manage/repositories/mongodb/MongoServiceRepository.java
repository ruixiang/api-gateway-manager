package org.itachi.api.gateway.manage.repositories.mongodb;

import org.itachi.api.gateway.manage.domain.Service;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by itachi on 2018/1/15.
 * User: itachi
 * Date: 2018/1/15
 * Time: 15:26
 *
 * @author itachi
 */
@Repository
@Profile("mongodb")
public interface MongoServiceRepository extends MongoRepository<Service, String> {
}
