/*
 * Copyright 2012-2014 Wanda.cn All right reserved. This software is the
 * confidential and proprietary information of Wanda.cn ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Wanda.cn.
 */
package com.wanda.ysh.nosql.mongodb;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Set;
import java.util.Map.Entry;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.alibaba.fastjson.JSONObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoException;
import com.mongodb.MongoOptions;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;

/**
 * 类MongoDBApp.java的实现描述：TODO 类实现描述 
 * @author yangshihong Feb 16, 2016 4:56:47 PM
 */
public class MongoDBApp {

    private static final String host = "localhost";
    private static final int port = 27017;
    public static void main(String[] args) {
        MongoClient client = null;
        try {
            String collName = "app";
            String databaseName = "test";
            client = getMongoClient();
            MongoClientOptions options = client.getMongoClientOptions();
//            MongoOptions options2 = client.getMongoOptions();
//            options2.s
            MongoDatabase db  = client.getDatabase(databaseName);
            Document param = new Document();
            int id = insertData(db, collName);
//            //创建要查询的document
            param.put("id", id);
            param.put("source", "IOS");
            
            queryCollection(db, collName, param);
//            deleteCollection(db, collName);
//            traverseCollection(db);
        }catch (MongoException e) {
                e.printStackTrace();
            }finally {
                if(client != null) {
                    client.close();
                }
            }    
    }

    
    protected static void queryCollection(MongoDatabase db, String collectionName,Document param) {
        try {
            MongoCollection<Document> collection = db.getCollection(collectionName);
            FindIterable<Document> fit = collection.find(param);
            for(Iterator<Document> it = fit.iterator();it.hasNext();){
                Document doc = it.next();
                System.out.print(doc.get("id")+"******");
                System.out.println(doc);
            }
        } catch (MongoException e) {
            e.printStackTrace();
        }
    }
    
    protected static int insertData(MongoDatabase db, String collectionName) {
        try {
            MongoCollection<Document> collection = db.getCollection(collectionName);
            Document document = new Document();
            int id = getRandomBetweenMaxAndMin(1000,999999);
            document.put("id", id);
            document.put("source", "IOS");
            document.put("version", "9.0.0");
            document.put("msg", "Hello world mongoDB in Java "+id);
            //将新建立的document保存到collection中去
            collection.insertOne(document);
            return id;
        } catch (MongoException e) {
            e.printStackTrace();
        }
        return -1;
    }

    protected static void deleteCollection(MongoDatabase db,
            String collectionName) {
        MongoCollection<Document> collection = db.getCollection(collectionName);
        collection.drop();
    }

    protected static void traverseCollection(MongoDatabase db) {
        MongoIterable<String> mit = db.listCollectionNames();
        System.out.println("********** CollectionNames Set***************");
        for(Iterator<String> it = mit.iterator();it.hasNext();){
            System.out.println(it.next());
        }
        System.out.println("********** CollectionNames Set***************");
    }

    protected static MongoClient getMongoClient() {
        //实例化MongoClient对象，连接27017端口
        return new MongoClient(host, port);
    }
    
    
    
    protected static void mongoDB() {
        MongoClient client = null;
        try {
            client = getMongoClient();
//            //连接名为test的数据库，假如数据库不存在的话，mongodb会自动建立
            MongoDatabase db  = client.getDatabase("test");
            //从Mongodb中获得名为yourColleection的数据集合，如果该数据集合不存在，Mongodb会为其新建立
            // 使用BasicDBObject对象创建一个mongodb的document,并给予赋值。
            MongoCollection<Document> collection = db.getCollection("testCol");
            MongoIterable<String> mit = db.listCollectionNames();
            System.out.println("********** CollectionNames Set***************");
            for(Iterator<String> it = mit.iterator();it.hasNext();){
                System.out.println(it.next());
            }
            System.out.println("********** CollectionNames Set***************");
            Document document = new Document();
            int id = getRandomBetweenMaxAndMin(1000,999999);
            document.put("id", id);
            document.put("msg", "Hello world mongoDB in Java "+id);
            //将新建立的document保存到collection中去
            collection.insertOne(document);
            // 创建要查询的document
            Document queryParam = new Document();
            queryParam.put("id", id);
            FindIterable<Document> fit = collection.find(queryParam);
            for(Iterator<Document> it = fit.iterator();it.hasNext();){
                System.out.println(it.next());
            }
            System.out.println("Done");
        } catch (MongoException e) {
            e.printStackTrace();
        }finally {
            if(client != null) {
                client.close();
            }
        }
    }
    
