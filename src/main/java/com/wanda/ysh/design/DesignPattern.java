/*
 * Copyright 2012-2014 Wanda.cn All right reserved. This software is the
 * confidential and proprietary information of Wanda.cn ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Wanda.cn.
 */
package com.wanda.ysh.design;

import java.io.IOException;

/**
 * 类DesignPattern.java的实现描述：TODO 类实现描述 
 * @author yangshihong Jan 18, 2016 12:07:51 PM
 */
public class DesignPattern {

    /**
     * @param args
     */
    public static void main(String[] args) {

        try {
            Prototype pt = new Prototype("pname", new SerializableObject("serialName"));
            Prototype ptClone = (Prototype) pt.clone();
            Prototype ptDeep = (Prototype) pt.deepClone();
            pt.getObj().setSerialName("changeValue");
            System.out.println("ptClone->value:"+ptClone.getObj().getSerialName());
            System.out.println("ptDeep->value:"+ptDeep.getObj().getSerialName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

}
