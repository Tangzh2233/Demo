package com.edu.util.emailSendUtil;

import org.springframework.mail.javamail.JavaMailSender;

import javax.annotation.Resource;

/**
 * @Author: tangzh
 * @Date: 2018/8/22$ 上午10:57$
 **/
public class SimpleEmailSendUtil {


    @Resource(name = "mailSender")
    private JavaMailSender javaMailSender;

    public void sendMail(String addr,String msg){

    }
}
