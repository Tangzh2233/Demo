package com.edu.util.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * @author: tangzh
 * @date: 2019/7/30$ 7:50 PM$
 * @version: 1.0
 * 现有包的实现
 **/
public class ZookeeperLock1 {

    private static CuratorFramework client;
    private static ExponentialBackoffRetry retry;
    private static InterProcessMutex lock;
    private static String zkLockPath = "/zkLock";

    static{
        retry = new ExponentialBackoffRetry(1000, 3);
        client = CuratorFrameworkFactory.newClient("127.0.0.1:2181",retry);
        client.start();
        lock = new InterProcessMutex(client,zkLockPath);
    }

    public static void lock() throws Exception {
        lock.acquire();
    }

    public static void unLock() throws Exception {
        lock.release();
    }
}
