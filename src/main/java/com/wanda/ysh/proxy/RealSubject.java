package com.wanda.ysh.proxy;
 
public class RealSubject implements Subject {
 
    public void operation1() {
       System.out.println("Realer do operation1");
    }
 
    public void operation2(String arg) {
       System.out.println("Realer do operation2 with " + arg);
    }
}