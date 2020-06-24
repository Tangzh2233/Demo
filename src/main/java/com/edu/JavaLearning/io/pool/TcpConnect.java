package com.edu.JavaLearning.io.pool;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author tangzh
 * @version 1.0
 * @date 2020/5/24 5:10 PM
 * 自定义池化对象,这里假设开启一个Http连接
 **/
public class TcpConnect {

    private String host;

    private int port;

    private URL connect;

    private TcpConnectPool pool;

    public TcpConnect(String host, int port) throws MalformedURLException {
        this.host = host;
        this.port = port;
        this.connect = new URL("http://" + host + ":" + port);
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public URL getConnect() {
        return connect;
    }

    public void setConnect(URL connect) {
        this.connect = connect;
    }

    public TcpConnectPool getPool() {
        return pool;
    }

    public void setPool(TcpConnectPool pool) {
        this.pool = pool;
    }

    public void close(){
        pool.returnResourceObject(this);
    }
}
