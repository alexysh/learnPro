/*
 * Copyright 2012-2014 Wanda.cn All right reserved. This software is the
 * confidential and proprietary information of Wanda.cn ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Wanda.cn.
 */
package com.wanda.ysh.disruptor;

import com.lmax.disruptor.RingBuffer;

/**
 * 类Producer.java的实现描述：
 * @author yangshihong Sep 2, 2016 3:22:04 PM
 */
public class Producer implements Runnable {

    private RingBuffer<ValueEvent> ringBuffer = null;

    public Producer(RingBuffer<ValueEvent> rb) {

        ringBuffer = rb;
    }

    @Override
    public void run() {
        // Publishers claim events in sequence
        int count = 1;
        while(true) {
            
            long sequence = ringBuffer.next();
            ValueEvent event = ringBuffer.get(sequence);
            event.setValue(count); // this could be more complex with multiple fields
            
            // make the event available to EventProcessors
            ringBuffer.publish(sequence); 
            System.out.println("producer content:"+count);
            count++;
//            try {
//                Thread.sleep(5000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }
    }
}
