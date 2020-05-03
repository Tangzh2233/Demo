package com.edu.util.httpInvoke;

import com.edu.common.result.ResultData;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author tangzh
 * @version 1.0
 * @date 2020/1/6 7:11 PM
 * 系统级别invoker
 **/
@Component
public class TradeApi extends AbstractBaseHttpInvoker {

    @Value("trade.base.url")
    private String baseUrl;

    @Value("trade.app.key")
    private String appKey;

    /**
     * 系统级别需要维护的数据
     * key->methodName
     * value->methodUrl
     */
    private static Map<String,String> tradeUrls = new HashMap<>();

    static {
        tradeUrls.put("methodA","urlA");
        tradeUrls.put("methodB","urlB");
        tradeUrls.put("methodC","urlC");
        tradeUrls.put("methodD","urlD");
    }

    @Override
    public TreeMap<String,String> parseCommonMapData(TreeMap<String,String> params){
        //todo SomeThing eg:
        params.put("appKey",appKey);
        return params;
    }

    private String getMethodUrl(String methodName){
        String uri = tradeUrls.get(methodName);
        if(StringUtils.isBlank(uri)){
           throw new IllegalArgumentException(methodName + " 无对应URL");
        }
        return uri;
    }

    @Override
    public String getBaseUrl() {
        return baseUrl;
    }

    @Override
    public String getUrl(String methodName) {
        return getBaseUrl() + getMethodUrl(methodName);
    }

    @Override
    public String getKey() {
        return appKey;
    }

//    @Override
//    public Class<? extends ResultData> getParseClass() {
//        return ResultData.class;
//    }
}
