package com.wan.ticket;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TestProduct {

	public String productNo;
	public String title;
	public int productType;

	public static TestProduct getProduct1004() {
		TestProduct p = new TestProduct();
		p.title = "停车券-测试-" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
		p.productType = 1004;

		return p;
	}

}
