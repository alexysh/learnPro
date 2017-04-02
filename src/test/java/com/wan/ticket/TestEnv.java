package com.wan.ticket;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

import org.apache.commons.httpclient.HttpException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.wanda.apigateway.http.HttpApiClient;
import com.wanda.apigateway.http.HttpApiParam;

public class TestEnv {

	public String baseURL;

	private String jdbcURL;
	private String jdbcUser;
	private String jdbcPassword;

	public Connection getConnection() {
		try {
			return DriverManager.getConnection(jdbcURL, jdbcUser, jdbcPassword);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static TestEnv testConfig() {
		TestEnv config = new TestEnv();
		config.baseURL = "http://10.77.135.215:10046";
		config.baseURL = "http://sandbox.api.wanhui.cn/coupon";
		config.jdbcUser = "coupon";
		config.jdbcPassword = "coupon";
		config.jdbcURL = "jdbc:mysql://10.77.135.217:10010/coupon";
		return config;
	}

	public String post(String url, HttpApiParam params) throws HttpException, IOException {
		String post = HttpApiClient.post(baseURL + url, params);
		if (!valid(post))
			throw new RuntimeException(post);
		return post;
	}

	public String get(String url, HttpApiParam params) throws HttpException, IOException, URISyntaxException {
		String get = HttpApiClient.get(baseURL + url, params);
		if (!valid(get))
			throw new RuntimeException(get);
		return get;
	}

	@SuppressWarnings("rawtypes")
	public boolean valid(String json) throws JsonParseException, JsonMappingException, IOException {
		Map readValue = new ObjectMapper().readValue(json, Map.class);
		if (readValue.get("status") == null || Integer.parseInt(readValue.get("status").toString()) != 200)
			return false;
		return true;
	}

}
