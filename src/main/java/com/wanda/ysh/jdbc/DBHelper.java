/*
 * Copyright 2012-2014 Wanda.cn All right reserved. This software is the
 * confidential and proprietary information of Wanda.cn ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Wanda.cn.
 */
package com.wanda.ysh.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 类DBHelper.java的实现描述：TODO 类实现描述 
 * @author yangshihong Jan 27, 2015 3:08:38 PM
 */
public class DBHelper {
    public static final String className = "com.mysql.jdbc.Driver";  
    public static final String url = "jdbc:mysql://10.1.169.16:3333/openapi?characterEncoding=UTF-8&amp;zeroDateTimeBehavior=convertToNull";  
    public static final String user = "openapi";  
    public static final String password = "openapi"; 
//    public static final String url = "jdbc:mysql://10.1.169.16:3333/openapi?characterEncoding=UTF-8&amp;zeroDateTimeBehavior=convertToNull";  
//    public static final String user = "openapi";  
//    public static final String password = "openapi"; 
    public Connection conn = null;
    public PreparedStatement pst = null;

    public DBHelper() {

    }
    public DBHelper(String sql) {
        try {
            Class.forName(className);//指定连接类型
            conn = DriverManager.getConnection(url, user, password);//获取连接
            pst = conn.prepareStatement(sql);//准备执行语句
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection getConn() {
        
        try {
            Class.forName(className);//指定连接类型
            return DriverManager.getConnection(url, user, password);//获取连接
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public  void close() {
        try {
            this.conn.close();
            this.pst.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static void close(Connection conn) {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
