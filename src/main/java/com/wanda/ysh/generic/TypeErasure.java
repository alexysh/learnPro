package com.wanda.ysh.generic;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

/**
 * Created by yangshihong on 2017/6/16.
 */
public class TypeErasure {

    public static void main1(String[] args) throws IllegalAccessException, NoSuchMethodException,
            InvocationTargetException {
        System.out.println("aaa");
        test2();

    }

    public static void test1() {

        ArrayList<String> arrayList1 = new ArrayList<String>();
          arrayList1.add("abc");
        ArrayList<Integer> arrayList2 = new ArrayList<Integer>();
        arrayList2.add(123);
        System.out.println(arrayList1.getClass() == arrayList2.getClass());
    }

    public static void test2()
            throws IllegalArgumentException, SecurityException,
            IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        ArrayList<Integer> arrayList3 = new ArrayList<Integer>();
        arrayList3.add(1);//这样调用add方法只能存储整形，因为泛型类型的实例为Integer
        arrayList3.getClass().getMethod("add", Object.class).invoke(arrayList3, "asd");
        for (int i = 0; i < arrayList3.size(); i++) {
            System.out.println(arrayList3.get(i));
        }
    }

    public static void test3 (){
        /**不指定泛型的时候*/
        int i= add(1, 2); //这两个参数都是Integer，所以T为Integer类型
        Number f= add(1, 1.2);//这两个参数一个是Integer，以风格是Float，所以取同一父类的最小级，为Number
        Object o= add(1, "asd");//这两个参数一个是Integer，以风格是Float，所以取同一父类的最小级，为Object

        /**指定泛型的时候*/
        int a=TypeErasure.<Integer>add(1, 2);//指定了Integer，所以只能为Integer类型或者其子类
//        int b=TypeErasure.<Integer>add(1, 2.2f);//编译错误，指定了Integer，不能为Float
        Number c=TypeErasure.<Number>add(1, 2.2); //指定为Number，所以可以为Integer和Float
    }

    //这是一个简单的泛型方法
    public static <T> T add(T x,T y){
        return y;
    }
}
