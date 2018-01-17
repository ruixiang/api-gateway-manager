package org.itachi.api.gateway.manage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by itachi on 2018/1/17.
 * User: itachi
 * Date: 2018/1/17
 * Time: 17:24
 *
 * @author itachi
 */
@Controller
@RequestMapping("/")
public class ViewController {
    @GetMapping
    public String loginOut() throws Exception {
        return "redirect:/admin";
    }
}
