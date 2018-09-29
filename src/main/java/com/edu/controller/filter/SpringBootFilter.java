package com.edu.controller.filter;

import com.edu.dao.domain.User;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Created by Administrator on 2017/9/20.
 */
/*@Order(1)
@WebFilter*/
public class SpringBootFilter implements Filter {
    public final static String LOGIN_JSP = "http://127.0.0.1:8848/myspringboot/login.html";
    public final static String LOGIN_DO = "http://127.0.0.1:8848/myspringboot/login.do";
    public final static String RESOURCES = "http://127.0.0.1:8848/myspringboot/resources/**";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request =(HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;

        User user = (User) request.getSession().getAttribute("user");
        String url = request.getRequestURL().toString();

        System.out.println(url+"222222"+user);
        if(LOGIN_JSP.equals(url) || LOGIN_DO.equals(url) ||RESOURCES.equals(url) || user!=null){
            System.out.println(user+"3333");
            filterChain.doFilter(request,response);
        }else{
            response.sendRedirect("login.html");
        }
    }
    @Override
    public void destroy() {

    }
}
