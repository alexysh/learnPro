package com.wanda.apigateway.http;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.Map.Entry;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.log4j.Logger;

public class HttpApiClient {

	private static Logger logger = Logger.getLogger(HttpApiClient.class);

	static {
		Protocol myhttps = new Protocol("https", new IgnoreSSLProtocolSocketFactory(), 443);
		Protocol.registerProtocol("https", myhttps);

	}

	private static HttpClient createHttpClient() {
		HttpClient httpClient = new HttpClient();
		HttpConnectionManagerParams managerParams = httpClient.getHttpConnectionManager().getParams();
		managerParams.setConnectionTimeout(5000);
		managerParams.setSoTimeout(50000);
		return httpClient;
	}

	public static String get(String url, HttpApiParam params) throws HttpException, IOException, URISyntaxException {
		long t = System.currentTimeMillis();
		HttpClient hc = createHttpClient();
		HttpClientParams hcp = hc.getParams();
		hcp.setContentCharset("UTF-8");

		String paramstring = "";
		for (Entry<String, Object> en : params.entrySet()) {
			if (en.getValue() != null)
				paramstring += en.getKey() + "=" + URLEncoder.encode(en.getValue().toString(), "UTF-8") + "&";
		}
		String respString = null;
		GetMethod method = null;
		try {
			method = new GetMethod(url + "?" + paramstring);
			hc.executeMethod(method);
			respString = method.getResponseBodyAsString();
		} catch (Exception e) {
			logger.error(e, e);
		} finally {
			method.releaseConnection();
		}
		logger.info("\nHTTP API GET(" + (System.currentTimeMillis() - t) + ")\nURL:" + url + "\nPARAM:" + params + "\nRESPONSE:" + respString);

		return respString;
	}

	public static String post(String url, HttpApiParam params) throws HttpException, IOException {
		long t = System.currentTimeMillis();
		NameValuePair[] nvp = new NameValuePair[params.size()];
		int pos = 0;
		for (Entry<String, Object> en : params.entrySet()) {
			nvp[pos++] = new NameValuePair(en.getKey(), en.getValue().toString());
		}
		HttpClient hc = createHttpClient();
		HttpClientParams hcp = hc.getParams();
		hcp.setContentCharset("UTF-8");

		String respString = null;
		PostMethod method = null;
		try {
			method = new PostMethod(url);
			method.setRequestBody(nvp);
			hc.executeMethod(method);
			respString = method.getResponseBodyAsString();
		} catch (Exception e) {
			logger.error(e, e);
		} finally {
			method.releaseConnection();
		}

		logger.info("\nHTTP API POST(" + (System.currentTimeMillis() - t) + ")\nURL:" + url + "\nPARAM:" + params + "\nRESPONSE:" + respString);

		return respString;
	}

}
