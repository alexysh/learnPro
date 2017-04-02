/*
 * Copyright 2012-2016 Ffan.com All right reserved. This software is the
 * confidential and proprietary information of Ffan.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Ffan.com.
 */
package com.wanda.ysh.encryp;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;


/**
 * 类EncryptMain.java的实现描述：TODO 类实现描述 
 * @author yangshihong Oct 11, 2016 6:54:03 PM
 */
public class EncryptMain {

    /**
     * @param args
     */
    public static void main(String[] args) {
        String parkingPrivateKey = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAKktxYGnlk/GiyECVpWzXRCcejuj22Zb8mESnny8Ofa3/PthK31894V/ua1moudTLZzvdWV3qHRRsRrnnadbGO6pS9K9Kr+RBLqGuw7uO1dfKm8R6y84JAnOxCalgJdqUn+hMLd4+JlGnGjkhvBxzJzuZgjtza5uix7kfGszcPJ5AgMBAAECgYA2IzuM3gAvzy0+hu0GQruJUUmcRDc1bj8F6VtmNyWlWgNOvi2YnqDmy5SzdKC0vd1e1xIOQlhZwLqYDX5Zy8ESN8PxLZMp7e9pP1jndbD6m9+K1fuCwzPg+IAGt6furWTi9cm7GL6NP/jbDJfUhLIZJt7uu/Zzbs5CPCD9alx9wQJBAPJma7uN81m5prZZYI6d1ASnlz40a6AIOeFRlZXWfTHZJ9enq3A+NroCba7DtCQDPwhZjPfwfLC04PASRaYBVn0CQQCyq61ldaT+lrhszAp6dvwkI/BqzguhNQE8+Xw3R5cBmQb+ppOYsuPcdO78TpMDop/aIkpRG3TEBGtur9pRZ4CtAkAfmu0XEkYVf9hV1Ed7sOZxCOz2Zl1WC1yInVuUotfGBRtlye3Xf7UhAaoNbGTzgRzZ2NtBhLCtVZgylR9RqsD9AkBTIQVAG4h023NbS2Pkrs+JkR8e0lce3iaBVCKzJZ/gom5atO4fMbxKqi03Puev3U46k5G4QuoE+Xlyumb9UQbRAkBvO6jRAM3tEyVyx/m5sKJF634eZNskIY1Gff9915M++sxAmKiIq6nwbpXuAnknad0kWTESUkXhzH3IefG2yolX";
        byte[] buffer = Base64.decode(parkingPrivateKey);
        System.out.println(new String(buffer));

    }
    
    public static RSAPrivateKey loadPrivateKeyByStr(String privateKeyStr) throws Exception
    {
      try
      {
        byte[] buffer = Base64.decode(privateKeyStr);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return (RSAPrivateKey)keyFactory.generatePrivate(keySpec);
      } catch (NoSuchAlgorithmException e) {
        throw new Exception("无此算法");
      } catch (InvalidKeySpecException e) {
        throw new Exception("私钥非法"); } catch (NullPointerException e) {
      }
      throw new Exception("私钥数据为空");
    }

}
