package com.edu.util.zookeeper.listener;

import com.edu.util.zookeeper.CuratorClient;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: tangzh
 * @Date: 2019/1/22$ 6:36 PM$
 **/
public class DefaultConnectStateListener extends AbstractConnectStateListener {

    private final static Logger logger = LoggerFactory.getLogger(DefaultConnectStateListener.class);

    public DefaultConnectStateListener(CuratorClient client) {
        super(client);
    }

    protected void doLost(){
        System.out.println("connection lost, waiting for reconnect");
        try {
            client.destroy();
            client.init();
        } catch (Exception e) {
            logger.error("client ReInit Fail",e);
        }
    }
    protected void doConnected(){}
    protected void doReConnected(){}
    protected void doSuspended(){}
    protected void doReadOnly(){}
}
