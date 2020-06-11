package com.edu.JavaLearning.io.pool;

import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import java.io.IOException;

/**
 * @author tangzh
 * @version 1.0
 * @date 2020/5/24 5:48 PM
 *
 * 典型的享元模式
 * 自定池化对象。关键点是
 *
 * @see GenericObjectPool#borrowObject()        //创建对象
 * @see GenericObjectPool#returnObject(Object)  //释放对象
 * 以及GenericObjectPool的初始化
 * @see GenericObjectPool#GenericObjectPool(PooledObjectFactory, GenericObjectPoolConfig)
 *
 * 1.其中PooledObjectFactory 为自定义需要池化对象的工厂类
 * @see TcpConnectFactory
 * 2.池子的参数配置,如最大连接,最大存活等
 * @see GenericObjectPoolConfig
 **/
public class MyConnectPoolTest {

    public static void main(String[] args) throws IOException {
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        // 最大空闲数
        poolConfig.setMaxIdle(2);
        // 最小空闲数, 池中只有一个空闲对象的时候，池会在创建一个对象，并借出一个对象，从而保证池中最小空闲数为1
        poolConfig.setMinIdle(1);
        // 最大池对象总数
        poolConfig.setMaxTotal(5);
        // 逐出连接的最小空闲时间 默认1800000毫秒(30分钟)
        poolConfig.setMinEvictableIdleTimeMillis(1800000);
        // 逐出扫描的时间间隔(毫秒) 如果为负数,则不运行逐出线程, 默认-1
        poolConfig.setTimeBetweenEvictionRunsMillis(1800000 * 2L);
        // 在获取对象的时候检查有效性, 默认false
        poolConfig.setTestOnBorrow(true);
        // 在归还对象的时候检查有效性, 默认false
        poolConfig.setTestOnReturn(false);
        // 在空闲时检查有效性, 默认false
        poolConfig.setTestWhileIdle(false);
        // 最大等待时间， 默认的值为-1，表示无限等待。
        poolConfig.setMaxWaitMillis(5000);
        // 是否启用后进先出, 默认true
        poolConfig.setLifo(true);
        // 连接耗尽时是否阻塞, false报异常,ture阻塞直到超时, 默认true
        poolConfig.setBlockWhenExhausted(true);
        // 每次逐出检查时 逐出的最大数目 默认3
        poolConfig.setNumTestsPerEvictionRun(3);

        //初始化连接池
        TcpConnectPool connectPool = new TcpConnectPool(poolConfig, "127.0.0.1", 9999);
        TcpConnect resource1 = connectPool.getResource();
        TcpConnect resource2 = connectPool.getResource();
        TcpConnect resource3 = connectPool.getResource();
        TcpConnect resource4 = connectPool.getResource();
        TcpConnect resource5 = connectPool.getResource();

        //此处若未释放,resource6将获取失败
        //因为config设置最大5个连接
        resource1.close();
        TcpConnect resource6 = connectPool.getResource();

        resource2.close();
        resource3.close();
        resource4.close();
        resource5.close();
        resource6.close();

        //连接池关闭
        connectPool.close();
    }
}
