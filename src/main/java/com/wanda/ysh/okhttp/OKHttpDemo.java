package com.wanda.ysh.okhttp;

import java.io.IOException;

import com.squareup.okhttp.Headers;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.wanda.ysh.httpclient.LoggingInterceptor;

/**
 * http://www.ibm.com/developerworks/cn/java/j-lo-okhttp/index.html#icomments
 * 类OKHttpDemo.java的实现描述：TODO 类实现描述 
 * @author yangshihong Jan 19, 2016 4:06:55 PM
 */
public class OKHttpDemo {

    public static void main(String[] args) throws IOException {

        String url = "http://api.sit.ffan.com/cloudparking/v3/plazas";
        test(url);
       }

    public static void test(String url) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();

//        client.interceptors().add(new LoggingInterceptor()); //添加应用拦截器
        client.networkInterceptors().add(new LoggingInterceptor()); //添加网络拦截器
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new IOException("服务器端错误: " + response);
        }

//        Headers responseHeaders = response.headers();
//        System.out.println("---------header info---------");
//        for (int i = 0; i < responseHeaders.size(); i++) {
//            System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
//        }
//        System.out.println("---------header info---------");

        System.out.println(response.body().string());
    }
}
