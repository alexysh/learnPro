/*
 * Copyright 2012-2016 Ffan.com All right reserved. This software is the
 * confidential and proprietary information of Ffan.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Ffan.com.
 */
package com.wanda.ysh.encryp.rsa;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;


/**
 * 类RSACoder.java的实现描述：TODO 类实现描述 
 * 非对称加密算法RSA算法组件
 * 非对称算法一般是用来传送对称加密算法的密钥来使用的，相对于DH算法，RSA算法只需要一方构造密钥，不需要
 * 大费周章的构造各自本地的密钥对了。DH算法只能算法非对称算法的底层实现。而RSA算法算法实现起来较为简单
 * @author yangshihong Oct 12, 2016 9:10:32 AM
 * */
public class RSACoder {
    //非对称密钥算法
    public static final String KEY_ALGORITHM="RSA";
    
    
    /**
     * 密钥长度，DH算法的默认密钥长度是1024
     * 密钥长度必须是64的倍数，在512到65536位之间
     * */
    private static final int KEY_SIZE=1024;
    //公钥
    private static final String PUBLIC_KEY="RSAPublicKey";
    
    //私钥
    private static final String PRIVATE_KEY="RSAPrivateKey";
    
    /**
     * 初始化密钥对
     * @return Map 甲方密钥的Map
     * */
    public static Map<String,Object> initKey() throws Exception{
        //实例化密钥生成器
        KeyPairGenerator keyPairGenerator=KeyPairGenerator.getInstance(KEY_ALGORITHM);
        //初始化密钥生成器
        keyPairGenerator.initialize(KEY_SIZE, new SecureRandom("parking".getBytes()));
        keyPairGenerator.initialize(KEY_SIZE);
        //生成密钥对
        KeyPair keyPair=keyPairGenerator.generateKeyPair();
        //甲方公钥
        RSAPublicKey publicKey=(RSAPublicKey) keyPair.getPublic();
        //甲方私钥
        RSAPrivateKey privateKey=(RSAPrivateKey) keyPair.getPrivate();
        //将密钥存储在map中
        Map<String,Object> keyMap=new HashMap<String,Object>();
        keyMap.put(PUBLIC_KEY, publicKey);
        keyMap.put(PRIVATE_KEY, privateKey);
        return keyMap;
        
    }
    
    
    /**
     * 私钥加密
     * @param data待加密数据
     * @param key 密钥
     * @return byte[] 加密数据
     * */
    public static byte[] encryptByPrivateKey(byte[] data,byte[] key) throws Exception{
        
        //取得私钥
        PKCS8EncodedKeySpec pkcs8KeySpec=new PKCS8EncodedKeySpec(key);
        KeyFactory keyFactory=KeyFactory.getInstance(KEY_ALGORITHM);
        //生成私钥
        PrivateKey privateKey=keyFactory.generatePrivate(pkcs8KeySpec);
        //数据加密
        Cipher cipher=Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        return cipher.doFinal(data);
    }
    /**
     * 公钥加密
     * @param data待加密数据
     * @param key 密钥
     * @return byte[] 加密数据
     * */
    public static byte[] encryptByPublicKey(byte[] data,byte[] key) throws Exception{
        
        //实例化密钥工厂
        KeyFactory keyFactory=KeyFactory.getInstance(KEY_ALGORITHM);
        //初始化公钥
        //密钥材料转换
        X509EncodedKeySpec x509KeySpec=new X509EncodedKeySpec(key);
        //产生公钥
        PublicKey pubKey=keyFactory.generatePublic(x509KeySpec);
        
        //数据加密
        Cipher cipher=Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        return cipher.doFinal(data);
    }
    /**
     * 私钥解密
     * @param data 待解密数据
     * @param key 密钥
     * @return byte[] 解密数据
     * */
    public static byte[] decryptByPrivateKey(byte[] data,byte[] key) throws Exception{
        //取得私钥
        PKCS8EncodedKeySpec pkcs8KeySpec=new PKCS8EncodedKeySpec(key);
        KeyFactory keyFactory=KeyFactory.getInstance(KEY_ALGORITHM);
        //生成私钥
        PrivateKey privateKey=keyFactory.generatePrivate(pkcs8KeySpec);
        //数据解密
        Cipher cipher=Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(data);
    }
    /**
     * 公钥解密
     * @param data 待解密数据
     * @param key 密钥
     * @return byte[] 解密数据
     * */
    public static byte[] decryptByPublicKey(byte[] data,byte[] key) throws Exception{
        
        //实例化密钥工厂
        KeyFactory keyFactory=KeyFactory.getInstance(KEY_ALGORITHM);
        //初始化公钥
        //密钥材料转换
        X509EncodedKeySpec x509KeySpec=new X509EncodedKeySpec(key);
        //产生公钥
        PublicKey pubKey=keyFactory.generatePublic(x509KeySpec);
        //数据解密
        Cipher cipher=Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, pubKey);
        return cipher.doFinal(data);
    }
    /**
     * 取得私钥
     * @param keyMap 密钥map
     * @return byte[] 私钥
     * */
    public static byte[] getPrivateKey(Map<String,Object> keyMap){
        Key key=(Key)keyMap.get(PRIVATE_KEY);
        return key.getEncoded();
    }
    /**
     * 取得公钥
     * @param keyMap 密钥map
     * @return byte[] 公钥
     * */
    public static byte[] getPublicKey(Map<String,Object> keyMap) throws Exception{
        Key key=(Key) keyMap.get(PUBLIC_KEY);
        return key.getEncoded();
    }
    /**
     * @param args
     * @throws Exception 
     */
    public static void main(String[] args) throws Exception {
        entryptAndDecrypt();
//        mainTest();
    }


    private static void mainTest() throws Exception {
        //初始化密钥
        //生成密钥对
        Map<String,Object> keyMap=RSACoder.initKey();
        //公钥
        byte[] publicKey=RSACoder.getPublicKey(keyMap);
        
        //私钥
        byte[] privateKey=RSACoder.getPrivateKey(keyMap);
        System.out.println("公钥：\n"+Base64.encodeBase64String(publicKey));
        System.out.println("私钥：\n"+Base64.encodeBase64String(privateKey));
        
        System.out.println("================密钥对构造完毕,甲方将公钥公布给乙方，开始进行加密数据的传输=============");
        String str="RSA密码交换算法";
        System.out.println("\n===========甲方向乙方发送加密数据==============");
        System.out.println("原文:"+str);
        //甲方进行数据的加密
        byte[] code1=RSACoder.encryptByPrivateKey(str.getBytes(), privateKey);
        System.out.println("加密后的数据："+Base64.encodeBase64String(code1));
        System.out.println("===========乙方使用甲方提供的公钥对数据进行解密==============");
        //乙方进行数据的解密
        byte[] decode1=RSACoder.decryptByPublicKey(code1, publicKey);
        System.out.println("乙方解密后的数据："+new String(decode1)+"\n\n");
        
        System.out.println("===========反向进行操作，乙方向甲方发送数据==============\n\n");
        
        str="乙方向甲方发送数据RSA算法";
        
        System.out.println("原文:"+str);
        
        //乙方使用公钥对数据进行加密
        byte[] code2=RSACoder.encryptByPublicKey(str.getBytes(), publicKey);
        System.out.println("===========乙方使用公钥对数据进行加密==============");
        System.out.println("加密后的数据："+Base64.encodeBase64String(code2));
        
        System.out.println("=============乙方将数据传送给甲方======================");
        System.out.println("===========甲方使用私钥对数据进行解密==============");
        
        //甲方使用私钥对数据进行解密
        byte[] decode2=RSACoder.decryptByPrivateKey(code2, privateKey);
        
        System.out.println("甲方解密后的数据："+new String(decode2));
    }
    
    public static void entryptAndDecrypt() throws Exception {
        String publicKeyStr = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCsYIxMdctB5wXJDVi5SCXh0/+shnRQ4Rpm5MHjMSMq83F18PkDawdg71fD4S14RjXtuxBoIHux5lwU0h+EZkIma27PDg1SXc/lOSwDbt9vOwmoeIRKUoyT619u6tRk06bzdxNDmvjEKVy7q71j14z/L7uUvLWeI0aj9uElU6XfYwIDAQAB";
        String privateKeyStr = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAKxgjEx1y0HnBckNWLlIJeHT/6yGdFDhGmbkweMxIyrzcXXw+QNrB2DvV8PhLXhGNe27EGgge7HmXBTSH4RmQiZrbs8ODVJdz+U5LANu3287Cah4hEpSjJPrX27q1GTTpvN3E0Oa+MQpXLurvWPXjP8vu5S8tZ4jRqP24SVTpd9jAgMBAAECgYBZpLQRp0iW8y+9COXVJUIeGQPjkuC2lvoF/H0gS5OAaK3eqfy13Fkv/ghkY5wj6k9tXFUB8bKiJ6xHX6boUc2gFmCgnbBdGx9dMkakP8/nTJclbnESo/D37xSfZJreIM+mGz2xiyS6k0xIwvjBTvsXC2TpLCDYUJ0kBhoVo9uzYQJBAN+MwDY3yeeAwWTjzc7NboObLTmYB3rITQR2p5BWeTVJSZrWSTl0QmFQJ4kBu70lttj8C+8tattEjFYIvqH47jkCQQDFZjByPnW7mtYevDmaVJ6hXCsxiLLA578JjBX1HhIb4Z11wW0lshKVgbWYuV8BU+c6q9m8ndCDBWCg9vWDprp7AkAJCg3zo0ncvI4VYEjAJfpkA/BONeGHi3XhylJ4OabpCq+ZPTT0x8ivWqhel91Zhp7gI0DwSOvbNeheyje7IO+xAkAGXg99/+nITWxOvX+WLNiZF+IEzQYHFvKgmjYzG+81mwd7PuwIZUAYNoDnFkOsRUqWJ+j0HpsVPt0pFA5DWuN1AkAkXZG7J8WqI6PSkjkh/+WfigEp8TdWWnqR6kpFFlxiYJ65UKZio+bcreJTM05BPyjwAbKl099eu4f7SyRVBBRR";
        
        
        String str="乙方向甲方发送数据，RSA公钥加密算法";
        
        System.out.println("原文:"+str);
        
        //乙方使用公钥对数据进行加密
        byte[] encode = RSACoder.encryptByPublicKey(str.getBytes(), Base64.decodeBase64(publicKeyStr));
        String encodeStr = Base64.encodeBase64String(encode);
        System.out.println("===========乙方使用公钥对数据进行加密==============");
        System.out.println("加密后的数据："+encodeStr);
        
        System.out.println("=============乙方将数据传送给甲方======================");
        
        System.out.println("===========甲方使用私钥对数据进行解密==============");
        
        
        //甲方使用私钥对数据进行解密
        byte[] decode = RSACoder.decryptByPrivateKey(Base64.decodeBase64(encodeStr), Base64.decodeBase64(privateKeyStr));
        
        System.out.println("甲方解密后的数据："+new String(decode));
    }
}


