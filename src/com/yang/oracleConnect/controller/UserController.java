package com.yang.oracleConnect.controller;

import com.yang.oracleConnect.entity.User;
import com.yang.oracleConnect.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Yangjing
 */
@Controller
public class UserController {

    @Autowired
    private IUserService userService;

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public String login(HttpServletRequest request, HttpServletResponse response) {
        /*String path = request.getContextPath();
        String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
        System.out.println(path);
        System.out.println(basePath);*/
        User user = userService.login(request.getParameter("username"), request.getParameter("password"));

        if (user != null) {
            return "success";
        } else {
            return "fail";
        }
    }

}
