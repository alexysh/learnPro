/*
 * Copyright 2012-2014 Wanda.cn All right reserved. This software is the
 * confidential and proprietary information of Wanda.cn ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Wanda.cn.
 */
package com.wanda.ysh.disruptor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.lmax.disruptor.BatchEventProcessor;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.SequenceBarrier;
import com.lmax.disruptor.YieldingWaitStrategy;

/**
 * 类TestShow.java的实现描述：TODO 类实现描述 
 * @author yangshihong Sep 2, 2016 3:19:36 PM
 */
public class TestShow {

    private static final int BUFFER_SIZE = 16;

    private final RingBuffer<ValueEvent> ringBuffer = RingBuffer.createSingleProducer(
            ValueEvent.EVENT_FACTORY, 
            BUFFER_SIZE,
            new YieldingWaitStrategy());
    
    private final SequenceBarrier sequenceBarrier = ringBuffer.newBarrier();

    private final ConsumeEventHandler handler = new ConsumeEventHandler();

    private final ExecutorService EXECUTOR = Executors.newSingleThreadExecutor();

    private final BatchEventProcessor<ValueEvent> batchEventProcessor = new BatchEventProcessor<ValueEvent>(
            ringBuffer, sequenceBarrier, handler);

    public TestShow() {
        
        ringBuffer.addGatingSequences(batchEventProcessor.getSequence());
    }

    public void consume() {
        
        EXECUTOR.submit(batchEventProcessor);
    }

    public void produce() {
        
        new Thread(new Producer(ringBuffer)).start();
    }

    public static void main(String[] args) {

        TestShow test = new TestShow();
        test.produce();
        test.consume();
    }
}
