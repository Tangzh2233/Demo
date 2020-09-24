package com.edu.service;

import com.edu.common.result.ResultData;
import com.edu.dao.domain.Dlog;
import com.edu.dao.domain.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by Administrator on 2017/9/14.
 */
public interface ILoginService {
    /**
     * login
     * @param name
     * @param pwd
     * @param request
     * @param response
     * @return
     */
    ResultData userLogin(String name, String pwd, HttpServletRequest request, HttpServletResponse response);

    /**
     * register
     * @param user
     * @return
     */
    ResultData userRegister(User user);

    /**
     * loginOut
     * @param request
     * @param response
     * @param token
     * @return
     */
    ResultData loginOut(HttpServletRequest request,HttpServletResponse response,String token);

    /**
     * check
     * @param token
     * @return
     */
    ResultData checkToken(String token);
}
