/**
 * 
 */
package com.wanda.ysh.proxy.cglib;

/**
 * @author Administrator
 *
 */
public class TestClgib {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
        BookFacadeCglib cglib=new BookFacadeCglib();  
        BookFacade bookCglib=(BookFacade)cglib.getInstance(new BookFacade());  
        bookCglib.addBook();  

	}

}
