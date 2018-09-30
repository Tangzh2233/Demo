package com.edu.controller.interceptor;

import com.edu.common.Constants;
import com.edu.common.UUIDUtil;
import com.edu.util.DateUtil;
import org.slf4j.MDC;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: tangzh
 * @Date: 2018/9/30$ 上午10:25$
 **/
public class TraceInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        System.out.println("进入traceInterceptor");
        MDC.put(Constants.REQ_TIME, DateUtil.getCurDateForHour());
        MDC.put(Constants.REQ_URL,request.getRequestURI());
        MDC.put(Constants.TRACE_ID, UUIDUtil.getUUID());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        MDC.remove(Constants.REQ_TIME);
        MDC.remove(Constants.REQ_URL);
        MDC.remove(Constants.TRACE_ID);
    }
}
