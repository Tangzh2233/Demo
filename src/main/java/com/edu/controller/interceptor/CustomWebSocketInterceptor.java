package com.edu.controller.interceptor;

import com.edu.common.Constants;
import com.edu.common.result.ResultData;
import com.edu.config.DemoContextHolder;
import com.edu.dao.domain.User;
import com.edu.service.ILoginService;
import com.edu.service.impl.LoginServiceImpl;
import com.edu.util.CookieUtils;
import org.apache.commons.lang3.StringUtils;
import org.omg.CORBA.DATA_CONVERSION;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @author Tangzh
 * @version 1.0.0
 * @date 2020/9/19 21:13
 * @description
 **/
@Component
public class CustomWebSocketInterceptor extends HttpSessionHandshakeInterceptor {

    @Resource
    private ILoginService iLoginService;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        System.out.println("Before Handshake");
        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
            Cookie[] cookies = servletRequest.getServletRequest().getCookies();
            String cookieValue = CookieUtils.getCookieValue(cookies, Constants.USER_TOKEN, false);
            if (StringUtils.isBlank(cookieValue)) {
                return false;
            }
            ResultData data = iLoginService.checkToken(cookieValue);
            if (data.isSuccess()) {
                User user = (User)data.getData();
                attributes.put("USER_ID",user.getUserNo());
                servletRequest.getServletRequest().getSession().setAttribute("USER_ID",user.getUserNo());
            }
        }
        return super.beforeHandshake(request, response, wsHandler, attributes);
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception ex) {
        super.afterHandshake(request, response, wsHandler, ex);
    }
}
