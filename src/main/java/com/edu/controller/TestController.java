package com.edu.controller;

import com.edu.common.httpresp.HttpResponse;
import com.edu.dao.domain.Dlog;
import com.edu.dao.domain.User;
import com.edu.service.ILoginService;
import com.edu.service.quartz.DailyJob;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Random;
import java.util.UUID;

/**
 * @author Tangzhihao
 * @date 2018/3/13
 */
@Controller
@RequestMapping("/myspringboot")
public class TestController {

    @Resource
    private ILoginService loginService;
    @Resource
    private DailyJob dailyJob;
    public String userId;


    @RequestMapping("/addDlog.html")
    public String showTest(){
        return "test";
    }

    @RequestMapping("/addDlog.do")
    public String addDlog(Dlog dlog){
        int i = new Random().nextInt(10);
        dlog.setId(String.valueOf(i));
        System.out.println(dlog.toString());
        loginService.addDlog(dlog);
        return "main";
    }

    @ResponseBody
    @RequestMapping("/httpPost.do")
    public HttpResponse httpTest(User user){
        HttpResponse resp = new HttpResponse();
        HttpResponse.HttpRespData data = new HttpResponse.HttpRespData();
        HttpResponse.HttpRespError error = new HttpResponse.HttpRespError();
        if("tang".equals(user.getUsername())&&"123".equals(user.getPassword())){
            error.setReturnCode("00");
            error.setReturnMessage("SUCCESS");
        }else {
            error.setReturnCode("11");
            error.setReturnMessage("FAIL");
        }
        resp.setData(data);
        resp.setError(error);
        return resp;
    }

    @RequestMapping("/sendEmail.do")
    private String sendEmail(){
        dailyJob.execute();
        return "main";
    }
}
