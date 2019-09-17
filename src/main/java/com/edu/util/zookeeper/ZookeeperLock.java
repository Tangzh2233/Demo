package com.edu.util.zookeeper;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

/**
 * @Author: tangzh
 * @Date: 2019/1/18$ 4:13 PM$
 * zookeeper 实现分布式锁
 **/
public class ZookeeperLock {

    //zookeeper客户端
    private CuratorClient client;
    //锁路径
    private String lock_Root;
    //节点值
    private String value;

    public ZookeeperLock(CuratorClient client,String root,String value){
        this.client = client;
        this.lock_Root = root;
        this.value = value;
    }

    public boolean tryLock(){
        try {
            client.createPath(lock_Root,CreateMode.EPHEMERAL_SEQUENTIAL,value);
            return true;
        }catch (Exception e){
            return false;
        }
    }
    public boolean unLock(){
        try {
            Stat stat = client.getStat(lock_Root);
            if(stat==null){
                return false;
            }
            String data = client.getData(lock_Root);
            if(!this.value.equals(data)){
                return false;
            }
            while (true){
                try {
                    stat = client.getStat(lock_Root);
                    if(stat==null){
                        return false;
                    }
                    client.deleteForPath(lock_Root);
                }catch (Exception e){
                    continue;
                }
                break;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }



    public CuratorClient getClient() {
        return client;
    }

    public String getLock_Root() {
        return lock_Root;
    }

    public String getValue() {
        return value;
    }
}
