package com.edu.util.zookeeper.listener;

import com.edu.util.zookeeper.CuratorClient;
import com.edu.util.zookeeper.DrmZookeeperClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: tangzh
 * @Date: 2019/1/23$ 11:38 AM$
 **/
public class DrmConnectStateListener extends AbstractConnectStateListener{

    private final static Logger logger = LoggerFactory.getLogger(DrmConnectStateListener.class);

    public DrmConnectStateListener(CuratorClient client) {
        super(client);
    }

    @Override
    protected void doLost(){
        System.out.println("connection lost, waiting for reconnect");
        try {
            DrmZookeeperClient.getDrmZookeeperClient().reInit();
        } catch (Exception e) {
            logger.error("client ReInit Fail",e);
        }
    }
}
