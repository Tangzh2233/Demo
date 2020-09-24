package com.edu.controller.interceptor;

import com.edu.common.Constants;
import com.edu.common.result.ERspCode;
import com.edu.common.result.ResultData;
import com.edu.config.DemoContextHolder;
import com.edu.dao.domain.User;
import com.edu.service.ILoginService;
import com.edu.util.CookieUtils;
import com.edu.util.annotation.PermissionLimit;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
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
@Component
public class LoginInterceptor implements HandlerInterceptor {

    private final static Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);
    @Resource
    private ILoginService loginService;


    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        DemoContextHolder.clear();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        boolean needLogin = true;
        boolean needAdmin = false;
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        PermissionLimit permissionLimit = handlerMethod.getMethodAnnotation(PermissionLimit.class);
        if (permissionLimit != null) {
            needLogin = permissionLimit.limit();
            needAdmin = permissionLimit.admin();
        }

        if (needLogin) {
            String contextPath = request.getContextPath();
            String token = CookieUtils.getCookieValue(request, Constants.USER_TOKEN);
            if (StringUtils.isBlank(token)) {
                response.sendRedirect(contextPath + "/login.html");
                return false;
            }
            ResultData result = loginService.checkToken(token);
            if (!ERspCode.SUCCESS.getCode().equals(result.getRspCode())) {
                response.sendRedirect(contextPath + "/login.html");
                return false;
            }
            User user = (User) result.getData();
            if (user == null) {
                logger.info("非法登录!");
                response.sendRedirect(contextPath + "/login.html");
                return false;
            }
            DemoContextHolder.setData(user);
        }

        if(needAdmin){
            //todo
        }
        return true;
    }

}
