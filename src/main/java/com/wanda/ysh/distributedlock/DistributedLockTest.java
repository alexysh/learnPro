/*
 * Copyright 2012-2016 Ffan.com All right reserved. This software is the
 * confidential and proprietary information of Ffan.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Ffan.com.
 */
package com.wanda.ysh.distributedlock;
/**
 * 类DistributedLockTest.java的实现描述：分布式锁测试 
 * @author yangshihong Mar 2, 2017 9:57:55 AM
 */
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.apache.zookeeper.KeeperException;


/**
 * 分布式锁测试
 * @author taomk
 * @version 1.0
 * @since 15-11-19 上午11:48
 */
public class DistributedLockTest {
    private static final Logger logger = Logger.getLogger(DistributedLockTest.class);
    public static void main(String [] args) {
        ExecutorService executor = Executors.newCachedThreadPool();
        final int count = 2;
        final CountDownLatch latch = new CountDownLatch(count);
        for (int i = 0; i < count; i++) {
            final DistributedLock node = new DistributedLock("/locks");
            executor.submit(new Runnable() {
                public void run() {
                    try {
                        Thread.sleep(1000);
//                        node.tryLock(); // 无阻塞获取锁
                        node.lock(); // 阻塞获取锁
                        Thread.sleep(100);

                        System.out.println("id: " + node.getId() + " is leader: " + node.isOwner());
                        logger.info("id: " + node.getId() + " is leader: " + node.isOwner());
                    } catch (InterruptedException e) {
                        logger.error(e);
                        e.printStackTrace();
                    } catch (KeeperException e) {
                        logger.error(e);
                        e.printStackTrace();
                    } catch (Exception e) {
                        logger.error(e);
                        e.printStackTrace();
                    } finally {
                        latch.countDown();
                        try {
                            node.unlock();
                        } catch (KeeperException e) {
                            logger.error(e);
                            e.printStackTrace();
                        }
                    }

                }
            });
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            logger.error(e);
            e.printStackTrace();
        }

        executor.shutdown();
    }
}
