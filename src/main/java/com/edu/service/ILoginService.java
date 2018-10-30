package com.edu.service;

import com.edu.common.result.ResultData;
import com.edu.dao.domain.Dlog;
import com.edu.dao.domain.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Administrator on 2017/9/14.
 */
public interface ILoginService {
    User login(String name,double amout);
    ResultData userLogin(String name, String pwd, HttpServletRequest request, HttpServletResponse response);
    ResultData userRegister(User user);
    ResultData loginOut(HttpServletRequest request,HttpServletResponse response,String token);
    <T> T select(T t);
    int addDlog(Dlog dlog);
    ResultData checkToken(String token);
}
