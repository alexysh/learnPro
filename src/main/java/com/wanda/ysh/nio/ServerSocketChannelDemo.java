/*
 * @(#)ServerSocketChannelDemo.java 2014-8-11上午10:38:40
 * MavenTest
 * Copyright 2014 Thuisoft, Inc. All rights reserved.
 * THUNISOFT PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.wanda.ysh.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * ServerSocketChannelDemo
 * @author yangshh
 * @version 1.0
 *
 */
public class ServerSocketChannelDemo {

    /**
     * @param args
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException {
        int counter = 0;
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(9999));
//        serverSocketChannel.configureBlocking(false);
        while(true){
            if(counter>10) {
                break;
            }
            SocketChannel socketChannel = serverSocketChannel.accept();
            ByteBuffer buffer = ByteBuffer.allocate(128);
            socketChannel.read(buffer);
            buffer.flip();
            byte[] aryByte = buffer.array();
            System.out.println(new String(aryByte,"GBK"));
            
            buffer.clear();
            buffer.put("Hello Client,I'm Server!".getBytes());
            socketChannel.write(buffer);
            
            counter++;
        }
        serverSocketChannel.close();
    }

}
