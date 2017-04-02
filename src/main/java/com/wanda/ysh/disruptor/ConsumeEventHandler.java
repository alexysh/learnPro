/*
 * Copyright 2012-2014 Wanda.cn All right reserved. This software is the
 * confidential and proprietary information of Wanda.cn ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Wanda.cn.
 */
package com.wanda.ysh.disruptor;

import com.lmax.disruptor.EventHandler;

/**
 * 类ConsumeEventHandler.java的实现描述：消费者处理类
 * @author yangshihong Sep 2, 2016 3:26:33 PM
 */
public class ConsumeEventHandler implements EventHandler<ValueEvent>{
    @Override
    public void onEvent(ValueEvent event, long sequence, boolean endOfBatch) throws Exception {

        System.out.println(String.format("=======sequence:%s======endOfBatch:%s========%s",sequence,endOfBatch, event.getValue()));
    }

}