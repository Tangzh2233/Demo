package com.edu.controller.login;

import com.edu.controller.common.DemoResult;
import com.edu.service.ILoginService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @author Tangzhihao
 * @date 2018/5/24
 */
@Controller
@RequestMapping("/check")
public class TokenController {

    @Resource
    private ILoginService loginService;

    @ResponseBody
    @RequestMapping("/token")
    public Object checkToken(String token,String callback){
        DemoResult result = new DemoResult();
        try {
            return loginService.checkToken(token);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //判断是否为jsonp调用
        if(StringUtils.isNotBlank(callback)){
            MappingJacksonValue value = new MappingJacksonValue(callback);
            value.setJsonpFunction(callback);
            return value;
        }else{
            return result;
        }
    }

}
