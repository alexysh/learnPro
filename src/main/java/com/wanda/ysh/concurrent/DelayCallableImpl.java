/*
 * Copyright 2012-2014 Wanda.cn All right reserved. This software is the
 * confidential and proprietary information of Wanda.cn ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Wanda.cn.
 */
package com.wanda.ysh.concurrent;

import java.util.concurrent.Callable;

/**
 * 类DelayCallableImpl.java的实现描述：TODO 类实现描述 
 * @author yangshihong May 12, 2015 6:54:00 PM
 */
public class DelayCallableImpl implements Callable<String> {

    /**
     * 
     */
    private String name;
    public DelayCallableImpl (String name) {
        this.name = name;
    }
    @Override
    public String call() throws Exception {
//        Thread.sleep(500);
        String result = "invoke CallableImpl call method,task  "+name+",threadName is "+Thread.currentThread().getName();
        System.out.println(result);
        return result;
    }

}
