package com.edu.JavaLearning.aop.aop.springaop;

import java.sql.SQLException;

/**
 * @author Tangzhihao
 * @date 2018/5/4
 */

public interface Waiter {
    void greeto(String name);
    String serverto(String name);
    void throwException() throws SQLException;
}

class NativeWaiter implements Waiter{

    @Override
    public void greeto(String name) {
        System.out.println("greeto"+name);
    }

    @Override
    public String serverto(String name) {
        System.out.println("server"+name);
        return name;
    }

    @Override
    public void throwException() throws SQLException {
        throw new SQLException();
    }
}
