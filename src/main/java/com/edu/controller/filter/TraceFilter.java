package com.edu.controller.filter;

import com.alibaba.dubbo.rpc.*;
import com.alibaba.fastjson.JSON;
import com.edu.common.Constants;
import com.edu.common.UUIDUtil;
import com.edu.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.context.annotation.Configuration;

/**
 * @author Tangzhihao
 * @date 2018/1/19
 */
@Configuration
public class TraceFilter implements Filter{

    private static final Logger logger = LoggerFactory.getLogger(TraceFilter.class);

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        String interfaceName = invoker.getInterface().getName();
        String methodName = invocation.getMethodName();
        String reqUrl = invoker.getUrl().getIp()+ ":" + invoker.getUrl().getHost() + " " +interfaceName+methodName;
        String traceId = MDC.get(Constants.TRACE_ID);
        if(StringUtils.isBlank(traceId)){
            traceId = RpcContext.getContext().getAttachment(Constants.TRACE_ID);
        }
        if(StringUtils.isBlank(traceId)){
            traceId = UUIDUtil.getUUID();
        }
        long reqTime = System.currentTimeMillis();
        //获取请求参数
        Object[] ojs = invocation.getArguments();
        logger.info("发起时间[{}] traceId[{}] reqUrl[{}] 请求报文[{}]", DateUtil.getCurDateForHour(),traceId,reqUrl,JSON.toJSONString(ojs));
        //设置traceId
        RpcContext.getContext().setAttachment(Constants.TRACE_ID,traceId);
        Result result = null;
        try {
            result = invoker.invoke(invocation);
            String rspTime = DateUtil.getCurDateForHour();
            logger.info("返回时间[{}] traceId[{}] rspUrl[{}] 返回报文[{}] 耗时[{}]",rspTime,traceId,reqUrl,JSON.toJSONString(result.getValue()),System.currentTimeMillis()-reqTime);
        }catch (Exception e){
            logger.error("dubbo调用异常{}",e);
        }

        return result;
    }
}
