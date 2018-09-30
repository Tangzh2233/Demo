package com.edu.controller.filter;

import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcException;
import com.dianping.cat.Cat;
import com.dianping.cat.message.Message;
import com.jiupai.cornerstone.monitor.cat.dubbo.CatTransactionFilter;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Tangzhihao
 * @date 2018/4/27
 */
@Activate(
        group = {"provider", "consumer"},
        order = -9000
)
public class MyCatFilter extends CatTransactionFilter{

    //@Override
    public void logBizEventAndMetric(Invoker<?> invoker,Invocation invocation) {
        String methodName = invocation.getMethodName();
        Cat.getProducer().newTransaction("DemoMetric",invocation.getMethodName());
        if("ManhatanApi.reCharge".equals(methodName)){
            double amount = 0.0;
            Cat.logEvent("","");
            Cat.logMetricForCount(invocation.getAttachment("amount"));
            Cat.logMetricForDuration(invocation.getAttachment("amount"), 1L);
            Cat.logMetricForSum(invocation.getAttachment("amount"),amount);
        }else if("ManhatanApi.daiKou".equals(methodName)){

        }
    }
}
