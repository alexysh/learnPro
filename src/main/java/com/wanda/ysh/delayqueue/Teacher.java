/*
 * Copyright 2012-2014 Wanda.cn All right reserved. This software is the
 * confidential and proprietary information of Wanda.cn ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Wanda.cn.
 */
package com.wanda.ysh.delayqueue;

import java.util.concurrent.DelayQueue;

/**
 * 类Teacher.java的实现描述：TODO 类实现描述 
 * @author yangshihong Jul 29, 2016 9:31:15 AM
 */
public class Teacher implements Runnable {

    private DelayQueue<Student> students;
    public Teacher(DelayQueue<Student> students){
        this.students = students;
    }
    
    @Override
    public void run() {
        try {
            System.out.println(" test start");
            while(!Thread.interrupted()){
                students.take().run();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
