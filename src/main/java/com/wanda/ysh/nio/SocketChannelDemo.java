/*
 * @(#)SocketChannelDemo.java 2014-8-11上午10:45:26
 * MavenTest
 * Copyright 2014 Thuisoft, Inc. All rights reserved.
 * THUNISOFT PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.wanda.ysh.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * SocketChannelDemo
 * @author yangshh
 * @version 1.0
 *
 */
public class SocketChannelDemo {

    /**
     * @param args
     */
    public static void main(String[] args) {

        SocketChannelDemo demo = new SocketChannelDemo();
        demo.connectServer();
    }
    
    public void connectServer() {
        SocketChannel socketChannel = null;
        try {
            socketChannel = SocketChannel.open();
            socketChannel.connect(new InetSocketAddress("127.0.0.1", 9999));
            
            String newData = "New String to write to file..." + System.currentTimeMillis();
            ByteBuffer buf = ByteBuffer.allocate(48);
            buf.clear();
            buf.put(newData.getBytes("GBK"));
            buf.flip();
            socketChannel.write(buf);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(socketChannel !=null) {
                try {
                    socketChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
