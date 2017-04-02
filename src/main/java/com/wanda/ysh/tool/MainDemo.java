/*
 * Copyright 2012-2014 Wanda.cn All right reserved. This software is the
 * confidential and proprietary information of Wanda.cn ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Wanda.cn.
 */
package com.wanda.ysh.tool;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import joptsimple.internal.Strings;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


/**
 * 类MainDemo.java的实现描述：TODO 类实现描述 
 * @author yangshihong Feb 11, 2015 4:56:49 PM
 */
public class MainDemo {
    /**
     * @param args
     * @throws ClassNotFoundException 
     * @throws UnsupportedEncodingException 
     */
    public static void main(String[] args) throws ClassNotFoundException, UnsupportedEncodingException {
        MainDemo.generateTradeTableSuffix();
        String devInfo = "%7B%22locationProvince%22%3A%22%E5%B1%B1%E8%A5%BF%E7%9C%81%22%2C%22locationY%22%3A%22112.934342%22%2C%22IP%22%3A%22192.168.1.110%22%2C%22wifiMac%22%3A%2294%3Ac%3A6d%3A36%3Abb%3A16%22%2C%22network_desc%22%3A%22WIFI%22%2C%22locationDistrict%22%3A%22%E9%AB%98%E5%B9%B3%E5%B8%82%22%2C%22device_id%22%3A%22421f916110a2b84ace8270c79a69faeb00b3199d%22%2C%22locationCity%22%3A%22%E6%99%8B%E5%9F%8E%E5%B8%82%22%2C%22sourceFrom%22%3A%22APP%22%2C%22wifi%22%3A%22%E2%88%9A%E2%80%B9%C2%AC%C3%8E%C2%A0%C2%AB%C2%A0%E2%89%A4%E2%88%9A%C2%A5%22%2C%22size%22%3A%22375%2A667%22%2C%22Os_type%22%3A%22IOS%22%2C%22device_desc%22%3A%22iPhone8%2C1%22%2C%22mac%22%3A%22421f916110a2b84ace8270c79a69faeb00b3199d%22%2C%22locationX%22%3A%2235.791903%22%2C%22locationAddress%22%3A%22%E4%B8%AD%E5%9B%BD%20%E5%B1%B1%E8%A5%BF%E7%9C%81%20%E6%99%8B%E5%9F%8E%E5%B8%82%20%E9%AB%98%E5%B9%B3%E5%B8%82%20%E9%AB%98%E5%B9%B3%E5%B8%82%22%2C%22network%22%3A%22WIFI%22%2C%22GPS%22%3A%2235.791903%2C112.934342%22%2C%22os_version%22%3A%22iOS%2010.1.1%22%2C%22screenSize%22%3A%22375%2A667%22%2C%22gmtTime%22%3A%222017-01-11%2023%3A50%3A23%22%7D";
        String devInfoEncode = URLDecoder.decode(devInfo, "UTF-8");
        BaseRiskInfo result = new BaseRiskInfo();
        result.setDevInfo(devInfoEncode);
        JSONObject devObj = JSONObject.parseObject(devInfoEncode);
        if (null != devObj) {
            String sourceFrom = devObj.getString("sourceFrom");
            if (!Strings.isNullOrEmpty(sourceFrom) 
                    && sourceFrom.equalsIgnoreCase("APP")) {

                result.setChannel(1);
            } else {

                result.setChannel(2);
            }
        }
        JSONObject.toJSONString(result);
        String sqlStr = "DELETE from test";
        Pattern dtPattern = Pattern.compile("(delete|update|drop|truncate|insert)",Pattern.CASE_INSENSITIVE);
        Matcher dtMatcher = dtPattern.matcher(sqlStr);
        boolean isContainDeleteOrUpdateOrDropOrtruncate = false;
        if (dtMatcher.find()) {
            isContainDeleteOrUpdateOrDropOrtruncate = true;
        }
        System.out.println("isContainDeleteOrUpdateOrDropOrtruncate:"+isContainDeleteOrUpdateOrDropOrtruncate);
        System.out.println("(([a01233,b|4566]".replaceAll("(\\[|\\])", ""));
        String[] aryPN = "".split(",");
        Integer.parseInt("20161207");
        System.out.println(aryPN.toString());
        int dataRetainMonth = 1;
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1*dataRetainMonth);//
        cal.set(Calendar.DAY_OF_MONTH,1);//设置为指定月第一天 
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        int endInTime = ToolUtils.getDateSeconds(cal.getTime());
        System.out.println("endInTime:"+ToolUtils.formatDatetimeString(cal.getTime()));
        System.out.println("endInTime:"+endInTime);
        

