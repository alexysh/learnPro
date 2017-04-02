package com.wanda.dynamicproxy.java;

import java.lang.reflect.Proxy;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
        Service service = new ServiceImpl();
        ServiceHandler serviceHandler = new ServiceHandler(service);
        Service proxy = (Service) Proxy.newProxyInstance(service.getClass()
                .getClassLoader(), service.getClass().getInterfaces(),
            serviceHandler);

        proxy.service("1111111111111");
        proxy.show("1111111111111");
	}

}
