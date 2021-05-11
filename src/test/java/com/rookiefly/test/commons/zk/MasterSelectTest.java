package com.rookiefly.test.commons.zk;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListener;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.retry.RetryNTimes;
import org.apache.curator.test.TestingServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

public class MasterSelectTest {

    private TestingServer zkTestServer;
    
    @Before
    public void startZookeeper() throws Exception {
        zkTestServer = new TestingServer(2181);
    }

    @After
    public void stopZookeeper() throws IOException {
        zkTestServer.stop();
    }

    @Test
    public void testMasterSelect() {
        String masterPath = "/mymaster";
        RetryPolicy retryPolicy = new RetryNTimes(5, 1000);
        CuratorFramework client = CuratorFrameworkFactory.builder().connectString("127.0.0.1:2181").sessionTimeoutMs(5000).connectionTimeoutMs(10000).retryPolicy(retryPolicy).namespace("text").build();
        client.start();
        /**
         * 该实例封装了所有Master选举相关的逻辑，包括所有和Zookeeper服务器交互的过程，其中Master_select代表一个Master选举的一个
         * 根节点，表明本次Master选举都是在该节点下进行的。
         * 在创建LeaderSelector实例的时候，还会传入一个监听器:LeaderSelectorListenerAdapter。这需要开发人员自行实现。Curator
         * 会在成功获取Master权利时候回调该监听器。
         */
        LeaderSelector leaderSelector = new LeaderSelector(client, masterPath, new LeaderSelectorListener() {

            @Override
            public void stateChanged(CuratorFramework client, ConnectionState stat) {

            }

            /**
             *  成为Master角色
             *  完成Master操作，释放Master权利
             *  成为Master角色
             */
            @Override
            public void takeLeadership(CuratorFramework client) throws Exception {
                System.out.println("成为Master");
                Thread.sleep(3000);
                List<String> path = client.getChildren().forPath(masterPath);
                System.out.println(path);
                System.out.println("完成Master操作，释放Master权利");
            }
        });
        leaderSelector.autoRequeue();
        leaderSelector.start();
    }
}