package com.edu.util.zookeeper.listener;

import com.edu.util.zookeeper.CuratorClient;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;

/**
 * @Author: tangzh
 * @Date: 2019/1/23$ 11:41 AM$
 **/
public class AbstractConnectStateListener implements ConnectionStateListener {

    protected CuratorClient client;

    public AbstractConnectStateListener(CuratorClient client) {
        this.client = client;
    }

    @Override
    public void stateChanged(CuratorFramework curatorFramework, ConnectionState connectionState) {
        switch (connectionState){
            case LOST:
                doLost();
                break;
            case CONNECTED:
                doConnected();
                break;
            case RECONNECTED:
                doReConnected();
                break;
            case SUSPENDED:
                doSuspended();
                break;
            case READ_ONLY:
                doReadOnly();
                break;
        }
    }

    protected void doLost(){}
    protected void doConnected(){}
    protected void doReConnected(){}
    protected void doSuspended(){}
    protected void doReadOnly(){}
}
