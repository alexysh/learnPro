/*
 * Copyright 2012-2014 Wanda.cn All right reserved. This software is the
 * confidential and proprietary information of Wanda.cn ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Wanda.cn.
 */
package com.wanda.ysh.kafka;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;


/**
 * 类ProducerSample.java的实现描述：TODO 类实现描述 
 * @author yangshihong May 6, 2015 10:18:54 AM
 */
public class ProducerClient {
    protected String groupId = "group_parking";
    protected String topic = "cloudparking";
    protected String sessionTimeOut = "30000";
    private  Producer<byte[],byte[]> producer;
    private int partition = 3;//分区号，从0开始
    
    /**
     * @param args
     */
    public static void main(String[] args) {

        ProducerClient ps = new ProducerClient();
        ps.produceMsg();
    }
    
    public  ProducerClient(){
        Map<String,Object> props = new HashMap<String, Object>();
//        props.put("metadata.broker.list", "127.0.0.1:9090,127.0.0.1:9091,127.0.0.1:9092");
        props.put("key.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");
        props.put("bootstrap.servers", "10.1.169.221:9090,10.1.169.221:9091,10.1.169.221:9092");
//        props.put("group.id", groupId);
        
        /** # producer接收消息ack的时机.默认为0. 
            # 0: producer不会等待broker发送ack 
            # 1: 当leader接收到消息之后发送ack 
            # 2: 当所有的follower都同步消息成功后发送ack. 
        */
        props.put("request.required.acks","2");
        producer = new KafkaProducer<byte[],byte[]>(props);
    }
    
    public void produceMsg() {
        for (int index = 0; index < 2; index++) {
//            String key =  String.valueOf(index) + "_"  + getRandomBetweenMaxAndMin(1000,99999999);
            String key =  String.valueOf(index);
            String data = "kafka Produce Message " + key;
//            ProducerRecord<byte[],byte[]> pr = new ProducerRecord<byte[],byte[]>(topic,  partitionTmp, key.getBytes(), data.getBytes());
//            ProducerRecord<byte[],byte[]> pr = new ProducerRecord<byte[],byte[]>(topic,  key.getBytes(), data.getBytes());
            ProducerRecord<byte[], byte[]> pr = new ProducerRecord<byte[], byte[]>(topic, key.getBytes(), data.getBytes());
            Future<RecordMetadata> future = producer.send(pr);
            String partitonInfo = "none";
            try {
                RecordMetadata rm = future.get();
                if (rm != null) {
//                    System.out.print("partition:" + rm.partition());
//                    System.out.println("\t topic:" + rm.topic());
                    partitonInfo = "partition:" + rm.partition() +"\ttopic:" + rm.topic();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            System.out.println("producer send>>"+data+ "\tfrom "+ partitonInfo);
        }
        producer.close();
    }
    
  
    public int partition(Object key, int partitions) {
        int partitionNum;
        try {
            if (key instanceof String) {
                partitionNum=  Math.abs(Integer.parseInt((String) key) % partitions);
            } else {
                partitionNum = Math.abs(key.hashCode() % partitions);
            }
        } catch (Exception e) {
            partitionNum = Math.abs(key.hashCode() % partitions);
        }
        return partitionNum;
    }
    
    /**
     * 获取给定返回的随机数[minValue,maxValue]
     * @param maxValue
     * @param minValue
     * @return
     */
    public  int getRandomBetweenMaxAndMin(int maxValue, int minValue) {
        long randomV = Math.round(Math.random()*(maxValue-minValue)+minValue);
        BigDecimal bdV = new BigDecimal(randomV);
        
        return bdV.intValue();
    }


}
