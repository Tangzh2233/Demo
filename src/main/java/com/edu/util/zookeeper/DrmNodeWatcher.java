package com.edu.util.zookeeper;

import com.jiupai.cornerstone.util.ReflectionUtils;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

/**
 * @Author: tangzh
 * @Date: 2019/1/21$ 7:18 PM$
 * 简单的节点监视器,当zookeeper节点值发生发生改变时对应改变项目的节点值
 **/
public class DrmNodeWatcher implements CuratorWatcher {

    private CuratorClient client;
    private AppDrmNode appDrmNode;

    public DrmNodeWatcher(CuratorClient client, AppDrmNode appDrmNode) {
        this.client = client;
        this.appDrmNode = appDrmNode;
    }

    @Override
    public void process(WatchedEvent event) throws Exception {
        if(event.getState()!=Watcher.Event.KeeperState.Disconnected && event.getState()!= Watcher.Event.KeeperState.Expired){
            if(this.client!=null){
                if(Watcher.Event.EventType.NodeDataChanged==event.getType()){
                    try {
                        String value = this.client.getData(event.getPath());
                        Object object = this.appDrmNode.getObject();
                        String paramName = this.appDrmNode.getParamName();
                        ReflectionUtils.writeFieldWithSet(paramName,object,value);
                    } finally {
                        this.client.watch(event.getPath(),this);
                    }
                }
            }
        }
    }

    public CuratorClient getClient() {
        return client;
    }

    public void setClient(CuratorClient client) {
        this.client = client;
    }

    public AppDrmNode getAppDrmNode() {
        return appDrmNode;
    }

    public void setAppDrmNode(AppDrmNode appDrmNode) {
        this.appDrmNode = appDrmNode;
    }

}
