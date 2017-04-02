/*
 * Copyright 2012-2014 Wanda.cn All right reserved. This software is the
 * confidential and proprietary information of Wanda.cn ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Wanda.cn.
 */
package com.wanda.ysh.kafka;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.RecordMetadata;

/**
 * 类TestCallback.java的实现描述：TODO 类实现描述 
 * @author yangshihong May 7, 2015 5:13:41 PM
 */
public class TestCallback implements Callback {

    @Override
    public void onCompletion(RecordMetadata metadata, Exception exception) {
        System.out.println(exception.getMessage());
        System.out.println("partition:"+metadata.partition());
        System.out.println("topic:"+metadata.topic());

    }

}
