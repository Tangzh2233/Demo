package com.edu.JavaLearning.io.pool;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;

/**
 * @author tangzh
 * @version 1.0
 * @date 2020/5/24 5:20 PM
 * 池化对象工厂
 *
 * 为了实现common-pool对象池管理,必须实现
 * @see org.apache.commons.pool2.PooledObjectFactory
 * 及其子类。BasePooledObjectFactory 是一个基础实现
 **/
public class TcpConnectFactory extends BasePooledObjectFactory<TcpConnect> {

    private String host;
    private int port;

    public TcpConnectFactory(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
     * @return
     * @throws Exception
     * @see PooledObjectFactory#makeObject()
     * 创建池化对象
     */
    @Override
    public TcpConnect create() throws Exception {
        return new TcpConnect(host, port);
    }

    /**
     * 在common-pool2中为了统计管理的对象的一些信息，
     * 比如调用次数，空闲时间，上次使用时间等，需要对管理的对象进行包装，然后在放入到对象池中
     *
     * @param tcpConnect
     * @return
     */
    @Override
    public PooledObject<TcpConnect> wrap(TcpConnect tcpConnect) {
        return new DefaultPooledObject<>(tcpConnect);
    }
}
