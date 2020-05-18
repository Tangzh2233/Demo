package com.edu.controller.login;

import com.alibaba.fastjson.JSON;
import com.edu.common.result.ResultData;
import com.edu.dao.domain.User;

import com.edu.service.ILoginService;
import com.edu.util.IpUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.exceptions.JedisConnectionException;

import javax.servlet.http.HttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.locks.LockSupport;

/**
 * @author: Tangzhihao
 * @date: 2017/11/17 14:30
 * @description: 登录逻辑处理类
 */
@RestController
@RequestMapping("/myspringboot")
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    private static ConcurrentHashMap<String,String> dataMap = new ConcurrentHashMap<>();
    private static ConcurrentMap<String,Thread> holdRequest = new ConcurrentHashMap<>();
    private static ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(4);

    @Resource
    private ILoginService userService;

    static {
        scheduledExecutorService.scheduleWithFixedDelay(new CheckHoldRequestTask(), 1000, 1000, TimeUnit.MILLISECONDS);
    }

    static class CheckHoldRequestTask implements Runnable{

        @Override
        public void run() {
            for (String key : holdRequest.keySet()) {
                String value = dataMap.get(key);
                if (StringUtils.isNotBlank(value)) {
                    LockSupport.unpark(holdRequest.get(key));
                }
            }
        }
    }

    @RequestMapping("/getFlag")
    public void getFlag(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String chatId = request.getParameter("chatId");
        if (StringUtils.isBlank(dataMap.get(chatId))) {
            holdRequest.put(chatId, Thread.currentThread());
            LockSupport.parkNanos(10 * 1000 * 1000 * 1000L);
        }
        if (StringUtils.isNotBlank(dataMap.get(chatId))) {
            response.getWriter().write("1");
        } else {
            response.getWriter().write("0");
        }
    }


    /**
     * @author: Tangzhihao
     * @date: 2017/11/17 14:30
     * @params: HttpServerletRequest, User
     * @return: String
     * @description:
     */
    @RequestMapping(value = "/login.do", method = RequestMethod.POST)
    public ResultData login(User usr, HttpServletRequest request, HttpServletResponse response) throws Exception, JedisConnectionException {
//        String string = JSON.toJSONString(user.toString());
//        JSONObject jsonObject = JSONObject.parseObject(string);
//        User usr = JSON.toJavaObject(jsonObject, User.class);
//        logger.info("请求参数: id={},name={}",new Object[]{usr.getId(),usr.getUsername()});
//        logger.error("error级别日志测试");
//        System.out.println(usr.getUsername()+usr.getPassword());
//        System.out.println("========"+request.getParameter("username"));
//        System.out.println("====IP2==="+IpUtil.getRequestClientIp(request));
        System.out.println("===LocalIp===" + IpUtil.getLocalIpV4Addr());
        return userService.userLogin(usr.getUsername(), usr.getPassword(), request, response);
//            return userService.userLogin(usr.getUsername(),usr.getPassword(),request,response);
    }

    @RequestMapping(value = "/register.do", method = RequestMethod.POST)
    public ResultData regisger(User usr) throws Exception {
        logger.info("请求参数: " + usr.toString());
        try {
            return userService.userRegister(usr);
        } catch (Exception e) {
            logger.error("service 异常", e);
            return ResultData.defaultFail();

        }
    }

    @RequestMapping(value = "/exit.do", method = RequestMethod.POST)
    public ResultData exit(HttpServletRequest request, HttpServletResponse response, String token) {
        System.out.println("Page_Token" + token);
        try {
            return userService.loginOut(request, response, token);
        } catch (Exception e) {
            logger.error("service 异常", e);
            return ResultData.defaultFail();
        }
    }


}
