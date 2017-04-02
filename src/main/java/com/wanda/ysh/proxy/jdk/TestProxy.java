/**
 * 
 */
package com.wanda.ysh.proxy.jdk;


/**
 * @author Administrator
 *
 */
public class TestProxy {

	public static void main(String[] args) {  
		
        BookFacadeProxy proxy = new BookFacadeProxy();  
        BookFacade bookProxy = (BookFacade) proxy.getInstance(new BookFacadeImpl());  
        bookProxy.addBook();  
    }  

}
