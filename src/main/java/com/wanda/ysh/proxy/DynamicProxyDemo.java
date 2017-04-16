package com.wanda.ysh.proxy;
 
import java.lang.reflect.Proxy;
import com.wanda.ysh.proxy.Subject;
import com.wanda.ysh.proxy.RealSubject;
 
public class DynamicProxyDemo {
    public static void consumer(Subject subject) {
       subject.operation1();
       subject.operation2("ZJ");
    }
   
    public static void main(String[] args) {
        RealSubject real = new RealSubject();
       System.out.println("===Without Proxy===");
       consumer(real);
       System.out.println("===Use Proxy===");
       Subject proxy = (Subject) Proxy.newProxyInstance(Subject.class
              .getClassLoader(), new Class[] { Subject.class },
              new DynamicProxyHandler(real));
       consumer(proxy);
    }
}