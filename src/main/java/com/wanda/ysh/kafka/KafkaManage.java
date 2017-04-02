/*
 * Copyright 2012-2014 Wanda.cn All right reserved. This software is the
 * confidential and proprietary information of Wanda.cn ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Wanda.cn.
 */
package com.wanda.ysh.kafka;

import kafka.admin.TopicCommand;

/**
 * 类KafkaManage.java的实现描述：
 * 使用Kafka的同学都知道，我们每次创建Kafka主题（Topic）的时候可以指定分区数和副本数等信息，
 * 如果将这些属性配置到server.properties文件中，以后调用Java API生成的主题将使用默认值，
 * 先改变需要使用命令bin/kafka-topics.sh --zookeeper localhost:2181 --alter --topic my-topic --config max.message.bytes=128000显示的修改，
 * 我们也希望将此过程在Producer调用之前通过API的方式进行设定，无需在之前或之后使用脚本进行操作。
 * 查看源码发现，其实内部所有的实现都是通过TopicCommand的main方法，在此记录两种方式 
 * @author yangshihong Nov 4, 2015 9:06:26 AM
 */
public class KafkaManage {
    protected String zkConnect = "10.1.169.221:2181";
//    protected String groupId = "group_parking";
//    protected String topic = "topicParking";
//    protected String topic = "mykafkaTopic";
//    protected String groupId = "testTopic";
//    protected String topic = "testTopic";
//    protected String groupId = "kafkaRep";
//    protected String topic = "kafkaRep";
    protected String groupId = "group_parking";
    protected String topic = "cloudparking";
    //分区数量
    protected int partition = 3;

    /**
     * @param args
     */
    public static void main(String[] args) {
        KafkaManage kafka = new KafkaManage();
        kafka.createTopic();
        kafka.describeAllTopic();
//        kafka.deleteTopic();
//        kafka.describeAllTopic();

    }
    
    public  void createTopic() {
        String[] options = new String[]{
                "--create",
                "--zookeeper",
                zkConnect,
                "--partitions",
                String.valueOf(partition),
                "--topic",
                topic,
                "--replication-factor",
                "2",
                "--config",
                "max.message.bytes=128000"
            };
            TopicCommand.main(options);
    }
    
    public  void describeAllTopic() {
        String[] options = new String[]{
                "--list",
                "--zookeeper",
                zkConnect
            };
            TopicCommand.main(options);
    }
    
    public  void describeTopic() {
        String[] options = new String[]{
                "--describe",
                "--zookeeper",
                zkConnect,
                "--topic",
                topic,
            };
            TopicCommand.main(options);
    }
    
    public  void alterTopic() {
        String[] options = new String[]{
                "--alter",
                "--zookeeper",
                zkConnect,
                "--topic",
                topic,
                "--config",
                "max.message.bytes=128000"
            };
            TopicCommand.main(options);
    }
    public  void deleteTopic() {
        String[] options = new String[]{
                "--zookeeper",
                zkConnect,
                "--topic",
                topic
            };
        TopicCommand.main(options);
    }
//    public  void alterTopic() {
//        String[] options = new String[]{
//                "--alter",
//                "--zookeeper",
//                zkConnect,
//                "--topic",
//                topic,
//                "--deleteConfig",
//                "max.message.bytes"
//        };
//        TopicCommand.main(options);
//    }

}
