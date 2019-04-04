package com.edu.JavaLearning.设计模式.InterfaceLearn;

import javax.servlet.http.Cookie;

/**
 * @Author: tangzh
 * @Date: 2018/11/1$ 上午10:10$
 **/
public class Main{

    private interface Proxy{
        void proxy(Cookie cookie);
    }

    private static void addCookie(String response,Proxy proxy){
        Cookie cookie = new Cookie("22", "sss");
        proxy.proxy(cookie);
    }

    public static void main(String[] args) {
        Interface a = new MainA();
        Interface b = new MainB();
        addCookie("derderder", new Proxy() {
            @Override
            public void proxy(Cookie cookie) {
                cookie.setDomain("");
            }
        });
    }

}
class MainA extends AbstractA{

    @Override
    public void methodA1() {

    }

    @Override
    public void methodA2() {

    }
}
class MainB extends AbstractB{

    @Override
    public void methodB1() {

    }

    @Override
    public void methodB2() {

    }
}
