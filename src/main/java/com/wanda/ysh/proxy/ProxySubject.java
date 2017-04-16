package com.wanda.ysh.proxy;
 
public class ProxySubject implements Subject {
    private Subject proxied;// ���������
 
    public ProxySubject(Subject proxied) {
       this.proxied = proxied;
    }
 
    public void operation1() {
       System.out.println("Proxyer do operation1");
       proxied.operation1();
    }
 
    public void operation2(String arg) {
       System.out.println("Proxyer do operation2 with " + arg);
       proxied.operation2(arg);
    }
}