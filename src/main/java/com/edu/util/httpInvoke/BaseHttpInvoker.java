package com.edu.util.httpInvoke;

import com.edu.common.result.ResultData;

import java.util.TreeMap;

/**
 * @author tangzh
 * @version 1.0
 * @date 2020/1/6 5:53 PM
 **/
public interface BaseHttpInvoker {

    /**
     * 系统级别基础URL
     * @return
     */
    String getBaseUrl();

    /**
     * 方法级别URL
     * @param methodName
     * @return
     */
    String getUrl(String methodName);

    /**
     * 系统级别KEY
     * @return
     */
    String getKey();

    /**
     * Object->TreeMap请求参数转换
     * @param request
     * @return
     */
    TreeMap<String,String> getMapData(Object request);

//    Class<? extends ResultData> getParseClass();

    <T> ResultData doInvoke(String url,TreeMap<String,String> params,Class<T> resp);
}