    protected static void mongoDBBeforeVersion2_10_0() {
        try {
            //实例化Mongo对象，连接27017端口
            Mongo mongo = new Mongo("localhost", 27017);
            //连接名为test的数据库，假如数据库不存在的话，mongodb会自动建立
            DB db = mongo.getDB("test");
            DBCollection collection = db.getCollection("testCol");
            Set<String> collSet = db.getCollectionNames();
            System.out.println("********** CollectionNames Set***************");
            for(Iterator<String> it = collSet.iterator();it.hasNext();){
                System.out.println(it.next());
            }
            System.out.println("********** CollectionNames Set***************");
            // Get collection from MongoDB, database named "yourDB"
            //从Mongodb中获得名为yourColleection的数据集合，如果该数据集合不存在，Mongodb会为其新建立
            // 使用BasicDBObject对象创建一个mongodb的document,并给予赋值。
            BasicDBObject document = new BasicDBObject();
            int id = getRandomBetweenMaxAndMin(1000,999999);
            document.put("id", id);
            document.put("msg", "Hello world mongoDB in Java");
            //将新建立的document保存到collection中去
            collection.insert(document);
            // 创建要查询的document
            BasicDBObject searchQuery = new BasicDBObject();
            searchQuery.put("id", id);
            // 使用collection的find方法查找document
            DBCursor cursor = collection.find(searchQuery);
            //循环输出结果
            while (cursor.hasNext()) {
            System.out.println(cursor.next());
            }
            System.out.println("Done"); 
        } catch (MongoException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 获取给定返回的随机数[minValue,maxValue]
     * @param maxValue
     * @param minValue
     * @return
     */
    public static int getRandomBetweenMaxAndMin(int maxValue, int minValue) {
        long randomV = Math.round(Math.random()*(maxValue-minValue)+minValue);
        BigDecimal bdV = new BigDecimal(randomV);
        
        return bdV.intValue();
    }
    
    public static void writeLogToMongoDB(JSONObject json) {
//      String msgResp = String.format("%s PayServlet->calPayLocal%s response data[merchantId:%s,carLicense:%s]: %s", DateUtil.getCurrMillisSecondsString(), version,merchantIdStr, carLicense,result.toJSONString());
//      JSONObject jsonResp = new JSONObject();
//      jsonResp.put("time", System.currentTimeMillis());
//      jsonResp.put("ip", ParkingUtil.getLocalServerIP());
//      jsonResp.put("interface", "calPayLocal");
//      jsonResp.put("messsage", msgResp);
//      ParkingUtil.writeLogToMongoDB(jsonResp);
        //实例化MongoClient对象，连接27017端口
        MongoClient client =  new MongoClient("localhost", 27017);
        MongoDatabase db  = client.getDatabase("test");
        try {
            MongoCollection<Document> collection = db.getCollection("parkingLog");
            Document document = new Document();
            int id = getRandomBetweenMaxAndMin(1000,999999);
            for(Iterator<Entry<String,Object>> it=json.entrySet().iterator();it.hasNext();){
                Entry<String,Object> entry = it.next();
                document.put(entry.getKey(), entry.getValue());
            }
            document.put("id", id);
            //将新建立的document保存到collection中去
            collection.insertOne(document);
        } catch (MongoException e) {
            e.printStackTrace();
        }finally {
            if(client!=null) {
                client.close();
            }
        }
    }
    
    

}
