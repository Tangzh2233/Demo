package com.edu.util.zookeeper;

import com.jiupai.cornerstone.zookeeper.listener.DefaultStateListener;
import org.apache.zookeeper.CreateMode;


/**
 * @author tangzh
 * @version 1.0
 * @date 2019/11/1 5:46 PM
 **/
public class ZKClientTest {

    private static final String NAME_SPACE = "jppSeq/uniqueCheck";

    private static com.jiupai.cornerstone.zookeeper.client.CuratorClient zkClient = new com.jiupai.cornerstone.zookeeper.client.CuratorClient();


    public static void main(String[] args) throws Exception {

        zkClient.init(NAME_SPACE, "127.0.0.1:2181", 3000, new DefaultStateListener(zkClient));
        for (int i = 0; i < 5; i++) {
            zkClient.create("/51", CreateMode.EPHEMERAL);
            boolean exists = zkClient.exists("/51");
            System.out.println(exists);
        }
    }


    class Task implements Runnable {

        CuratorClient client;
        String path;

        public Task(CuratorClient client, String path) {
            this.client = client;
            this.path = path;
        }

        @Override
        public void run() {
            try {
                zkClient.create(path, CreateMode.EPHEMERAL);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
