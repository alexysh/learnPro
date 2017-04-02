/*
 * Copyright 2012-2014 Wanda.cn All right reserved. This software is the
 * confidential and proprietary information of Wanda.cn ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Wanda.cn.
 */
package com.wanda.ysh.monitorlog;

import org.apache.log4j.Logger;

/**
 * 类MonitorLog.java的实现描述：TODO 类实现描述 
 * @author yangshihong Jun 6, 2016 2:23:11 PM
 */
public class MonitorLog {
    private static final Logger logger = Logger.getLogger(MonitorLog.class);
    /**
     * @param args
     * @throws InterruptedException 
     */
    public static void main(String[] args) throws InterruptedException {
        int count = 0;
        logger.info(count+" MonitorLog kafka test");
        logger.info(count+" MonitorLog kafka test");
        logger.info(count+" MonitorLog kafka test");
//        while(true) {
//            count++;
//            if (count/100 == 0) {
//                Thread.sleep(1000);
//            }
//            logger.info(count+" MonitorLog kafka test");
//            if(count > 1000) {
//                break;
//            }
//        }
//       

    }

}
