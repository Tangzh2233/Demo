package com.edu.util;

import org.apache.commons.io.IOUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @Author: tangzh
 * @Date: 2018/10/8$ 下午7:25$
 * http连接池工具类。获取使用http连接池管理的httpClient
 * httpclients4.5.x版本直接调用ClosableHttpResponse.close()
 * 就能直接把连接放回连接池,而不是关闭连接,以前的版本貌似要调用其他方法才能把连接放回连接池
 **/
public class HttpPoolClientUtil {

    private final static Logger logger = LoggerFactory.getLogger(HttpPoolClientUtil.class);
    //最大连接数
    private final static int MaxTotal = 100;
    //最大并发数
    private final static int DefaultMaxPerRoute = 5;
    private final static int ConnectTimeout = 5000;
    private final static int ConnectionRequestTimeout = 2000;
    private final static int SocketTimeout = 2000;
    private final static int ReTryCount = 2;
    private final static long IdleTimeOut = 5000L;
    private final static String DefaultEncoding = "UTF-8";
    private static PoolingHttpClientConnectionManager manager ;
    private static ScheduledExecutorService monitorExecutor;


    /**
     * @description:内部类单例httpClient
     **/
    private static class HttpClientHelper{
        private static CloseableHttpClient httpClient ;
        private final static Object syncLock = new Object();
        static {
            System.out.println("内部类静态块加载……");
            synchronized (syncLock){
                if(httpClient==null){
                    httpClient =  HttpClients.custom()
                            //设置连接池管理类
                            .setConnectionManager(getClientConnectionManager())
                            //设置默认请求配置信息
                            .setDefaultRequestConfig(setRequestConfig(getRequestConfigBuilder()))
                            //设置请求失败时是否重试，及重试次数
                            .setRetryHandler(new DefaultHttpRequestRetryHandler(ReTryCount, true))
                            .build();
                }
                //开启监控线程,对异常和空闲线程进行关闭
                monitorExecutor = Executors.newScheduledThreadPool(1);
                monitorExecutor.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        //关闭异常连接
                        manager.closeExpiredConnections();
                        //关闭5s异常连接
                        manager.closeIdleConnections(IdleTimeOut, TimeUnit.MILLISECONDS);
                    }
                },IdleTimeOut,IdleTimeOut,TimeUnit.MILLISECONDS);
                System.out.println("===获取httpClient实例===="+httpClient.toString());
            }
        }
    }

    /**
      * @description:实例化连接池管理类
    **/
    private static PoolingHttpClientConnectionManager getClientConnectionManager(){
        try {
            //支持http/https两种访问协议
            SSLContextBuilder builder = new SSLContextBuilder();
            builder.loadTrustMaterial(null,new TrustSelfSignedStrategy());
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(builder.build());
            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory> create()
                    .register("http", PlainConnectionSocketFactory.getSocketFactory())
                    .register("https", sslsf)
                    .build();
            manager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
            //最大连接数
            manager.setMaxTotal(MaxTotal);
            //最大并发数
            manager.setDefaultMaxPerRoute(DefaultMaxPerRoute);
        } catch (NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
            logger.error("HttpClient连接池实例创建失败",e);
        }finally {
            if(manager==null){
                manager = new PoolingHttpClientConnectionManager();
            }
        }
        return manager;
    }

    private static RequestConfig setRequestConfig(RequestConfig.Builder builder){
        return builder.build();
    }

    private static RequestConfig.Builder getRequestConfigBuilder(){
        RequestConfig.Builder builder = RequestConfig.custom();
        builder.setConnectTimeout(ConnectTimeout)
                .setConnectionRequestTimeout(ConnectionRequestTimeout)
                .setSocketTimeout(SocketTimeout);
        return builder;
    }


    /**
      * @description:doPost请求
    **/
    public static String doPost(String url, Map<String,String> params){
        HttpPost post = new HttpPost(url);
        CloseableHttpClient httpClient = HttpClientHelper.httpClient;
        post.setConfig(setRequestConfig(getRequestConfigBuilder()));
        if(!(params==null||params.size()==0)){
            UrlEncodedFormEntity formEntity = getFormEntity(params);
            post.setEntity(formEntity);
        }
        CloseableHttpResponse response ;
        InputStream content = null;
        try {
            response = httpClient.execute(post);
            if(response!=null){
                content = response.getEntity().getContent();
                return IOUtils.toString(content,DefaultEncoding);
            }
        } catch (IOException e){
            logger.error("httpClientPost execute error",e);
        }finally {
            closeConnect(content,httpClient);
        }
        return null;
    }

    public static String doPost(String url){
        return doPost(url, (Map<String, String>) null);
    }

    public static String doPost(String url,byte[] data){
        HttpPost post = new HttpPost();
        CloseableHttpClient httpClient = HttpClientHelper.httpClient;
        post.setConfig(setRequestConfig(getRequestConfigBuilder()));
        if(data!=null){
            ByteArrayEntity entity = new ByteArrayEntity(data);
            post.setEntity(entity);
        }
        CloseableHttpResponse response;
        InputStream content = null;
        try {
            response = httpClient.execute(post);
            if(response!=null){
               content = response.getEntity().getContent();
               return IOUtils.toString(content,DefaultEncoding);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            closeConnect(content,httpClient);
        }
        return null;
    }

    /**
      * @description：doGet请求
    **/
    public static String doGet(String url,Map<String,String> params) throws Exception {
        URIBuilder builder = new URIBuilder(url);
        if(!(params==null||params.size()==0)){
            params.forEach(builder::setParameter);
        }
        return doGet(builder.build().toString());
    }

    private static String doGet(String url){
        HttpRequestBase get = new HttpGet(url);
        get.setConfig(setRequestConfig(getRequestConfigBuilder()));
        CloseableHttpClient httpClient = HttpClientHelper.httpClient;
        CloseableHttpResponse response;
        InputStream content = null;
        try {
             response = httpClient.execute(get);
             if(response!=null){
                 content = response.getEntity().getContent();
                 return IOUtils.toString(content, DefaultEncoding);
             }
        } catch (IOException e) {
            logger.error("httpClientGet execute error",e);
        }finally {
            closeConnect(content,httpClient);
        }
        return null;
    }

    private static void closeConnect(InputStream content,CloseableHttpClient httpClient){
        try {
            if(content!=null){
                content.close();
            }
            if(httpClient!=null){
                httpClient.close();
            }
        } catch (IOException e) {
            logger.error("httpClient 流|连接 关闭出错",e);
        }
    }

    /**
      * @description:构建form表单对象
    **/
    private static UrlEncodedFormEntity getFormEntity(Map<String,String> params){
        UrlEncodedFormEntity formEntity = null;
        try {
            List<NameValuePair> pairList = new ArrayList<>();
            params.forEach((key, value) -> pairList.add(new BasicNameValuePair(key, value)));
            formEntity = new UrlEncodedFormEntity(pairList,DefaultEncoding);
        } catch (UnsupportedEncodingException e) {
            logger.error("构建form表单对象失败",e);
        }
        return formEntity;
    }

    public static void main(String[] args) {
        Map<String, String> params = new HashMap<>();
        params.put("username","tang");
        params.put("password","123");
        Thread thread = new Thread(){
            @Override
            public void run(){
                try {
                    System.out.println(doGet("http://127.0.0.1:8848/myspringboot/httpPost.do",params));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        Thread thread1 = new Thread(){
            @Override
            public void run(){
                try {
                    System.out.println(doGet("http://127.0.0.1:8848/myspringboot/httpPost.do",params));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        Thread thread2 = new Thread(){
            @Override
            public void run(){
                try {
                    System.out.println(doGet("http://127.0.0.1:8848/myspringboot/httpPost.do",params));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
        thread1.start();
        thread2.start();

    }
}
