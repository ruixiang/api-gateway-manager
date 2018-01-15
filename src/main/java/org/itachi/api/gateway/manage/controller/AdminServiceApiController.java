package org.itachi.api.gateway.manage.controller;

import org.itachi.api.gateway.manage.domain.Pager;
import org.itachi.api.gateway.manage.domain.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
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
    private CrudRepository<Service, String> repository;

    @Autowired
    public AdminServiceApiController(CrudRepository<Service, String> repository) {
        this.repository = repository;
    }

    @GetMapping
    public Map<String, Object> services(@Valid @RequestParam("appName") String appName,
                                        @RequestParam("page")Integer page,
                                        @RequestParam("rows") Integer rows) throws Exception {
        Map<String, Object> map = new HashMap<>(16);
        Pager pager = buildPager(page, rows);
        pager.setTotal((int) repository.count());
        map.put("pager", pager);
        map.put("services", repository.findAll());
        return map;
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable String id) {
        logger.info("Deleting service " + id);
        repository.delete(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody @Valid Service service) throws Exception {
        logger.info("Updating album " + service.getId());
        repository.save(service);
    }

    @GetMapping("/{id}")
    public Service service(@PathVariable("id") String id) throws Exception {
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
