package org.itachi.api.gateway.manage.controller;

import org.itachi.api.gateway.manage.repositories.jpa.JpaUserRepository;
import org.itachi.api.gateway.manage.util.Constants;
import org.itachi.api.gateway.manage.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by itachi on 2018/1/16.
 * User: itachi
 * Date: 2018/1/16
 * Time: 16:34
 *
 * @author itachi
 */
@Controller
@RequestMapping("/admin")
@Validated
public class AdminLoginController extends BaseController {

    @Autowired
    private JpaUserRepository repository;

    @PostMapping("/login")
    @ResponseBody
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void toLogin(@Valid @Size(min = 1, max = 15, message = "{account.invalid}") @RequestParam("account") String account,
                        @Size(min = 6, max = 16, message = "{password.invalid}") @RequestParam("password") String password) throws Exception {
        request.getSession(true).setAttribute(Constants.SESSION_KEY, repository.findByUserNameAndPassword(account, password));
    }

    /**
     * 进入登录页面
     */
    @GetMapping("/login")
    public String login(Model model) throws Exception {
        String host;
        try {
            host = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            host = "unknow";
        }
        model.addAttribute("host", host);
        return "admin/login";
    }

    /**
     * 退出登陆
     */
    @GetMapping("/logout")
    public String loginOut() throws Exception {
        Utils.cleanSessions(request);
        return "redirect:/admin/login";
    }
}
