/*
 * Copyright 2012-2014 Wanda.cn All right reserved. This software is the
 * confidential and proprietary information of Wanda.cn ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Wanda.cn.
 */
package com.wanda.ysh.zookeeper;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.test.TestingServer;
import org.apache.curator.utils.CloseableUtils;


/**
 * 类CreateClientExample.java的实现描述： 类实现描述 
 * Curator框架提供了一种流式接口。 操作通过builder串联起来， 这样方法调用类似语句一样。
 * client.create().forPath("/head", new byte[0]);
 * client.delete().inBackground().forPath("/head");
 * client.create().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath("/head/child", new byte[0]);
 * client.getData().watched().inBackground().forPath("/test");
 * @author yangshihong Aug 10, 2016 9:44:54 AM
 */


public class CreateClientExample {
    private static final String PATH = "/example/basic";

    public static void main(String[] args) throws Exception {
        TestingServer server = new TestingServer();
        CuratorFramework client = null;
        try {
            client = createSimple(server.getConnectString());
            client.start();
            client.create().creatingParentsIfNeeded().forPath(PATH, "test".getBytes());
            CloseableUtils.closeQuietly(client);

            client = createWithOptions(server.getConnectString(), new ExponentialBackoffRetry(1000, 3), 1000, 1000);
            client.start();
            System.out.println(new String(client.getData().forPath(PATH)));
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            CloseableUtils.closeQuietly(client);
            CloseableUtils.closeQuietly(server);
        }

    }

    public static CuratorFramework createSimple(String connectionString) {
        // these are reasonable arguments for the ExponentialBackoffRetry. 
        // The first retry will wait 1 second - the second will wait up to 2 seconds - the
        // third will wait up to 4 seconds.
        ExponentialBackoffRetry retryPolicy = new ExponentialBackoffRetry(1000, 3);
        // The simplest way to get a CuratorFramework instance. This will use default values.
        // The only required arguments are the connection string and the retry policy
        return CuratorFrameworkFactory.newClient(connectionString, retryPolicy);
    }

    public static CuratorFramework createWithOptions(String connectionString, RetryPolicy retryPolicy, int connectionTimeoutMs, int sessionTimeoutMs) {
        // using the CuratorFrameworkFactory.builder() gives fine grained control
        // over creation options. See the CuratorFrameworkFactory.Builder javadoc details
        return CuratorFrameworkFactory.builder().connectString(connectionString)
                .retryPolicy(retryPolicy)
                .connectionTimeoutMs(connectionTimeoutMs)
                .sessionTimeoutMs(sessionTimeoutMs)
                // etc. etc.
                .build();
    }
}
