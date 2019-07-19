package com.edu.util;

import com.google.common.base.Joiner;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: tangzh
 * @Date: 2019/4/3$ 12:00 PM$
 **/
public class HttpClientUtil2 {

    private final static Logger log = LoggerFactory.getLogger(HttpClientUtil2.class);

    private static int DEFAULT_TIMEOUT = 120;

    private final static int MAX_TOTAL_CONNECTIONS = 200;

    private final static int MAX_ROUTE_CONNECTIONS = 200;

    private final static int READ_TIMEOUT = Integer.MAX_VALUE;

    private final static int TEST_READ_TIMEOUT = Integer.MAX_VALUE;

    private final static int CONNECT_TIMEOUT = Integer.MAX_VALUE;

    private final static int TEST_CONNECT_TIMEOUT = Integer.MAX_VALUE;

    private final static int WAIT_TIMEOUT = Integer.MAX_VALUE;

    private static CloseableHttpClient client;

    private static RequestConfig testRequestConfig = RequestConfig.custom().setConnectionRequestTimeout(WAIT_TIMEOUT)
            .setConnectTimeout(TEST_CONNECT_TIMEOUT).setSocketTimeout(TEST_READ_TIMEOUT).build();

    static {
        SSLContext sslcontext = SSLContexts.createSystemDefault();
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", new SSLConnectionSocketFactory(sslcontext))
                .build();
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        connectionManager.setMaxTotal(MAX_TOTAL_CONNECTIONS);
        RequestConfig defaultRequestConfig = RequestConfig.custom().setConnectionRequestTimeout(WAIT_TIMEOUT)
                .setConnectTimeout(CONNECT_TIMEOUT)
                .setSocketTimeout(READ_TIMEOUT).build();
        connectionManager.setDefaultMaxPerRoute(MAX_ROUTE_CONNECTIONS);
        client = HttpClients.custom().setConnectionManager(connectionManager)
                .setDefaultRequestConfig(defaultRequestConfig).build();
    }


    public  static String sendGetRequest(String url) throws Exception {
        return sendGetRequest(url, null);
    }

    public static String sendTestRequest(String url) throws Exception {
        return sendTestRequest(url, null);
    }

    /**
     * 发送http Get请求
     * @param url	请求地址
     * @param params	请求的参数
     */
    public static String sendGetRequestParam(String url, Map<String, String> params) throws Exception{
        StringBuilder builder = new StringBuilder(url);
        if (!url.contains("?")) {
            builder.append("?");
        }
        builder.append(Joiner.on("&").withKeyValueSeparator("=").join(params));

        return sendGetRequest(builder.toString(), null);
    }

    @SuppressWarnings("unchecked")
    public static String sendGetRequest(String url, String decodeCharset) throws Exception {
        long start = System.currentTimeMillis();
        String responseContent = null;

        HttpGet httpGet = new HttpGet(url);
        HttpEntity entity = null;
        try {
            HttpResponse response = client.execute(httpGet);
            entity = response.getEntity();
            if (null != entity) {
                responseContent = EntityUtils.toString(entity, decodeCharset == null ? "UTF-8" : decodeCharset);
            }
            log.info("调用接口{}耗时为{}毫秒", new Object[]{url, System.currentTimeMillis() - start});
        } catch (Exception e) {
            log.error("访问" + url + "异常,信息如下",  e);
            throw e;
        } finally {
            try {
                EntityUtils.consume(entity);
            } catch (Exception ex) {
                log.error("net io exception ", ex);
            }
        }

        if (responseContent == null) {
            log.info("url[{}] failed", url);
        } else {
            log.info("url[{}] ret[{}]", new String[] {url, responseContent});
        }

        return responseContent;
    }

    public static String sendPostRequest(String url, Map<String, String> params) throws Exception {
        return sendPostRequest(url, params, null);
    }

    public static String sendPostRequest(String url, Map<String, String> params,  String decodeCharset) throws Exception {
        long start = System.currentTimeMillis();
        HttpPost post = new HttpPost(url);
        List<BasicNameValuePair> postData = new ArrayList<>();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            postData.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }

        HttpEntity httpEntity = null;
        String responseContent = null;
        try {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(postData, decodeCharset == null ? "UTF-8" : decodeCharset);
            post.setEntity(entity);
            HttpResponse response = client.execute(post);

            httpEntity = response.getEntity();
            if (httpEntity != null) {
                responseContent = EntityUtils.toString(httpEntity, decodeCharset == null ? "UTF-8" : decodeCharset);
            }

            log.info("调用接口{}耗时为{}毫秒", url, System.currentTimeMillis() - start);
        } catch (Exception ex) {
            log.error("访问" + url + "异常,信息如下",  ex);
            throw  ex;
        } finally {
            try {
                EntityUtils.consume(httpEntity);
            } catch (Exception ex) {
                log.error("net io exception ", ex);
            }
        }
        return responseContent;
    }

    public static String sendPostRequest(String url, String requestContent,  String decodeCharset) {
        long start = System.currentTimeMillis();
        HttpPost post = new HttpPost(url);

        HttpEntity httpEntity = null;
        String responseContent = null;
        try {
            post.setEntity(new StringEntity(requestContent, "UTF-8"));
            HttpResponse response = client.execute(post);
            httpEntity = response.getEntity();
            if (httpEntity != null) {
                responseContent = EntityUtils.toString(httpEntity, decodeCharset == null ? "UTF-8" : decodeCharset);
            }

            log.info("调用接口{}耗时为{}毫秒", new Object[]{url, System.currentTimeMillis() - start});
        } catch (Exception ex) {
            log.error("访问" + url + "异常,信息如下",  ex);
        } finally {
            try {
                EntityUtils.consume(httpEntity);
            } catch (Exception ex) {
                log.error("net io exception ", ex);
            }
        }
        return responseContent;
    }

    public static String sendTestRequest(String url, String decodeCharset) throws Exception {
        long start = System.currentTimeMillis();
        String responseContent = null;

        HttpGet httpGet = new HttpGet(url);
        httpGet.setConfig(testRequestConfig);
        HttpEntity entity = null;
        try {
            HttpResponse response = client.execute(httpGet);
            log.info("调用第三方接口：{}", httpGet);

            entity = response.getEntity();
            if(response.getStatusLine().getStatusCode() > 400){
                log.error("访问{}异常，状态码：{}", url, response.getStatusLine().getStatusCode());
                return null;
            }
            if (null != entity) {
                responseContent = EntityUtils.toString(entity, decodeCharset == null ? "UTF-8" : decodeCharset);
            }
            log.info("调用接口{}耗时为{}毫秒", url, System.currentTimeMillis() - start);
        } catch (Exception e) {
            log.error("访问" + url + "异常,信息如下",  e);
            throw e;
        } finally {
            try {
                EntityUtils.consume(entity);
            } catch (Exception ex) {
                log.error("net io exception ", ex);
            }
        }

        return responseContent;
    }

    public static String httpPost(String url, String param) {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost(url);
        post.setHeader("Content-Type", "application/json");
        post.setEntity(new StringEntity(param, "UTF-8"));


        String result = null;
        try{
            HttpResponse response = client.execute(post);
            log.info("调用第三方接口：{},时间为：{}", post, System.currentTimeMillis());
            if(null != response){
                result = EntityUtils.toString(response.getEntity(), "UTF-8");
            }
        } catch(Exception e){
            log.error("Fail to get response.", e);
        }

        return result;
    }

    public static void main(String[] args) {
    }

}

