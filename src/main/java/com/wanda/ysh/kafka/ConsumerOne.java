/*
 * Copyright 2012-2014 Wanda.cn All right reserved. This software is the
 * confidential and proprietary information of Wanda.cn ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Wanda.cn.
 */
package com.wanda.ysh.kafka;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;

/**
 * 类ConsumerSample.java的实现描述： 类实现描述 
 * @author yangshihong May 6, 2015 1:36:32 PM
 */
public class ConsumerOne {

    protected String zkConnect = "10.1.169.221:2181";
    protected String groupId = "group_parking";
    protected String topic = "cloudparking";
    protected String sessionTimeOut = "30000";
    private int partition = 1;//
    
    
    private ExecutorService threadPool;
    private ConsumerConnector connector;
    /**
     * @param args
     */
    public static void main(String[] args) {

        new ConsumerOne();
    }
    

    public void init() {
        Properties props = new Properties();
        //注意消费端需要配置成zk的地址，而生产端配置的是kafka的ip和端口。
        props.put("zookeeper.connect", zkConnect);
        props.put("zookeeper.connection.timeout.ms", sessionTimeOut);
        props.put("zookeeper.session.timeout.ms", sessionTimeOut);
        props.put("group.id", groupId);
        props.put("auto.commit.enable", "true");  
        props.put("auto.commit.interval.ms", "60000"); 
        props.put("auto.offset.reset", "smallest");
        
        // Create the connection to the cluster
        ConsumerConfig config = new ConsumerConfig(props);
        connector = Consumer.createJavaConsumerConnector(config);
        
        // create 1 partitions of the stream for topic, to allow 1
        // threads to consume
        Map<String, Integer> topics = new HashMap<String, Integer>();
        topics.put(topic, partition);
        Map<String, List<KafkaStream<byte[], byte[]>>> topicMessageStreams = connector.createMessageStreams(topics);
        List<KafkaStream<byte[], byte[]>> streams = topicMessageStreams.get(topic);
        
        // create list of 1 threads to consume from each of the partitions
        if (threadPool == null) {
            threadPool = Executors.newFixedThreadPool(partition);
        }
        
        for (final KafkaStream<byte[], byte[]> partition : streams) {
            threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        ConsumerIterator<byte[], byte[]> it = partition.iterator();
                        while (it.hasNext()) {
                            MessageAndMetadata<byte[], byte[]> item = it.next();
                            String msg = null;
                            try {
                                msg = new String(item.message(), "UTF-8");
                                System.out.println("consumer one receive>>"+msg +"\tread thread:"+Thread.currentThread().getName());
                            } catch (Exception e) {
                                System.out.println(String.format("KafKa consumer message error;The message is :%s",msg));
                                System.out.println(e.getMessage());
                            }
                            
                        }
                    } catch (Exception e) {
                        System.out.println("KafKa consumer message error;The KafKa OrderStatusMessageReceiver stop work");
                        System.out.println(e.getMessage());
                    }
                    
                }
            });
        }
    }

    public  ConsumerOne() {
        System.out.println("***consumer one******");
        init();
    }

    protected Properties getConsumerProp() {
        Properties props = new Properties();
        //注意消费端需要配置成zk的地址，而生产端配置的是kafka的ip和端口。
        props.put("zookeeper.connect", zkConnect);
        props.put("zookeeper.connection.timeout.ms", sessionTimeOut);
        props.put("zookeeper.session.timeout.ms", sessionTimeOut);
        props.put("group.id", groupId);
        props.put("auto.commit.enable", "true");  
        props.put("auto.commit.interval.ms", "60000"); 
        props.put("auto.offset.reset", "smallest");
        return props;
    }
}
