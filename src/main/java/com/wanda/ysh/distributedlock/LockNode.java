/*
 * Copyright 2012-2016 Ffan.com All right reserved. This software is the
 * confidential and proprietary information of Ffan.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Ffan.com.
 */
package com.wanda.ysh.distributedlock;
/**
 * 类LockNode.java的实现描述：ZK节点类 
 * @author yangshihong Mar 2, 2017 10:07:33 AM
 */
public class LockNode  implements Comparable<LockNode>{
    
    private String id;
    private String name;

    
    public LockNode(String id) {
        this.id = id;
    }
    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    @Override
    public int compareTo(LockNode node) {
        return node.getId().hashCode() - this.getId().hashCode();
    }

}
