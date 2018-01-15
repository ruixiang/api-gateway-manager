package org.itachi.api.gateway.manage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by itachi on 2017/8/13.
 * User: itachi
 * Date: 2017/8/13
 * Time: 09:58
 * @author itachi
 */
@Controller
@RequestMapping("/admin")
public class AdminViewController extends BaseController {

    @GetMapping("/menu")
    public String menu() throws Exception {
        return "admin/menu";
    }

    @GetMapping
    public String index() throws Exception {
        return "admin/index";
    }

    @GetMapping("/welcome")
    public String welcome() throws Exception {
        return "admin/welcome";
    }

    @GetMapping("/header")
    public String header() throws Exception {
        return "admin/header";
    }

    @GetMapping("/services")
    public String services() throws Exception {
        return "admin/service/list";
    }

    @GetMapping("/service")
    public String service() throws Exception {
        return "admin/service/view";
    }

    @GetMapping("/caches")
    public String caches() throws Exception {
        return "admin/cache/list";
    }
}
