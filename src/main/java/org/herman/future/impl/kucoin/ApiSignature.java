package org.herman.future.impl.kucoin;

import org.apache.commons.lang3.StringUtils;
import org.herman.exception.ApiException;
import org.herman.utils.UrlParamsBuilder;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class ApiSignature {
    private static final String signatureMethodValue = "HmacSHA256";

    public static String createSignature(String accessKey, String secretKey, String timestamp, String requestPath, UrlParamsBuilder builder) {
        if (accessKey == null || "".equals(accessKey) || secretKey == null || "".equals(secretKey)) {
            throw new ApiException(ApiException.KEY_MISSING, "API key and secret key are required");
        }

        String queryString = "", body = "", method = builder.getMethod();
        if (method.equalsIgnoreCase("GET") || method.equalsIgnoreCase("DELETE")) {
            queryString = builder.buildUrl();
        } else {
            body = builder.buildBodToJsonString();
        }

        return doSignature(secretKey, timestamp, requestPath, queryString, body, method);
    }

    public static String doSignature(String secretKey, String timestamp, String requestPath, String queryString, String body, String method) {
        String preHash = preHash(timestamp, method.toUpperCase(), requestPath, queryString, body);
        Mac hmacSha256;
        try {
            hmacSha256 = Mac.getInstance(signatureMethodValue);
            SecretKeySpec secKey = new SecretKeySpec(secretKey.getBytes(), signatureMethodValue);
            hmacSha256.init(secKey);
        } catch (NoSuchAlgorithmException e) {
            throw new ApiException(ApiException.RUNTIME_ERROR,
                    "[Signature] No such algorithm: " + e.getMessage());
        } catch (InvalidKeyException e) {
            throw new ApiException(ApiException.RUNTIME_ERROR,
                    "[Signature] Invalid key: " + e.getMessage());
        }
        return Base64.getEncoder().encodeToString(hmacSha256.doFinal(preHash.getBytes(StandardCharsets.UTF_8)));
    }


    private static String preHash(String timestamp, String method, String requestPath, String queryString, String body) {
        StringBuilder preHash = new StringBuilder();
        preHash.append(timestamp);
        preHash.append(method.toUpperCase());
        preHash.append(requestPath);
        //get方法
        if (StringUtils.isNotEmpty(queryString)) {
            //在queryString前面拼接上？
            preHash.append(queryString);
        }
        //post方法
        if (StringUtils.isNotEmpty(body)) {
            preHash.append(body);
        }
        return preHash.toString();
    }

}
