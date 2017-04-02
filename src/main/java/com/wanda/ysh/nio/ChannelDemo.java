/*
 * @(#)ChannelDemo.java 2014-8-7 上午09:07:15
 * MavenTest
 * Copyright 2014 Thuisoft, Inc. All rights reserved.
 * THUNISOFT PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
/**
 * 
 */
package com.wanda.ysh.nio;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import junit.framework.TestCase;


/**
 * @author Administrator
 *
 */
public class ChannelDemo extends TestCase{
    

    public void UtestChannel() throws IOException {
//        RandomAccessFile aFile = new RandomAccessFile("data/nio-data.txt", "rw");
        FileInputStream aFile = new FileInputStream("data/nio-data.txt");
        FileChannel inChannel = aFile.getChannel();

        ByteBuffer buf = ByteBuffer.allocate(48);

        int bytesRead = inChannel.read(buf);
        while (bytesRead != -1) {
            System.out.println("Read " + bytesRead);
            buf.flip();
            while (buf.hasRemaining()) {
                System.out.print((char) buf.get());
            }

            buf.clear();
            bytesRead = inChannel.read(buf);
        }
        aFile.close();
    }
    
    public void wtestScatterAndGather() throws IOException {
        
        FileInputStream aFile = new FileInputStream("scatterAndGather.txt");
        FileChannel channel = aFile.getChannel();
        
        ByteBuffer header = ByteBuffer.allocate(8);
        ByteBuffer body   = ByteBuffer.allocate(10);
        header.put("12345678".getBytes());
        body.put("1234567890".getBytes());
        ByteBuffer[] bufferArray = { header, body };
//        channel.read(bufferArray);
        channel.write(bufferArray);
        header.clear();
        body.clear();
        aFile.close();
    }
    
    protected void testNIO() throws Exception
    {  
        int size = 10;
        ExecutorService workerpool = Executors.newFixedThreadPool(size);
    
      System.out.println("Start: " + System.currentTimeMillis());
        
        for(int i = 0; i < 100; i++)
        {
            workerpool.execute(new Worker());
            
            // send out every 10ms
            Thread.sleep(100);
        }
    }
    
    private class Worker implements Runnable
    {
        public void run()
        {
            int busyTime = 10;  
            int idleTime = busyTime;  
            long startTime = 0;
            int i = 0;
              
            while (i < 50) {  
                startTime = System.currentTimeMillis();  
                // busy loop  
                while ((System.currentTimeMillis() - startTime) <= busyTime)  
                    ;  
                // idle loop  
                try {  
                    Thread.sleep(idleTime);  
                } catch (InterruptedException e) {  
                    System.out.println(e);  
                }  
                
                i ++;
            } 
            
            System.out.println("Finish: " + System.currentTimeMillis());
       }  
    }  
}

