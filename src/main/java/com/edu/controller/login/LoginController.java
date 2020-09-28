package com.edu.controller.login;

import com.edu.common.result.ResultData;
import com.edu.dao.domain.User;
import com.edu.service.ILoginService;
import com.edu.util.IpUtil;
import com.edu.util.annotation.PermissionLimit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.exceptions.JedisConnectionException;
import javax.servlet.http.HttpServletRequest;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * @author: Tangzhihao
 * @date: 2017/11/17 14:30
 * @description: 登录逻辑处理类
 */
@RestController
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Resource
    private ILoginService userService;

    @PermissionLimit(limit = false)
    @RequestMapping(value = "/login.do", method = RequestMethod.POST)
    public ResultData login(User usr, HttpServletRequest request, HttpServletResponse response) {
        return userService.userLogin(usr.getUsername(), usr.getPassword(), request, response);
    }

    @PermissionLimit(limit = false)
    @RequestMapping(value = "/register.do", method = RequestMethod.POST)
    public ResultData register(User usr) {
        return userService.userRegister(usr);
    }

    @RequestMapping(value = "/exit.do", method = RequestMethod.POST)
    public ResultData exit(HttpServletRequest request, HttpServletResponse response, String token) {
        return userService.loginOut(request, response, token);
    }


}
