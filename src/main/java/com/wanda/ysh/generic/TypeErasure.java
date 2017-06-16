package com.wanda.ysh.generic;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

/**
 * Created by yangshihong on 2017/6/16.
 */
public class TypeErasure {

    public static void main(String[] args) {

        test1();
    }

    public static void test1() {

        ArrayList<String> arrayList1 = new ArrayList<String>();
        arrayList1.add("abc");
        ArrayList<Integer> arrayList2 = new ArrayList<Integer>();
        arrayList2.add(123);
        System.out.println(arrayList1.getClass() == arrayList2.getClass());
    }

    public static void test2(String[] args)
            throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        ArrayList<Integer> arrayList3 = new ArrayList<Integer>();
        arrayList3.add(1);//这样调用add方法只能存储整形，因为泛型类型的实例为Integer
        arrayList3.getClass().getMethod("add", Object.class).invoke(arrayList3, "asd");
        for (int i = 0; i < arrayList3.size(); i++) {
            System.out.println(arrayList3.get(i));
        }
    }
}
