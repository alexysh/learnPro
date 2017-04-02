package com.wan.ticket;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.httpclient.HttpException;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.wanda.apigateway.http.HttpApiParam;

@SuppressWarnings("unchecked")
public class CouponV2Test {

	private static Logger logger = Logger.getLogger(CouponV2Test.class);

	static TestEnv env = TestEnv.testConfig();
	static TestProduct prod = TestProduct.getProduct1004();
	static String memberId = "14102817360501335";
	static int buyCount = 10;
	static int productCount = 40000;
	static int[] limitstores = new int[] { }; 

	static String origPrice = "100";
	static String salePrice = "1";

	static int[][] categories = new int[][] { new int[] { 2, 116, 116 }, new int[] { 1, 68, 68 }, new int[] { 2, 115, 115 },
			new int[] { 2, 114, 114 }, new int[] { 1, 66, 66 }, new int[] { 2, 110, 110 }, new int[] { 2, 51, 51 } };

	static int orderStoreId = 0;

	public static void main(String[] args) throws Exception {
		new CouponV2Test().test();
	}

	static {

		limitstores = new int[] {  };

		if (Double.parseDouble(salePrice) == 0)
			prod.title = "[免费]" + prod.title;
		if (limitstores.length == 0)
			prod.title = "[通用]" + prod.title;

		HttpApiParam map = new HttpApiParam();

		try {
			if (memberId.equals("14102817360501335"))
			// create product
			map.put("type", prod.productType);
			map.put("title", prod.title);
			map.put("origPrice", origPrice);
			map.put("parkingTime", "60");
			map.put("merchantNo", 2075150);
			map.put("merchantName", "武汉七匹狼服装营销有限公司");
			map.put("operatorId", "123");
			map.put("operatorName", "unit_test");
			map.put("description", "商品描述");

			Map<?, ?> newPost = toT(env.post("/v2/products", map), Map.class);
			prod.productNo = newPost.get("data").toString();

			map.put("origPrice", origPrice);
			env.post("/v2/products/" + prod.productNo + "/basic", map);

			map.clear();
			map.put("vendibility", "1");
			map.put("createCertificateType", "1");
			map.put("stockNum", "100");
			map.put("couponNos", "[]");
			map.put("operatorId", "123");
			map.put("operatorName", "unit_test");

			env.post("/v2/products/" + prod.productNo + "/saleRule", map);

			map.clear();

			map.put("validStartTime", System.currentTimeMillis() / 1000 - 300);
			map.put("validEndTime", System.currentTimeMillis() / 1000 + 101 * 24 * 3600);
			map.put("validRelativeTime", "0");
			map.put("remark", "ceshi remark");
			map.put("thresholdOrder", "1");
			map.put("thresholdItem", "2");
			map.put("useNumLimitPerOrder", "0");
			map.put("useNumLimitPerTotal", "0");
			map.put("useNumLimitPerItem", "0");
			if (limitstores.length > 0)
				map.put("limitStores", new ObjectMapper().writeValueAsString(limitstores));
			// map.put("limitProducts", "[\"1\",\"3\",\"4\"]");
			// map.put("limitCategories", "[\"1\",\"2\"]");
			map.put("settlement", "11.1");
			// map.put("storeSettlement", "{\"s1\":23}");
			map.put("operatorId", "123");
			map.put("operatorName", "unit_test");

			env.post("/v2/products/" + prod.productNo + "/useRule", map);

			map.clear();
			map.put("subTitle", "sub");
			map.put("salePrice", salePrice);
			map.put("salePricePoint", salePrice);

			int[] category = categories[(int) (Math.random() * categories.length)];

			map.put("category1", category[0]);
			map.put("category2", category[1]);
			map.put("category3", category[2]);

			map.put("topics", "[\"h00607793c6a4c1b707eaa7440c8be435f2\"]");
			map.put("services", "[1]");
			map.put("buyNotify", "11");
			map.put("webDetail", "11");
			map.put("appDetail", "11");
			map.put("saleStartTime", System.currentTimeMillis() / 1000 - 3600 * 10);
			map.put("saleEndTime", System.currentTimeMillis() / 1000 + 100 * 24 * 3600);
			map.put("memberType", "[1,2,3]");
			map.put("limitPerMember", "0");
			map.put("limitPerOrder", "0");

			map.put("operatorId", "123");
			map.put("operatorName", "unit_test");

			env.post("/v2/products/" + prod.productNo + "/info", map);

			map.clear();
			map.put("source", "1");
			System.out.println(toT(env.get("/v2/products/" + prod.productNo, map), Map.class).get("data"));

			// 增加库存量
			map.clear();
			map.put("totalNum", productCount);
			map.put("source", 1001);
			map.put("operatorId", 10);
			map.put("operatorName", "unit_test_v2");
			map.put("productNo", prod.productNo);
			map.put("couponNos", "[\"" + System.currentTimeMillis() + "\"]");
			env.post("/v1/certificate_batchs", map);

			// 创建销售计划
			map.clear();
			map.put("quotaNum", productCount);
			map.put("salePlatform", 1001);
			map.put("operatorId", 10);
			map.put("operatorName", "unit_test_v2");
			map.put("productNo", prod.productNo);
			env.post("/v1/marketing_plans", map);

			// 商品上线
			map.put("operatorId", 1001);
			map.put("operatorName", "unit");
			map.put("saleStatus", 2);
			map.put("remark", "");
			env.post("/v1/products/" + prod.productNo + "/applyOnSale", map);
			env.post("/v1/products/" + prod.productNo + "/approveOnSale", map);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void test() throws HttpException, IOException, URISyntaxException {
		try {
			HttpApiParam params = new HttpApiParam();
			List<String> orderCoupons = new ArrayList<String>();
			String orderNo = UUID.randomUUID().toString().replace("-", "").replace("_", "");
			params.clear();
			params.put("orderNo", orderNo);
			params.put("memberId", memberId);
			Map<String, Integer> buyMap = new HashMap<String, Integer>();
			buyMap.put(prod.productNo, buyCount);
			params.put("productIds", new ObjectMapper().writeValueAsString(buyMap));
			env.post("/v1/trade/lock", params);

			params.clear();
			params.put("orderNo", orderNo);
			env.post("/v1/trade/allocate", params);

			params.clear();
			params.put("limit", 1);
			List<Map<?, ?>> coupons = (List<Map<?, ?>>) toT(env.get("/v1/order/" + orderNo + "/coupons", params), Map.class).get("coupons");
			for (Map<?, ?> coupon : coupons) {
				orderCoupons.add(coupon.get("couponNo").toString());
			}

			if (limitstores.length > 0)
				params.put("storeId", limitstores[0]);

			env.get("/v1/members/" + memberId + "/coupons", params);
			System.out.println("===== " + prod.productNo);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	private static <T> T toT(String json, Class<T> clazz) throws JsonParseException, JsonMappingException, IOException {
		try {
			return (T) new ObjectMapper().readValue(json, clazz);
		} catch (Exception e) {
			logger.error(e, e);
		}
		return null;
	}

}
