package com.edu.JavaLearning.io.pool;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * @author tangzh
 * @version 1.0
 * @date 2020/5/24 6:33 PM
 *
 * 将具体业务实现和对GenericObjectPool的操作分开
 **/
public class TcpConnectPool extends ConnectPool<TcpConnect> {

    public TcpConnectPool(GenericObjectPoolConfig config, String host, int port) {
        super(config, new TcpConnectFactory(host, port));
    }

    @Override
    public TcpConnect getResource() {
        TcpConnect resource = super.getResource();
        resource.setPool(this);
        return resource;
    }

    @Override
    public void close() {
        super.close();
    }
}
