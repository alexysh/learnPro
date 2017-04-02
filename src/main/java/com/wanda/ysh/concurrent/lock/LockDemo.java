/*
 * Copyright 2012-2014 Wanda.cn All right reserved. This software is the
 * confidential and proprietary information of Wanda.cn ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Wanda.cn.
 */
package com.wanda.ysh.concurrent.lock;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 类LockDemo.java的实现描述：TODO 类实现描述 
 * @author yangshihong Mar 14, 2016 1:45:18 PM
 */
public class LockDemo {

    /**
     * @param args
     */
    public static void main(String[] args) {
        
        final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
            new Thread(){
                public void run() {
                    for(int index =0;index < 655380;index++) {
                        rwLock.writeLock();
                    }
                }
            }.start(); 
//        final ReentrantLockTest test = new ReentrantLockTest();
//            new Thread(){
//                public void run() {
//                    Object v = test.getDataAndSet("kkk1");
//                    System.out.println("value>>"+v);
//                }
//            }.start();

    }

}
