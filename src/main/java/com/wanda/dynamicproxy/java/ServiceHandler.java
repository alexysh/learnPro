package com.wanda.dynamicproxy.java;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class ServiceHandler implements InvocationHandler {

	private Object target ;
	
	public ServiceHandler(Object target){
		this.target = target;
	}
	
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
	    PerformanceMonitor.begin();
		Object obj = method.invoke(target, args);
		PerformanceMonitor.end();
		return obj;
	}

}
