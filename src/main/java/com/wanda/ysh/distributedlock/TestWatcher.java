/*
 * Copyright 2012-2016 Ffan.com All right reserved. This software is the
 * confidential and proprietary information of Ffan.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Ffan.com.
 */
package com.wanda.ysh.distributedlock;

import org.apache.log4j.Logger;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeperMain;

/**
 * 类TestWatcher.java的实现描述：自定义Watcher 
 * @author yangshihong Mar 2, 2017 11:30:51 AM
 */
public class TestWatcher implements Watcher {
    private static final Logger logger = Logger.getLogger(TestWatcher.class);

    @Override
    public void process(WatchedEvent event) {
        ZooKeeperMain.printMessage("WATCHER::");
        ZooKeeperMain.printMessage(event.toString());
        logger.info("WATCHER::");
        logger.info(event.toString());
    }

}
