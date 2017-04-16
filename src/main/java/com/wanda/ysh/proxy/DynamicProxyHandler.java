package com.wanda.ysh.proxy;
 
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
 
public class DynamicProxyHandler implements InvocationHandler {
    private Object proxied;
 
    public DynamicProxyHandler(Object proxied) {
       this.proxied = proxied;
    }
 
    public Object invoke(Object proxy, Method method, Object[] args)
           throws Throwable {
       System.out.println("**** proxy: ****\n" + proxy.getClass()
              + "\nmethod: " + method + "\nargs: " + args);
       if (args != null)
           for (Object arg : args)
              System.out.println("  " + arg);
       return method.invoke(proxied, args);
    }
}