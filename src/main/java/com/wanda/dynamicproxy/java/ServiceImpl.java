package com.wanda.dynamicproxy.java;

public class ServiceImpl implements Service {

	@Override
    public void service(String arg) {
		
		System.out.println("service is called,arg : "+arg);

	}

	
    @Override
    public void show(String arg) {
        System.out.println("show is called,arg : "+arg);
    }

}
