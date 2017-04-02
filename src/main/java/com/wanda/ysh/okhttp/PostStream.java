/*
 * Copyright 2012-2016 Ffan.com All right reserved. This software is the
 * confidential and proprietary information of Ffan.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Ffan.com.
 */
package com.wanda.ysh.okhttp;

import java.io.IOException;

import okio.BufferedSink;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

/**
 * 类PostStream.java的实现描述：使用流方法提交 HTTP POST 请求的示例 
 * 当请求内容较大时，应该使用流来提交。这里创建了 RequestBody 的一个匿名子类。
 * 该子类的 contentType 方法需要返回内容的媒体类型，而 writeTo方法的参数是一个BufferedSink对象。
 * 我们需要做的就是把请求的内容写入到 BufferedSink 对象即可
 * @author yangshihong Feb 13, 2017 11:00:01 AM
 */
public class PostStream {
    public static void main(String[] args) throws IOException {
     OkHttpClient client = new OkHttpClient();
     final MediaType MEDIA_TYPE_TEXT = MediaType.parse("text/plain");
     final String postBody = "Hello World";

     RequestBody requestBody = new RequestBody() {
         @Override
         public MediaType contentType() {
             return MEDIA_TYPE_TEXT;
         }

         @Override
         public void writeTo(BufferedSink sink) throws IOException {
             sink.writeUtf8(postBody);
         }

  @Override
         public long contentLength() throws IOException {
             return postBody.length();
         }
     };

     Request request = new Request.Builder()
             .url("http://www.baidu.com")
             .post(requestBody)
             .build();

     Response response = client.newCall(request).execute();
     if (!response.isSuccessful()) {
         throw new IOException("服务器端错误: " + response);
     }

     System.out.println(response.body().string());
    }
 }
