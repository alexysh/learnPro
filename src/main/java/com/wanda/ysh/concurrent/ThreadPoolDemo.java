/*
 * Copyright 2012-2014 Wanda.cn All right reserved. This software is the
 * confidential and proprietary information of Wanda.cn ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Wanda.cn.
 */
package com.wanda.ysh.concurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * 类ThreadPoolDemo.java的实现描述：TODO 类实现描述 
 * @author yangshihong May 12, 2015 3:32:41 PM
 */
public class ThreadPoolDemo {

    /**
     * @param args
     */
    public static void main(String[] args) {
        ThreadPoolDemo tp = new ThreadPoolDemo();
        tp.unboundedThreadPool();
//        try {
//            tp.test();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }

    /**
     * 无界队列:使用无界队列（例如，不具有预定义容量的 LinkedBlockingQueue）将导致在所有 corePoolSize
     * 线程都忙时新任务在队列中等待。这样，创建的线程就不会超过 corePoolSize。（因此，maximumPoolSize的值
     * 也就无效了。）当每个任务完全独立于其他任务，即任务执行互不影响时，适合于使用无界队列；例如，在 Web页服务器中。
     * 这种排队可用于处理瞬态突发请求，当命令以超过队列所能处理的平均数连续到达时，此策略允许无界线程具有增长的可能性。
     */
    public  void unboundedThreadPool() {
        int recycles = 5;
        ExecutorService threadPool = new ThreadPoolExecutor(4, 4,3000L, 
                TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
        List<Future<String>> result = new ArrayList<Future<String>>();
        for (int index=1;index <=recycles;index++) {
//            threadPool.submit(new Thread());
//            Future t = threadPool.submit(
//                    new Runnable() {
//                        public void run() {
//                            try {
//                                Thread.sleep(5000);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//                    );
//            Object tt = null;
//            try {
//                tt = t.get();
//                System.out.println("xxxx");
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            } catch (ExecutionException e) {
//                e.printStackTrace();
//            }
            Future<String> future = threadPool.submit(new CallableImpl(String.valueOf(index)));
            result.add(future);
        }
        
        try {
            for (Future<String> future : result) {
                System.out.println(future.get());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        threadPool.shutdown();
    }
    public  void boundedThreadPool() {
        int recycles = 4;
        ExecutorService threadPool = new ThreadPoolExecutor(1, 2,0L, 
                TimeUnit.MILLISECONDS,  new ArrayBlockingQueue<Runnable>(2));
        List<Future<String>> result = new ArrayList<Future<String>>();
        for (int index=1;index <=recycles;index++) {
            result.add(threadPool.submit(new CallableImpl(String.valueOf(index))));
        }
        threadPool.shutdown();
    }
    
    public  void directSubmitThreadPool() {
        ExecutorService threadPool =new ThreadPoolExecutor(0, 10,30L, 
                TimeUnit.SECONDS,new SynchronousQueue<Runnable>());
        List<Future<String>> result = new ArrayList<Future<String>>();
        result.add(threadPool.submit(new DelayCallableImpl(String.valueOf(1))));
        result.add(threadPool.submit(new CallableImpl(String.valueOf(2))));
//        result.add(threadPool.submit(new DelayCallableImpl(String.valueOf(3))));
        threadPool.shutdown();
    }

    public void completionService() {
        int recycles = 10;
        ExecutorService executor = Executors.newFixedThreadPool(4);
        CompletionService<String> cs = new ExecutorCompletionService<String>(executor);
        for (int index = 10; index < recycles; index++) {
            cs.submit(new CallableImpl(String.valueOf(index)));
        }
        try {
            for (int index = 0; index < recycles; index++) {
                Future<String> future;
                future = cs.take();
                System.out.println(future.get());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        executor.shutdown();
    }
    
    public  void newFixedThreadPool() {
        int recycles = 10;
        ExecutorService threadPool = Executors.newFixedThreadPool(2);
        List<Future<String>> result = new ArrayList<Future<String>>();
        for (int index=10;index <recycles;index++) {
            
            result.add(threadPool.submit(new CallableImpl(String.valueOf(index))));
        }
        threadPool.shutdown();
        try {
            for (Future<String> future:result) {
                System.out.println(future.get());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
    public  void newScheduledThreadPool() {
        int recycles = 10;
        ExecutorService threadPool = Executors.newScheduledThreadPool(5);
        for (int index=0;index <recycles;index++) {
            
            threadPool.submit(new CallableImpl(String.valueOf(index)));
        }
        threadPool.shutdown();
    }


    public void scheduledThreadPool() {
        ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(2);
        exec.scheduleAtFixedRate(new Runnable() {// 每隔一段时间就触发异常
                    @Override
                    public void run() {
                        // throw new RuntimeException();
                        System.out.println("================");
                    }
                }, 1000, 4000, TimeUnit.MILLISECONDS);
        exec.scheduleAtFixedRate(new Runnable() {// 每隔一段时间打印系统时间，证明两者是互不影响的
                    @Override
                    public void run() {
                        System.out.println(System.nanoTime());
                    }
                }, 1000, 2000, TimeUnit.MILLISECONDS);
    }
    
    
    public  void test() throws InterruptedException { 
        BlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(4); //固定为4的线程队列
        ThreadPoolExecutor executor = new ThreadPoolExecutor(2, 6, 10, TimeUnit.SECONDS, queue);
        for (int i = 0; i < 10; i++) {   
            executor.execute(new Thread(new ThreadPoolTest(), "TestThread".concat(""+i)));   
            int threadSize = queue.size();
            System.out.println("线程队列大小为-->"+threadSize);
            if (threadSize==4){
                System.out.println("--i-->"+i);
                queue.put(new Runnable() {
                    @Override
                    public void run(){
                        System.out.println("我是新线程，看看能不能搭个车加进去！");
                    }
                });
            }
        }   
        executor.shutdown();  
    }
    
    class ThreadPoolTest implements Runnable {
        public void run() {
            synchronized (this) {
                try {
                    System.out.println(Thread.currentThread().getName());
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}
