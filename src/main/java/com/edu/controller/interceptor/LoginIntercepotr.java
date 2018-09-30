package com.edu.controller.interceptor;

import com.edu.common.Constants;
import com.edu.common.result.ResultData;
import com.edu.dao.domain.User;
import com.edu.service.ILoginService;
import com.edu.util.CookieUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author: Tangzhihao
 * @date: 2018/5/24 14:19 //拦截器中注入bean为null的解决方法
 * @description:https://blog.csdn.net/qq_33206732/article/details/78422157
 */
public class LoginIntercepotr implements HandlerInterceptor {

    private final static Logger logger = LoggerFactory.getLogger(LoginIntercepotr.class);
    @Resource
    private ILoginService loginService;
    @Value("${redis_key_expire_time}")
    private Integer key_expire_time;
    @Value("${redis_session_key}")
    private String user_session_key;


    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        System.out.println("进入loginInterceptor");
        StringBuffer url = request.getRequestURL();
        System.out.println("In Intercepor-+-+-+-"+url.toString());
        String token = CookieUtils.getCookieValue(request, "USER_TOKEN");
        if(StringUtils.isBlank(token)){
            response.sendRedirect("login.html");
            return false;
        }
        ResultData result = loginService.checkToken(token);
        if(!Constants.SUCCESS_CODE.equals(result.getRspCode())){
            response.sendRedirect("login.html");
            return false;
        }
        User user = (User) result.getData();
        if(user==null) {
            logger.info("非法登录!");
            response.sendRedirect("login.html");
            return false;
        }
        return true;
    }

}
