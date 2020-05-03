package com.edu.JavaLearning.tomcat;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author tangzh
 * @version 1.0
 * @date 2019/12/9 4:03 PM
 **/
@WebServlet("/firstServletService")
public class TomcatServletService extends HttpServlet {
    private static final long serialVersionUID = -5453986785950889263L;


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response){
        //todo
    }
}
