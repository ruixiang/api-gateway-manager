package org.itachi.api.gateway.manage.repositories;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.itachi.api.gateway.manage.domain.Service;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.init.Jackson2ResourceReader;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * Created by itachi on 2018/1/15.
 * User: itachi
 * Date: 2018/1/15
 * Time: 15:04
 *
 * @author itachi
 */
@Component
public class ServiceRepositoryPopulator implements ApplicationListener<ContextRefreshedEvent>, ApplicationContextAware {
    private final Jackson2ResourceReader resourceReader;
    private final Resource sourceData;

    private ApplicationContext applicationContext;

    public ServiceRepositoryPopulator() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        resourceReader = new Jackson2ResourceReader(mapper);
        sourceData = new ClassPathResource("services.json");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().equals(applicationContext)) {
            CrudRepository serviceRepository =
                    BeanFactoryUtils.beanOfTypeIncludingAncestors(applicationContext, CrudRepository.class);

            if (serviceRepository != null && serviceRepository.count() == 0) {
                populate(serviceRepository);
            }
        }

    }

    @SuppressWarnings("unchecked")
    public void populate(CrudRepository repository) {
        Object entity = getEntityFromResource(sourceData);

        if (entity instanceof Collection) {
            for (Service service : (Collection<Service>) entity) {
                if (service != null) {
                    repository.save(service);
                }
            }
        } else {
            repository.save(entity);
        }
    }

    private Object getEntityFromResource(Resource resource) {
        try {
            return resourceReader.readFrom(resource, this.getClass().getClassLoader());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
