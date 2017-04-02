/*
 * Copyright 2012-2016 Ffan.com All right reserved. This software is the
 * confidential and proprietary information of Ffan.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Ffan.com.
 */
package com.wanda.ysh.fastjson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * 类FastJsonMain.java的实现描述：TODO 类实现描述 
 * @author yangshihong Nov 8, 2016 7:41:14 PM
 */
public class FastJsonMain {

    /**
     * @param args
     */
    public static void main(String[] args) {
        JSONObject data = new JSONObject();
        JSONObject json1 = new JSONObject();
        json1.put("message", "您已经绑定了该车牌，不需要再绑定");
        json1.put("status", 2040);
        JSONObject json2 = new JSONObject();
        json2.put("message", "您已经绑定了该车牌，不需要再绑定");
        json2.put("status", 2040);
        JSONObject json3 = new JSONObject();
        json3.put("message", "绑定车牌超过上限");
        json3.put("status", 2037);
//        data.put("皖AJ1N771", "{\"message\":\"您已经绑定了该车牌，不需要再绑定\",\"status\":2040}");
//        data.put("京Q00001", "{\"message\":\"您已经绑定了该车牌，不需要再绑定\",\"status\":2040}");
//        data.put("京Q00003", "{\"message\":\"绑定车牌超过上限\",\"status\":2037}");
        data.put("皖AJ1N771", json1);
        data.put("京Q00001", json1);
        data.put("京Q00003", json3);
        JSONObject result = new JSONObject();
        result.put("message", "绑定车牌超过上限");
        result.put("status", 2037);
        result.put("data", data);
        System.out.println(result.toJSONString());
        System.out.println(JSON.toJSONString(result, SerializerFeature.DisableCircularReferenceDetect));

    }

}
