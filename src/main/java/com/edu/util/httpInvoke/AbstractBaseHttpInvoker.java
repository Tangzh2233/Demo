package com.edu.util.httpInvoke;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.edu.common.result.ResultData;
import com.edu.dao.domain.User;
import com.edu.util.DateUtil;
import com.edu.util.HttpClientUtil2;
import com.google.gson.JsonObject;
import com.rrx.util.Md5Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.TreeMap;

/**
 * @author tangzh
 * @version 1.0
 * @date 2020/1/6 6:47 PM
 * invoke 公共方法
 **/
public abstract class AbstractBaseHttpInvoker implements BaseHttpInvoker{

    protected final static Logger log = LoggerFactory.getLogger(AbstractBaseHttpInvoker.class);

    /**
     * 通用加签
     * @param params
     * @return
     */
    private String getSign(TreeMap<String,String> params){
        params.put("ts", DateUtil.getCurDateForHour());
        return Md5Util.md5(params,getKey());
    }

    protected <T> ResultData<T> invoke(String url,Object request,Class<T> respClass){
        TreeMap<String, String> params = getMapData(request);
        params.put("sign",getSign(parseCommonMapData(params)));
        return doInvoke(url,params,respClass);
    }

    /**
     * 系统级别额外公共参数设置
     * @param params
     * @return
     */
    protected TreeMap<String,String> parseCommonMapData(TreeMap<String,String> params){
        return params;
    }

    @Override
    public TreeMap<String, String> getMapData(Object request) {
        JSONObject parseObject = JSON.parseObject(JSON.toJSONString(request));
        TreeMap<String, String> params = new TreeMap<>();
        parseObject.forEach((key, value) -> params.put(key, String.valueOf(value)));
        return params;
    }

    /**
     * execute invoke
     * @param url
     * @param params
     * @return
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> ResultData<T> doInvoke(String url, TreeMap<String, String> params, Class<T> respClass) {
        ResultData<T> resultData;
        try {
            String result = HttpClientUtil2.sendPostRequest(url, params);
            resultData = JSON.parseObject(result, ResultData.class);
            resultData.setData(JSONObject.toJavaObject((JSONObject) resultData.getData(), respClass));
        } catch (Exception e) {
            log.error("Http Invoke Exception url={},params={}", url, JSON.toJSONString(params), e);
            throw new IllegalArgumentException("http invoke fail");
        }
        return resultData;
    }

}
