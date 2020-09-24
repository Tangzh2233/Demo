package com.edu.config;

import com.edu.dao.domain.User;

/**
 * @author Tangzh
 * @version 1.0.0
 * @date 2020/9/20 20:53
 * @description
 **/
public class DemoContextHolder {

    private final static ThreadLocal<User> dataMap = new ThreadLocal<>();

    public static void setData(User user) {
        dataMap.set(user);
    }

    public static User getData() {
        return dataMap.get();
    }

    public static void clear(){
        dataMap.remove();
    }
}
