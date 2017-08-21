package com.yang.oracleConnect;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author Yangjing
 */
@Controller
public class StringController {

    @RequestMapping(value = "/string", method = {RequestMethod.GET, RequestMethod.POST})
    public String getLogin() {
        return "login";
    }
}
