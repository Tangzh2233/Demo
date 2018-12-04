package com.edu.config;

import com.edu.controller.interceptor.LoginIntercepotr;
import com.edu.controller.interceptor.TraceInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author Tangzhihao
 * @date 2017/10/11
 */
@Configuration
public class ConfigAdapter extends WebMvcConfigurerAdapter{


    @Bean
    LoginIntercepotr loginIntercepotr(){
        return new LoginIntercepotr();
    }
    @Bean
    TraceInterceptor traceInterceptor(){
        return new TraceInterceptor();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**")
                .addResourceLocations("classpath:/resources/");
        super.addResourceHandlers(registry);
     }

//    先执行LoginInterceptor再执行TraceInterceptor
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(traceInterceptor()).addPathPatterns("/**")
                .excludePathPatterns("/page/**","/error","/myspringboot/exit.do","/myspringboot/addUsers.do");
        registry.addInterceptor(loginIntercepotr()).addPathPatterns("/**")
                .excludePathPatterns("/page/login.html","/myspringboot/login.do","/error","/myspringboot/httpPost.do","/page/register.html","/myspringboot/register.do","/myspringboot/addUsers.do");
        super.addInterceptors(registry);
    }

    /*cat集成*//*
    @Bean
    public FilterRegistrationBean catFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        CatFilter filter = new CatFilter();
        registration.setFilter(filter);
        registration.addUrlPatterns("*//*");
        registration.setName("cat-filter");
        registration.setOrder(1);
        return registration;
    }*/

}
