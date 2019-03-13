package com.edu.util.zookeeper;

import com.edu.util.IpUtil;
import com.edu.util.zookeeper.listener.DefaultConnectStateListener;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.Stat;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.nio.charset.Charset;
import java.util.List;

/**
 * @Author: tangzh
 * @Date: 2019/1/17$ 8:35 PM$
 * http://throwable.coding.me/2018/12/16/zookeeper-curator-usage/
 **/
public class CuratorClient {

    private final static Charset charset = Charset.forName("utf-8");

    //zk地址
    private String zkAddress;
    //保证业务之间的隔离性,不同的业务需要分配不同的命名空间
    private String nameSpace;
    //zookeeper客户端
    private CuratorFramework curator;
    //状态监听
    private ConnectionStateListener listener;

    private NodeCache nodeCache;

    public void init() throws InterruptedException {
        init(zkAddress,nameSpace,5000,null);
    }

    /**
     * 初始化客户端
     *
     * @param namespace     命名空间
     * @param address       链接地址+端口列表(ip:port,ip:port)
     * @param timeout       连接创建超时时间，单位毫秒，默认60000ms
     * @param stateListener 状态监听器 ,ConnectionStateListener stateListener
     * @throws Exception
     */
    public void init(String address, String namespace, int timeout,ConnectionStateListener stateListener) throws InterruptedException {
        this.zkAddress = address;
        this.nameSpace = namespace;
        this.listener = stateListener;
        curator = CuratorFrameworkFactory.builder()
                  .connectString(this.zkAddress)
                  .sessionTimeoutMs(60000)
                  .connectionTimeoutMs(timeout)
                  .retryPolicy(new RetryNTimes(5,1000))
                  .namespace(this.nameSpace)
                  .build();

        if(this.listener==null){
            this.listener = new DefaultConnectStateListener(this);
        }
        curator.getConnectionStateListenable().addListener(this.listener);
        curator.start();
        //客户端启动需要事件,这里做循环等待,确保客户端已启动完成
        while (!curator.getZookeeperClient().isConnected()){
            Thread.sleep(200);
        }
    }

    /**
      * @description:添加节点
    **/
    public void createPath(String path, CreateMode mode) throws Exception {
        createPath(path, mode, "");
    }

    public String createPathForReturn(String path,CreateMode mode) throws Exception {
        return createPath(path,mode,"");
    }

    public String createPath(String path, CreateMode mode, String value) throws Exception {
        return curator.create()// 创建一个路径
                .creatingParentsIfNeeded()// 如果指定的节点的父节点不存在，递归创建父节点
                .withMode(mode)// 存储类型（临时的还是持久的）
                .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)// 不设置访问权限
                .forPath(path, value.getBytes(charset));// 创建的路径, 默认空值
    }

    /**
      * @description:节点删除
      * 此方法只能删除叶子节点，否则会抛出异常
      * @params:deletingChildrenIfNeeded()递归删除所有子节点
      *         withVersion()强制指定版本删除
      *         guaranteed()强制保证删除。只要客户端会话有效，那么Curator会在后台持续进行删除操作，直到删除节点成功。
      * 可以进行组合配置删除方式
    **/
    public void deleteForPath(String path) throws Exception {
        curator.delete().forPath(path);
    }

    /**
      * @description:设置节点数据
      * @params: String,String
    **/
    public void setData(String path,String value) throws Exception {
        curator.setData().forPath(path,value.getBytes(charset));
    }

    /**
      * @description:获取节点数据
      * @params: String
    **/
    public String getData(String path) throws Exception {
        byte[] bytes = curator.getData().forPath(path);
        return new String(bytes,charset);
    }

    /**
      * @description:获取节点状态
      * @params: String
    **/
    public Stat getStat(String path) throws Exception {
        return curator.checkExists().forPath(path);
    }

    /**
      * @description:检查节点是否存在
    **/
    public boolean isPathExist(String path) throws Exception {
        Stat stat = getStat(path);
        if(stat!=null){
            return true;
        }
        return false;
    }

    /**
      * @description: 获取子节点
      * @params: String
    **/
    public List<String> getChildren(String path) throws Exception {
        return curator.getChildren().forPath(path);
    }
    /**
      * @description: 添加连接状态监视器
    **/
    public void addConnectStateListener(ConnectionStateListener listener){
        this.listener = listener;
        curator.getConnectionStateListenable().addListener(listener);
    }

    /**
      * @description: 移除连接状态监视器
    **/
    public void removeConnectStateListener(ConnectionStateListener listener){
        curator.getConnectionStateListenable().removeListener(listener);
    }

    public void close(){
        if(curator!=null){
            removeConnectStateListener(listener);
            if(curator.getState()!= CuratorFrameworkState.STOPPED){
                curator.close();
            }
        }
    }

    public void destroy(){
        synchronized (this){
            close();
        }
    }

    /**
      * @description: 添加节点监视器
    **/
    public void watch(String path,CuratorWatcher watcher) throws Exception {
        curator.getData().usingWatcher(watcher).forPath(path);
    }

    public CuratorFramework getCurator() {
        return curator;
    }

    public void setZkAddress(String zkAddress) {
        this.zkAddress = zkAddress;
    }

    public void setNameSpace(String nameSpace) {
        this.nameSpace = nameSpace;
    }

}

class UserDefinedWatcher implements CuratorWatcher{

    private CuratorClient client;

    public UserDefinedWatcher(CuratorClient client){
        this.client = client;
    }

    //注意,节点的watcher为一次性的,使用一次后得重新添加,eg:finally代码块
    @Override
    public void process(WatchedEvent event) throws Exception {
        if(client==null){
            return;
        }
        if(event.getState() == Watcher.Event.KeeperState.Disconnected ||
           event.getState() == Watcher.Event.KeeperState.Expired){
            return;
        }
        if(event.getType() == Watcher.Event.EventType.NodeDataChanged){
            try {
                System.out.println(client.getData(event.getPath()));
            } finally {
                client.watch(event.getPath(),this);
            }
        }
    }
}
class CuratorClientTest{

    public static void main(String[] args) throws Exception {

        String path = "/servers/locks";
        ApplicationContext context = new ClassPathXmlApplicationContext("/xml/beanConfig.xml");
        CuratorClient client = (CuratorClient) context.getBean("curatorClient");
        String node_a = splitNode(client.createPath(path,CreateMode.PERSISTENT, IpUtil.getLocalIpV4Addr()));
        client.watch(path,new UserDefinedWatcher(client));
        client.setData(path,"hahaha");
        System.out.println(client.getChildren(path));
        //循环等待!
        for (int i=0;i<10;i++){
            System.out.println("节点数据修改,等待通知!");
        }
//        client.deleteForPath(path+"/locks");
//        client.close();
//        client.createPath(path,CreateMode.EPHEMERAL,IpUtil.getLocalIpV4Addr());
    }
    private static String splitNode(String nodes){
        String[] node = nodes.split("/");
        return node[node.length-1];
    }
}
