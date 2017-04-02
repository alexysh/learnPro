/*
 * Copyright 2012-2014 Wanda.cn All right reserved. This software is the
 * confidential and proprietary information of Wanda.cn ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Wanda.cn.
 */
package com.wanda.ysh.concurrent.lock;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 类ReentrantLockTest.java的实现描述：TODO 类实现描述 
 * @author yangshihong Mar 14, 2016 1:40:59 PM
 */
public class ReentrantLockTest {
    Map<String, Object> cacheData = new HashMap<String, Object>();
    private volatile boolean cacheValid;
    private ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();

    /**
     * 根据key获取数据
     * @param key
     * @return
     */
    public Object getData(String key){ 
        Object value = null;  
        try{  
            rwLock.readLock().lock();//当线程开始读时，首先开始加上读锁  
            value = cacheData.get(key);  
        }finally{  
            rwLock.readLock().unlock();//释放读锁  
        }  
        return value;  
    }  
    /**
     * 根据key获取数据，不存在则时set
     * @param key
     * @return
     */
    public Object getDataAndSet(String key){  
        Object value = null;  
        try{  
            rwLock.readLock().lock();//当线程开始读时，首先开始加上读锁  
            value = cacheData.get(key);//获取值  
            if(value == null){//判断是否存在值  
                try{  
                    rwLock.readLock().unlock();//在开始写之前，首先要释放写锁，否则写锁无法拿到  
                    rwLock.writeLock().lock();//获取写锁开始写数据  
                    /**再次判断该值是否为空，因为如果两个写线程如果都阻塞在这里，当一个线程  
                     * 被唤醒后value的值不为null，当另外一个线程也被唤醒如果不判断就会执
                     * 行两次写  */
                    if(value == null){
                        value = "v_"+key;  
                        cacheData.put(key, value);  
                    }  
                    rwLock.readLock().lock();//读完之后重入降级为读锁  
                }finally{  
                    rwLock.writeLock().unlock();//最后释放写锁  
                }  
            }  
        }finally{  
            rwLock.readLock().unlock();//释放读锁  
        }  
        return value;  
    }  

    public void refreshCache() {
        try {
            rwLock.readLock().lock();
            if (!cacheValid) {
                // Must release read lock before acquiring write lock
                rwLock.readLock().unlock();
                rwLock.writeLock().lock();
                // Recheck state because another thread might have acquired
                // write lock and changed state before we did.
                if (!cacheValid) {
                    cacheData.put("init", "test data");
                    cacheValid = true;
                }
                // Downgrade by acquiring read lock before releasing write lock
                rwLock.readLock().lock();
                rwLock.writeLock().unlock(); // Unlock write, still hold read
            }
        } finally {
            rwLock.readLock().unlock();
        }
    }
    
}
