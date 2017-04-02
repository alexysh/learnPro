/**
 * 
 */
package com.wanda.ysh.proxy.jdk;


/**
 * @author Administrator
 *
 */
public class BookFacadeImpl implements BookFacade {

	private String str = getClass().toString();
	public void addBook() {
		
		System.out.println("增加图书方法。。。"+str);  

	}

}
