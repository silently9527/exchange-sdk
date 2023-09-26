package org.herman.future.impl.binance;

import org.apache.commons.codec.binary.Hex;
import org.herman.Constants;
import org.herman.exception.ApiException;
import org.herman.utils.UrlParamsBuilder;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class ApiSignature {

    static final String op = "op";
    static final String opValue = "auth";
    private static final String signatureMethodValue = "HmacSHA256";
    public static final String signatureVersionValue = "2";

    public void createSignature(String accessKey, String secretKey, UrlParamsBuilder builder) {

        if (accessKey == null || "".equals(accessKey) || secretKey == null || "".equals(secretKey)) {
            throw new ApiException(ApiException.KEY_MISSING, "API key and secret key are required");
        }

        builder.putToUrl("recvWindow", Long.toString(Constants.Future.DEFAULT_RECEIVING_WINDOW))
                .putToUrl("timestamp", Long.toString(System.currentTimeMillis()));

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
        String payload = builder.buildSignature();
        String actualSign = new String(Hex.encodeHex(hmacSha256.doFinal(payload.getBytes())));

        builder.putToUrl("signature", actualSign);

    }

}
