package com.edu.controller.login;

import com.edu.util.annotation.PermissionLimit;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Tangzhihao
 * @date 2018/5/17
 */
@Controller
public class PageController {

    @RequestMapping("/login.html")
    @PermissionLimit(limit = false)
    public String gologin(){
        return "login";
    }

    @RequestMapping("/register.html")
    @PermissionLimit(limit = false)
    public String goregister(){
        return "register";
    }

    @RequestMapping("/main.html")
    public String gomain(){
        return "main";
    }
}
