package org.itachi.api.gateway.manage.domain;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.util.UUID;

/**
 * Created by itachi on 2018/1/15.
 * User: itachi
 * Date: 2018/1/15
 * Time: 15:20
 *
 * @author itachi
 */
public class RandomIdGenerator implements IdentifierGenerator {
    @Override
    public Serializable generate(SessionImplementor session, Object object) throws HibernateException {
        return generateId();
    }

    public String generateId() {
        return UUID.randomUUID().toString();
    }
}
