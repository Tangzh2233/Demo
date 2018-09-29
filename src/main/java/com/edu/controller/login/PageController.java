package com.edu.controller.login;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Tangzhihao
 * @date 2018/5/17
 */
@Controller
@RequestMapping("/page")
public class PageController {

    @RequestMapping("/login.html")
    public String gologin(){
        return "login";
    }

    @RequestMapping("/register.html")
    public String goregister(){
        return "register";
    }

    @RequestMapping("/main.html")
    public String gomain(){
        return "main";
    }
}
