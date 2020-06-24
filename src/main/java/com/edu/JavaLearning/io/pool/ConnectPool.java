package com.edu.JavaLearning.io.pool;

import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * @author tangzh
 * @version 1.0
 * @date 2020/5/24 5:29 PM
 *
 * 参考实现
 * @see redis.clients.util.Pool
 *
 **/
public abstract class ConnectPool<T> implements Cloneable{

    private GenericObjectPool<T> internalPool;


    public ConnectPool(final GenericObjectPoolConfig config, PooledObjectFactory<T> factory) {
        internalPool = new GenericObjectPool<>(factory, config);
    }

    /**
     * 通过自定义的TcpConnectFactory工厂类创建池化对象
     * @return
     */
    public T getResource() {
        try {
            return internalPool.borrowObject();
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * 池化对象释放
     * @param connect
     */
    public void returnResourceObject(T connect) {
        if (connect == null) {
            return;
        }
        internalPool.returnObject(connect);
    }

    public void close() {
        if (internalPool != null) {
            internalPool.close();
        }
    }


}
