package org.itachi.api.gateway.manage.controller;

import org.apache.commons.lang3.StringUtils;
import org.itachi.api.gateway.manage.domain.Pager;
import org.itachi.api.gateway.manage.domain.Service;
import org.itachi.api.gateway.manage.repositories.jpa.JpaServiceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by itachi on 2018/1/15.
 * User: itachi
 * Date: 2018/1/15
 * Time: 15:50
 *
 * @author itachi
 */
@RestController
@RequestMapping("/api/admin/services")
@Validated
public class AdminServiceApiController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(AdminServiceApiController.class);
    private JpaServiceRepository repository;

    @Autowired
    public AdminServiceApiController(JpaServiceRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public Map<String, Object> services(@Valid @RequestParam("appName") String appName,
                                        @RequestParam("page")Integer page,
                                        @RequestParam("rows") Integer rows) throws Exception {
        Map<String, Object> map = new HashMap<>(16);
        Pager pager = buildPager(page, rows);
        if (StringUtils.isBlank(appName)) {
            pager.setTotal((int) repository.count());
            map.put("services", repository.findAll(new PageRequest(page - 1, rows)).getContent());
        } else {
            pager.setTotal(repository.countByAppName(appName));
            Page<Service> servicePage = repository.findByAppName(appName, new PageRequest(page - 1, rows));
            map.put("services", servicePage.getContent());
        }
        map.put("pager", pager);

        return map;
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        logger.info("Deleting service " + id);
        repository.delete(id);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@RequestBody List<Long> ids) {
        repository.deleteByIdIn(ids);
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody @Valid Service service) throws Exception {
        logger.info("Updating album " + service.getId());
        repository.save(service);
    }

    @GetMapping("/{id}")
    public Service service(@PathVariable("id") Long id) throws Exception {
        logger.info("Getting service " + id);
        return repository.findOne(id);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void modify(@RequestBody @Valid Service service) throws Exception {
        logger.info("Adding service " + service.getId());
        repository.save(service);
    }
}
