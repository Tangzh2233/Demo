package com.edu.util.httpInvoke;

import com.edu.common.result.ResultData;
import com.edu.dao.domain.User;

import javax.annotation.Resource;

/**
 * @author tangzh
 * @version 1.0
 * @date 2020/1/6 7:22 PM
 **/
public class HttpInvokeTest {

    @Resource
    private TradeApi tradeApi;

    public static void main(String[] args) {
        HttpInvokeTest httpInvokeTest = new HttpInvokeTest();
        httpInvokeTest.invoke();
    }

    public void invoke (){
        User params = new User();
        params.setUsername("tangzh");
        params.setPassword("1234");
        params.setId(1001);
        ResultData<User> resultData = tradeApi.invoke(tradeApi.getUrl("methodA"), params, User.class);
        System.out.println(resultData);
    }
}
