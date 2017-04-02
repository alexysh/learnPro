package com.wanda.apigateway.http;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * User: bob
 * Date: 14/11/29
 * Time: 15:32
 */
public class ApiKeySecret {

    private String toHex(byte buffer[]) {
        StringBuffer sb = new StringBuffer(buffer.length * 2);
        for (int i = 0; i < buffer.length; i++) {
            sb.append(Character.forDigit((buffer[i] & 240) >> 4, 16));
            sb.append(Character.forDigit(buffer[i] & 15, 16));
        }

        return sb.toString();
    }

    public String test(String appKey, String appSecret, String method, int ts) {
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 not supported", e);
        }
        md5.reset();

        String[] params = new String[]{
                "app_key=" + appKey,
                "app_secret=" + appSecret,
                "method=" + method,
                "ts=" + ts
        };
        Arrays.sort(params);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < params.length; i++) {
            sb.append("&").append(params[i]);
        }

        // get sign
        md5.update(sb.toString().substring(1).getBytes());
        byte[] md5ByteArr = md5.digest();
        String sign = this.toHex(md5ByteArr).toLowerCase();

        // url get params
        String[] params2 = new String[]{
                "app_key=" + appKey,
                "sign=" + sign,
                "method=" + method,
                "ts=" + ts
        };
        Arrays.sort(params2);
        StringBuilder sb2 = new StringBuilder();
        for (int i = 0; i < params2.length; i++) {
            sb2.append("&").append(params2[i]);
        }

        return sb2.toString().substring(1);
    }

    public static void main(String[] args) {
        ApiKeySecret apiKeySecret = new ApiKeySecret();
        // test
        System.out.println(apiKeySecret.test("2239rj293fj23jf23j0f23", "3fi903i4f3i49fi340if034f", "POST", 1417248592));
//        System.out.println(apiKeySecret.test("2239rj293fj23jf23j0f23", "3fi903i4f3i49fi340if034f", "POST", (int) (System.currentTimeMillis()/1000)));
    }

}
