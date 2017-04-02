/**
 * 
 */
package com.wanda.ysh.proxy.jdk;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * java动态代理
 * 缺点：JDK的动态代理依靠接口实现，如果有些类并没有实现接口，则不能使用JDK代理
 * @author YANGSHH
 *
 */
public class BookFacadeProxy implements InvocationHandler {

	private Object target; 
	
	 /** 
     * 绑定委托对象并返回一个代理类 
     * @param target 
     * @return 
     */  
    public Object getInstance(Object target) {  
        this.target = target;  
        //取得代理对象，要绑定接口(这是一个缺陷，cglib弥补了这一缺陷)
        return Proxy.newProxyInstance(target.getClass().getClassLoader()
        		,target.getClass().getInterfaces()
        		, this);     
    } 
    
    /** 
     * 调用方法 
     */
    public Object invoke(Object proxy, Method method, Object[] args)  
            throws Throwable {  
        Object result=null;  
        System.out.println("事务开始");  
        //执行方法  
        result=method.invoke(target, args);  
        System.out.println("事务结束");  
        return result;  
    }  
  

}
