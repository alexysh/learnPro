/*
 * Copyright 2012-2016 Ffan.com All right reserved. This software is the
 * confidential and proprietary information of Ffan.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Ffan.com.
 */
package com.wanda.ysh.okhttp;

import java.io.IOException;
import java.net.Proxy;

import com.squareup.okhttp.Authenticator;
import com.squareup.okhttp.Credentials;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

/**
 * 类PostString.java的实现描述：HTTP POST 请求的基本示例
 * @author yangshihong Feb 13, 2017 11:01:57 AM
 */
public class PostString {
    public static void main(String[] args) throws IOException {
     OkHttpClient client = new OkHttpClient();
     MediaType MEDIA_TYPE_TEXT = MediaType.parse("text/plain");
     client.setAuthenticator(new Authenticator() {
            public Request authenticate(Proxy proxy, Response response)
                    throws IOException {
                String credential = Credentials.basic("user", "password");
                return response.request().newBuilder()
                        .header("Authorization", credential).build();
            }

            public Request authenticateProxy(Proxy proxy, Response response)
                    throws IOException {
                return null;
            }
         });
     
     
     String postBody = "Hello World";
     Request request = new Request.Builder()
             .url("http://www.ibm.com/developerworks/cn/java/j-lo-okhttp/index.html")
             .post(RequestBody.create(MEDIA_TYPE_TEXT, postBody))
             .build();

     Response response = client.newCall(request).execute();
     if (!response.isSuccessful()) {
         throw new IOException("服务器端错误: " + response);
     }

     System.out.println(response.body().string());
    }
 }
