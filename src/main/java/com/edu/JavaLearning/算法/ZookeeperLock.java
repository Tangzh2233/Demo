package com.edu.JavaLearning.算法;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author tangzh
 * @version 1.0
 * @date 2020/4/7 10:10 PM
 * zookeeper 分布式锁实现。wait()与notify()重新认知
 **/
//@Component
public class ZookeeperLock {

    private final static Logger log = LoggerFactory.getLogger(ZookeeperLock.class);

    @Value("zk.address")
    private static String zkAddress;
    @Value("zk.port")
    private static int port;

    private ZkClient zkClient = new ZkClient("127.0.0.1:2181", 60000, 5000);
    private static String Name_Space = "/ZK_LOCK";

    private static volatile int product = 5;

    public ZookeeperLock() {
        if (!zkClient.exists(Name_Space)) {
            zkClient.createPersistent(Name_Space);
        }
    }

    private final static Object lockLock = new Object();
    /**
     * 线程变量及对应path保存
     */
    private static ConcurrentMap<Thread, String> threadData = new ConcurrentHashMap<>(256);

    /**
     * 事件监听
     */
    private static IZkDataListener listener = new IZkDataListener() {

        @Override
        public void handleDataChange(String s, Object o) throws Exception {

        }

        @Override
        public void handleDataDeleted(String s) throws Exception {
            System.out.println("节点删除");
            synchronized (lockLock) {
                lockLock.notifyAll();
            }
        }
    };

    public static void main(String[] args) throws Exception {
        //代表3个client
        ZookeeperLock zookeeperLock = new ZookeeperLock();
        ZookeeperLock zookeeperLock1 = new ZookeeperLock();
        ZookeeperLock zookeeperLock2 = new ZookeeperLock();

        for (int i = 0; i < 10; i++) {
            new Thread(new Task(zookeeperLock, "A组")).start();
        }
        for (int i = 0; i < 10; i++) {
            new Thread(new Task(zookeeperLock1, "B组")).start();
        }
        for (int i = 0; i < 10; i++) {
            new Thread(new Task(zookeeperLock2, "C组")).start();
        }
    }

    public boolean lock() {
        return tryLock();
    }

    private boolean tryLock() {
        try {
            String currentPath = zkClient.createEphemeralSequential(Name_Space + "/", "Lock");
            threadData.put(Thread.currentThread(), currentPath);
            while (true) {
                //获取当前线程currentPath
                currentPath = threadData.get(Thread.currentThread());
                List<String> children = zkClient.getChildren(Name_Space);
                Collections.sort(children);
                if (currentPath.substring(Name_Space.length() + 1).equals(children.get(0))) {
                    return true;
                } else {
                    synchronized (lockLock) {
                        int i = Collections.binarySearch(children, currentPath.substring(Name_Space.length() + 1));
                        String beforePath = Name_Space + "/" + children.get(i - 1);
                        zkClient.subscribeDataChanges(beforePath, listener);
                        //判断节点存在与否,当前置节点已经删除,就不需要等待,直接尝试再次获取锁
                        if (zkClient.exists(beforePath)) {
                            //问题？ 这一步时beforePath删除了,此时他最小,然后wait。
                            //再也没有其他线程可以触发删除事件,则所有线程无法被唤醒。
                            //所以删除节点操作和此处应该互斥,或者wait有限时间
                            System.out.println(Thread.currentThread().getName() + "进入等待");
                            lockLock.wait(1000);
                        }
                        zkClient.unsubscribeDataChanges(beforePath, listener);
                    }
                }
            }
        } catch (Exception e) {
            log.error("锁获取失败", e);
        }
        return false;
    }


    private void unLock() {
        String path = threadData.get(Thread.currentThread());
        if (StringUtils.isNotBlank(path)) {
            zkClient.delete(path);
            threadData.remove(Thread.currentThread());
        }
    }

    static class Task implements Runnable {

        ZookeeperLock lock;
        String threadFlag;

        public Task(ZookeeperLock lock, String threadFlag) {
            this.lock = lock;
            this.threadFlag = threadFlag;
        }

        @Override
        public void run() {
            try {
                if (lock.lock()) {
                    try {
                        if (product > 0) {
                            --product;
                            System.out.println(threadFlag + Thread.currentThread().getName() + "获取成功：" + product);
                        } else {
                            System.out.println(threadFlag + Thread.currentThread().getName() + "产品告罄");
                        }
                    } finally {
                        lock.unLock();
                    }
                } else {
                    System.out.println(threadFlag + Thread.currentThread().getName() + "获取失败");
                }
            } catch (Exception e) {

            }
        }
    }
}
