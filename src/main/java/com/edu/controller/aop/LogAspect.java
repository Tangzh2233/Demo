package com.edu.controller.aop;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.dianping.cat.Cat;
import com.dianping.cat.message.Event;
import com.dianping.cat.message.Message;
import com.dianping.cat.message.Transaction;
import com.edu.common.Constants;
import com.edu.common.result.ResultData;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

/**
 * @Author: tangzh
 * @Date: 2018/10/30$ 下午3:20$
 * 数据toJSONString()+PropertyPreFilter的方式。不能直接使用JSON.toJSONString().
 * 否则或报错"It is illegal to call this method if the current request is not in asynchronous mode"
 **/
@Component
@Aspect
public class LogAspect {

    private final static Logger logger = LoggerFactory.getLogger(LogAspect.class);

    private LogJsonFilter jsonFilter;

    public LogAspect() {
        String[] nName = {"inputStream", "response", "request", "req", "resp"};
        this.jsonFilter = new LogJsonFilter(nName);
    }

    @Around("execution(* com.edu.controller.login.*.*(..))")
    public Object doAround(ProceedingJoinPoint point) throws Throwable {
        Class<?> target = point.getTarget().getClass();
        String methodName = point.getSignature().getName();
        String fullName = target.getSimpleName() + "." + methodName;
        Object[] args = point.getArgs();
        logger.info("请求方法:{} 请求参数:{}", fullName, toJSONString(args));
        Object result = null;
        Transaction transaction = Cat.newTransaction("Http服务", fullName);
        try {
            result = point.proceed(args);
            this.sendCatMsg(transaction, fullName, result, null);
        } catch (Exception e) {
            this.sendCatMsg(transaction, fullName, result, e);
            logger.error("请求方法:{} 异常 请求参数{}", fullName, toJSONString(args), e);
            return ResultData.defaultFail("服务异常");
        }
        logger.info("返回方法:{} 返回结果:{}", fullName, toJSONString(result));
        return result;
    }

    private String toJSONString(Object args) {
        return JSON.toJSONString(args, this.jsonFilter, SerializerFeature.WriteMapNullValue);
    }

    public void sendCatMsg(Transaction transaction, String fullName, Object object, Exception e) {
        String status = Message.SUCCESS, value = "TraceId: " + MDC.get(Constants.TRACE_ID);
        try {
            if (e == null && object instanceof ResultData) {
                ResultData result = (ResultData) object;
                if (!ResultData.checkSuccess(result)) {
                    status = "Fail";
                }
                Cat.logMetricForCount("登录成功次数");
                Cat.logEvent(fullName, result.getRspMessage(), status, value);
            } else if (e != null) {
                status = "Fail";
                Cat.logError(e);
                Cat.logMetricForCount("登录失败次数");
                Cat.logEvent(fullName, "服务异常", status, value);
            } else {
                Cat.logEvent(fullName, "处理成功", status, value);
            }
        } finally {
            transaction.setStatus(status);
            transaction.complete();
        }
    }

    public static void main(String[] args) {
        String a = "[CAT Transaction告警: SpringBootDemo Http服务 cat-home] : [实际值:6 ] [最大阈值: 1 ][告警时间:2019-07-03 11:07:01]\n" +
                "[时间: 2019-07-03 11:07] \n" +
                "\n" +
                "\n" +
                "[告警间隔时间]1分钟";
        a = a.replace("\n","");
        String[] split = a.split("\\[");
        for(String str : split){
            System.out.println(str);
        }
        //System.out.println(split[0].replace("[","")+"-"+split[1].replace("]",""));
    }
}