        net.sf.json.JSONObject pf = new net.sf.json.JSONObject();
        pf.put("payedPrice", "0.0");
        pf.put("totalPrice", "20.5");
        pf.put("unPayPrice", "20.5");
        
        net.sf.json.JSONObject sfjson= new net.sf.json.JSONObject();
        com.alibaba.fastjson.JSONObject fastjson = new com.alibaba.fastjson.JSONObject();
        com.alibaba.fastjson.JSONObject fastjson2 = new com.alibaba.fastjson.JSONObject();
        sfjson.put("data", pf.toString());
        fastjson.put("data", pf.toString());
        fastjson2.put("data", pf);
        System.out.println(sfjson);
        System.out.println(fastjson);
        System.out.println(fastjson2);
        
        String str = "leftPad($a$ % 4, 3)";
        System.out.println(handleVariableReplacement(str));
        
    }
    private static final Pattern VARABLE_SEGMENT = Pattern.compile("\\$.*?\\$");
    
    private static String handleVariableReplacement(String expression) {
      Matcher matcher = VARABLE_SEGMENT.matcher(expression);
      StringBuilder builder = new StringBuilder();
      int cursor = 0;
      while (matcher.find(cursor)) {
          String var = matcher.group();
          var = var.substring(1, var.length() - 1);
          builder.append(expression.substring(cursor, matcher.start()));
          builder.append(parseTakeVarExp(var));
          cursor = matcher.end();
      }
      builder.append(expression.substring(cursor));
      return builder.toString();

  }
    
    private static String parseTakeVarExp(String var) {
        return "(map.get(\"" + StringUtils.trim(var).toUpperCase() + "\"))";
    }
    
    
    public static String  generateTradeTableSuffix() {
        String tradeNo = "11532724284564ab";
        String tableNameSuffix = "";
        tradeNo = tradeNo == null ? null : tradeNo.trim();
        if (tradeNo != null) {
            long nan = 0;
            StringBuilder sb = new StringBuilder("0");
            for (char c : String.valueOf(tradeNo).toCharArray()) {
                if (Character.isDigit(c))
                    sb.append(c);
                else
                    nan += c;
            }
            long lid = new BigDecimal(sb.toString()).longValue();
            if (nan > 0) lid += 64 + nan;
            tableNameSuffix = (Math.abs(lid) % 64) + 1 + "";
        }
        
        System.out.println("tableNameSuffix:"+tableNameSuffix);
        return tableNameSuffix;
    }
    
}

class BaseRiskInfo {
    
    /**（风控分配，固定写死） */
    private String checkpoint;

    /** 用户id */
    private String uid;

    /** 用户puid */
    private String puid;

    /** 设备信息json串（微信不传） */
    private String devInfo;

    /** 设备指纹（微信不传） */
    private String siedc;

    /** 操作时间（毫秒） */
    private long operateTime;

    /** 渠道      APP:1   微信公众号：2 */
    private Integer channel;

    /** 车牌号码 */
    private String vin;

    /** 手机号 */
    private String phone;
    
    public String getCheckpoint() {
        return checkpoint;
    }

    public void setCheckpoint(String checkpoint) {
        this.checkpoint = checkpoint;
    }
    
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
    
    public String getPuid() {
        return puid;
    }

    public void setPuid(String puid) {
        this.puid = puid;
    }
    
    public String getDevInfo() {
        return devInfo;
    }

    public void setDevInfo(String devInfo) {
        this.devInfo = devInfo;
    }
    
    public String getSiedc() {
        return siedc;
    }

    public void setSiedc(String siedc) {
        this.siedc = siedc;
    }
    
    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }
    
    public long getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(long operateTime) {
        this.operateTime = operateTime;
    }
    
    public Integer getChannel() {
        return channel;
    }

    public void setChannel(Integer channel) {
        this.channel = channel;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    //
}