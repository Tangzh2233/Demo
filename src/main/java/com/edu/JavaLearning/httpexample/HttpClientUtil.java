package com.edu.JavaLearning.httpexample;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

/**
 * @author Tangzhihao
 * @date 2017/12/20
 */

public class HttpClientUtil {

    private static Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);
    private static final int READ_TIMEOUT = 5000;
    private static final int CONNECT_TIMEOUT = 3000;

    public static String doPost(String url, Map<String,String> params){
        return doPost(url,mapToNameValuePair(params));
    }

    private static String doPost(String url,List<NameValuePair> pairs){
        //新建http客户端
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //创建post连接
        HttpPost post = new HttpPost(url);
        CloseableHttpResponse response = null;
        //设置参数编码格式
        UrlEncodedFormEntity urlEntity;
        try {
            //配置post
            configTimeOut(post);
            urlEntity = new UrlEncodedFormEntity(pairs,"UTF-8");
            post.setEntity(urlEntity);
            try {
                response = httpClient.execute(post);
                HttpEntity entity = response.getEntity();
                if(entity!=null){
                    return EntityUtils.toString(entity,"UTF-8");
                }
            } finally {
                if(response!=null){
                    try {
                        response.close();
                    } catch (IOException e) {
                    }
                }
            }
        } catch (Exception e) {
            logger.error("http发送异常",e);
        }finally {
            if(httpClient!=null){
                try {
                    httpClient.close();
                } catch (IOException e) {
                }
            }
        }
        return null;
    }

    /**
     * @author: Tangzhihao
     * @date: 2017/12/20 15:38
     * @description:配置HttpPost
     */
    private static void configTimeOut(HttpPost httpPost){
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(CONNECT_TIMEOUT).setSocketTimeout(READ_TIMEOUT).build();
        httpPost.setConfig(requestConfig);
    }

    /**
     * @author: Tangzhihao
     * @date: 2017/12/20 15:39
     * @description:Map->List<NameValuePair>转化
     */
    private static List<NameValuePair> mapToNameValuePair(Map<String,String> map){
        List<NameValuePair> pairs = new ArrayList<>();
        Set<String> keySet = map.keySet();
        for (String key:keySet) {
            pairs.add(new BasicNameValuePair(key,map.get(key)));
        }
        return pairs;
    }
    
    /**
     * @author: Tangzhihao
     * @date: 2017/12/20 15:42
     * @description:Map->List<NameValuePair>
     */
    private  static List<NameValuePair> mapToNameValuePair2(Map<String,String> map){
        List<NameValuePair> pairs = new ArrayList<>();
        Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String, String> next = iterator.next();
            pairs.add(new BasicNameValuePair(next.getKey(), next.getValue()));
        }
        return  pairs;
    }
}
