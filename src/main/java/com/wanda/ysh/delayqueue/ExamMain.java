/*
 * Copyright 2012-2014 Wanda.cn All right reserved. This software is the
 * confidential and proprietary information of Wanda.cn ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Wanda.cn.
 */
package com.wanda.ysh.delayqueue;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.DelayQueue;

/**
 * 类ExamMain.java的实现描述：TODO 类实现描述 
 * @author yangshihong Jul 29, 2016 9:27:09 AM
 */
public class ExamMain {

    /**
     * @param args
     */
    public static void main(String[] args) throws InterruptedException {

        int fixTime = 1;//30
        int endTime = 5;//120
        int studentNumber = 20;
        CountDownLatch countDownLatch = new CountDownLatch(studentNumber+1);
        DelayQueue< Student> students = new DelayQueue<Student>();
        Random random = new Random();
        for (int i = 0; i < studentNumber; i++) {
            students.put(new Student("student"+(i+1), fixTime+random.nextInt(endTime),countDownLatch));
        }
        Thread teacherThread =new Thread(new Teacher(students)); 
        students.put(new EndExam(students, endTime,countDownLatch,teacherThread));
        teacherThread.start();
        countDownLatch.await();
        System.out.println("考试时间到，全部交卷！");  
    }
}
