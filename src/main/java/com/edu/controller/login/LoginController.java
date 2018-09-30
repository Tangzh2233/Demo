package com.edu.controller.login;

import com.edu.common.Constants;
import com.edu.common.result.ResultData;
import com.edu.dao.domain.User;

import com.edu.service.ILoginService;
import com.edu.util.IpUtil;
import com.jiupai.cornerstone.monitor.cat.springaop.CatStatictisAnnotaion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.exceptions.JedisConnectionException;

import javax.servlet.http.HttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author: Tangzhihao
 * @date: 2017/11/17 14:30
 * @description: 登录逻辑处理类
 */
@Controller
@RequestMapping("/myspringboot")
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Resource
    private ILoginService userService;


    /**
     * @author: Tangzhihao
     * @date: 2017/11/17 14:30
     * @params: HttpServerletRequest,User
     * @return: String
     * @description:
     */
    @ResponseBody
    @RequestMapping(value = "/login.do",method= RequestMethod.POST)
    @CatStatictisAnnotaion()
    public ResultData login(User usr, HttpServletRequest request, HttpServletResponse response, String username) throws Exception,JedisConnectionException {
        logger.info("请求参数: id={},name={}",new Object[]{usr.getId(),usr.getUsername()});
        logger.error("error级别日志测试");
        System.out.println(usr.getUsername()+usr.getPassword());
        System.out.println("========"+request.getParameter("username"));
        System.out.println("========"+username);
        System.out.println("====IP2==="+IpUtil.getRequestClientIp(request));
        System.out.println("===LocalIp==="+ IpUtil.getLocalIpV4Addr());
//        System.out.println(params.get("username"));
        try {
            return userService.userLogin(usr.getUsername(),usr.getPassword(),request,response);
        }catch (Exception e){
            logger.error("Service Exception",e);
            return ResultData.isFail("01", Constants.SYSEXCEPTION);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/register.do",method = RequestMethod.POST)
    @CatStatictisAnnotaion()
    public ResultData regisger(User usr) throws Exception{
        logger.info("请求参数: "+usr.toString());
        try {
            return userService.userRegister(usr);
        }catch (Exception e){
            logger.error("service 异常",e);
            return ResultData.isFail("01","Service Exception");

        }
    }

    @ResponseBody
    @RequestMapping(value = "/exit.do",method = RequestMethod.POST)
    public ResultData exit(HttpServletRequest request,HttpServletResponse response,String token){
        System.out.println("Page_Token"+token);
        try {
            return userService.loginOut(request,response,token);
        }catch (Exception e){
            logger.error("service 异常",e);
            return ResultData.isFail("01","登出失败");
        }
    }


}
