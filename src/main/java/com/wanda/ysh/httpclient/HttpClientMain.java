/*
 * Copyright 2012-2016 Ffan.com All right reserved. This software is the
 * confidential and proprietary information of Ffan.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Ffan.com.
 */
package com.wanda.ysh.httpclient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 类HttpClientMain.java的实现描述：TODO 类实现描述 
 * @author yangshihong Nov 2, 2016 10:59:25 AM
 */
public class HttpClientMain {
    /**
     * log日志
     */
    private static final Logger logger = Logger.getLogger(HttpClientMain.class);
    /**
     * @param args
     */
    public static void main(String[] args) {
//        HttpClientMain.doGetVersion4_3("http://ifeve.com/httpclient-2-3/","",1,3000);
        HttpClientMain.doGetVersion4_3("https://mapi.etcp.cn/merchant/open/v1/ffan/getCarStatus?merchant_no=c14d59fcaf6348e6970835d34f0c7590&time_stamp=20170227113344&data=%7B%22mobile%22%3A%2215110254093%22%7D&sign=82F675A6806DCA6FCE7345924E545BBA","",3000,3000);
//       System.out.println(HttpClientMain.doGetVersion4_3("http://api.sit.ffan.com/cloudparking/v3/carParks?cityId=110100","",1,100));

    }
    
    /**
     * HttpClient4.3及以上版本超时设置
     * @param reqUrl
     * @param traceId
     * @param connectionTimeout 连接超时时间
     * @param httpSocketTimeout 数据传输超时时间
     * @return
     */
    public static JSONObject doGetVersion4_3(String reqUrl, String traceId,int connectionTimeout, int httpSocketTimeout){
//        HttpClient client = HttpClientBuilder.create().build();
        
        //HttpClient代理配置
        //即使HttpClient意识到路由方案和代理连接的复杂性，它也只支持简单直连或单跳代理连接的开箱即用
        HttpHost proxy = new HttpHost("10.199.75.12", 8080);
        DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
        CloseableHttpClient client = HttpClients.custom().setRoutePlanner(routePlanner).build();
        
        String resultStr = "";
        //添加身份唯一标识
//        reqUrl = addTimeStamp(reqUrl);
        logger.info(String.format("%s request params[traceId:%s]", reqUrl, traceId));
        try{
            HttpGet request = new HttpGet(reqUrl);//HTTP Get请求(POST雷同)
            request.setHeader("Accept", "application/json");
            //4.3及以上版本超时设置
          RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(httpSocketTimeout).setConnectTimeout(connectionTimeout).build();//设置请求和传输超时时间
          request.setConfig(requestConfig);
            HttpResponse response = client.execute(request);
            BufferedReader rd = new BufferedReader (new InputStreamReader(response.getEntity().getContent(),"utf-8"));
            String line = "";
            
            while ((line = rd.readLine()) != null) {
                resultStr  = resultStr +  line;
            }
            if(logger.isDebugEnabled()) {
                logger.debug(String.format("*****HttpProxy get response,result [%s]", resultStr));
            }
            
        }catch (SocketTimeoutException ste) {
            logger.error(String.format("%s >> traceId:%s >> %s",reqUrl, traceId,ste));
            resultStr = HttpClientMain.getHttpResult(408, ste.getMessage()).toString();
        }catch (ConnectTimeoutException cte) {
            logger.error(String.format("%s >> traceId:%s >> %s",reqUrl, traceId,cte));
            resultStr = HttpClientMain.getHttpResult(408, cte.getMessage()).toString();
        } catch(Exception e) {
            logger.error(String.format("%s >> traceId:%s >> %s",reqUrl, traceId,e));
        }finally {
            client.getConnectionManager().shutdown();
        }
        
        JSONObject result = null;
        try {
            result = JSON.parseObject(resultStr);
        } catch (Exception e) {
            logger.error("traceId:" + traceId, e);
            result = HttpClientMain.getHttpResult(2039, "接口调用返回结果不是json格式");
        }
        logger.info(String.format("%s response result[traceId:%s]：%s", reqUrl, traceId, resultStr));
        return result;
    }
    
    @SuppressWarnings("deprecation")
    public static JSONObject doGetBeforeVersion4_3(String reqUrl, String traceId,int connectionTimeout, int httpSocketTimeout){
        HttpClient client = new DefaultHttpClient();
        String resultStr = "";
        //添加身份唯一标识
//        reqUrl = addTimeStamp(reqUrl);
        logger.info(String.format("%s request params[traceId:%s]", reqUrl, traceId));
        try{
            HttpGet request = new HttpGet(reqUrl);
            request.setHeader("Accept", "application/json");
            /** 4.X版本的超时设置(4.3后已过时) */
            //连接超时时间
            client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,connectionTimeout);
            //数据传输超时时间
            client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,httpSocketTimeout);
            
            HttpResponse response = client.execute(request);
            BufferedReader rd = new BufferedReader (new InputStreamReader(response.getEntity().getContent(),"utf-8"));
            String line = "";
            
            while ((line = rd.readLine()) != null) {
                resultStr  = resultStr +  line;
            }
            if(logger.isDebugEnabled()) {
                logger.debug(String.format("*****HttpProxy get response,result [%s]", resultStr));
            }
            
        }catch (SocketTimeoutException ste) {
            logger.error(String.format("%s >> traceId:%s >> %s",reqUrl, traceId,ste));
            JSONObject tmp = new JSONObject();
            resultStr = HttpClientMain.getHttpResult(408, ste.getMessage()).toString();
        } catch(Exception e) {
            logger.error(String.format("%s >> traceId:%s >> %s",reqUrl, traceId,e));
        }finally {
            client.getConnectionManager().shutdown();
        }
        
        JSONObject result = null;
        try {
            result = JSON.parseObject(resultStr);
        } catch (Exception e) {
            logger.error("traceId:" + traceId, e);
            result = HttpClientMain.getHttpResult(2039, "接口调用返回结果不是json格式");
        }
        logger.info(String.format("%s response result[traceId:%s]：%s", reqUrl, traceId, resultStr));
        return result;
    }
    
    
    public static JSONObject getHttpResult(int code,String message)
    {
        JSONObject resultObj = new JSONObject();
        resultObj.put("status", code);
        resultObj.put("message", message);
        return resultObj;
    }

}
