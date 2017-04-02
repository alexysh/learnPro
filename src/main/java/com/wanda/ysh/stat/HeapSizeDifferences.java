///*
// * Copyright 2012-2014 Wanda.cn All right reserved. This software is the
// * confidential and proprietary information of Wanda.cn ("Confidential
// * Information"). You shall not disclose such Confidential Information and shall
// * use it only in accordance with the terms of the license agreement you entered
// * into with Wanda.cn.
// */
//package com.wanda.ysh.stat;
//
//import java.lang.management.ManagementFactory;
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.List;
//
///**
// * 类HeapSizeDifferences.java的实现描述：TODO 类实现描述 
// * @author yangshihong May 4, 2015 9:43:44 AM
// */
//public class HeapSizeDifferences {
//
//    static Collection objects = new ArrayList();
//    static long lastMaxMemory = 0;
//
//    public static void main(String[] args) {
//      try {
//        List<String> inputArguments = ManagementFactory.getRuntimeMXBean().getInputArguments();
//        System.out.println("Running with: " + inputArguments);
//        while (true) {
//          printMaxMemory();
//          consumeSpace();
//        }
//      } catch (OutOfMemoryError e) {
//        freeSpace();
//        printMaxMemory();
//      }
//    }
//
//    static void printMaxMemory() {
//      long currentMaxMemory = Runtime.getRuntime().maxMemory();
//      if (currentMaxMemory != lastMaxMemory) {
//        lastMaxMemory = currentMaxMemory;
//        System.out.format("Runtime.getRuntime().maxMemory(): %,dK.%n", currentMaxMemory / 1024);
//      }
//    }
//
//    static void consumeSpace() {
//      objects.add(new int[1000000]);
//    }
//
//    static void freeSpace() {
//      objects.clear();
//    }
//}
