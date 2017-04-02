/*
 * Copyright 2012-2014 Wanda.cn All right reserved. This software is the
 * confidential and proprietary information of Wanda.cn ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Wanda.cn.
 */
package com.wanda.ysh.filemonitor;
/**
 * 类FileDirectoryMonitor.java的实现描述：TODO 类实现描述 
 * @author yangshihong May 25, 2016 3:04:10 PM
 */
import java.io.IOException;     
import java.nio.file.FileSystems;     
import java.nio.file.Path;     
import java.nio.file.Paths;     
import java.nio.file.WatchEvent;     
import java.nio.file.WatchKey;     
import java.nio.file.WatchService;     
import static java.nio.file.StandardWatchEventKinds.*;     
    
/**    
 * WatchService API使用实例
 * @author yangshihong   
 */    
public class FileDirectoryMonitor {     
         
    private WatchService watcher;     
         
    public FileDirectoryMonitor(Path path)throws IOException{     
        watcher = FileSystems.getDefault().newWatchService();     
        path.register(watcher, ENTRY_CREATE,ENTRY_DELETE,ENTRY_MODIFY);     
    }     
         
    public void handleEvents() throws InterruptedException{     
        while(true){     
            WatchKey key = watcher.take();     
            for(WatchEvent event : key.pollEvents()){     
                WatchEvent.Kind kind = event.kind();     
                //事件可能lost or discarded
                if(kind == OVERFLOW){     
                    continue;     
                }     
                     
                WatchEvent e = (WatchEvent)event;     
                Path fileName = (Path)e.context();     
                     
                //System.out.printf("Event %s has happened,which fileName is %s%n"    
                  //      ,kind.name(),fileName);
                if(kind.name() == "ENTRY_CREATE")
                {
                    System.out.println(fileName + "-----> 创建！---->"+kind.name());
                }else if(kind.name() == "ENTRY_DELETE" ){
                    System.out.println(fileName + "-----> 删除！---->"+kind.name());
                }else if(kind.name() == "ENTRY_MODIFY"){
                    System.out.println(fileName + "-----> 修改！---->"+kind.name());
                }
//                System.out.printf("%s -----> %s%n",fileName,kind.name());
            }     
            if(!key.reset()){     
                break;     
            }     
        }     
    }     
         
    public static void main(String args[]) throws IOException, InterruptedException{     
        /*
        if(args.length!=1){     
            System.out.println("请设置要监听的文件目录作为参数");     
            System.exit(-1);     
        }   */
        String watchDir = "E:/test";
        new FileDirectoryMonitor(Paths.get(watchDir)).handleEvents();     
    }     
}   