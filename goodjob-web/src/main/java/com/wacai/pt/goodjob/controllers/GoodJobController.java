package com.wacai.pt.goodjob.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by xuanwu on 16/4/1.
 */
@RestController
public class GoodJobController {

    @RequestMapping(value = "/check_backend_active.html")
    public void checkBackendActive(HttpServletResponse response) {
        response.setStatus(200);
    }

//    @RequestMapping(value = "/login.html")
//    public User login() {
//        User user = (User) SecurityUtils.getSubject().getSession().getAttribute("user");
//        user.setPassword(null);
//
//        return user;
//    }

    @RequestMapping(value = "/logout.html")
    public void logout() {
//        SecurityUtils.getSubject().logout();
//        UserUtil.setUser(null);
    }
}
