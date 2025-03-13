package org.herman.future.impl.huobi;

import com.alibaba.fastjson.JSON;
import org.herman.utils.UrlParamsBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class ApiSignature {

    final Logger log = LoggerFactory.getLogger(getClass());

    static final DateTimeFormatter DT_FORMAT = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ss");

    static final ZoneId ZONE_GMT = ZoneId.of("Z");

    /**
     * 创建一个有效的签名。该方法为客户端调用，将在传入的params中添加AccessKeyId、Timestamp、SignatureVersion、SignatureMethod、Signature参数。
     *
     * @param appKey       AppKeyId.
     * @param appSecretKey AppKeySecret.
     * @param "host"       请求域名，例如"be.huobi.com"
     * @param uri          请求路径，注意不含?以及后的参数，例如"/v1/api/info"
     */
    public static void createSignature(String appKey, String appSecretKey, String uri,
                                       UrlParamsBuilder builder) {
        StringBuilder sb = new StringBuilder(1024);
        int index = uri.indexOf("//");
        String subString = uri.substring(index + 2);
        int index2 = subString.indexOf("/");
        String host = subString.substring(0, index2);
        String constant = subString.substring(index2);
        sb.append(builder.getMethod().toUpperCase()).append('\n') // GET
                .append(host.toLowerCase()).append('\n') // Host
                .append(constant).append('\n'); // /path
        builder.putToPost("AccessKeyId", appKey);
        builder.putToPost("SignatureVersion", "2");
        builder.putToPost("SignatureMethod", "HmacSHA256");
        builder.putToPost("Timestamp", gmtNow());
        // build signature:
        SortedMap<String, Object> map = new TreeMap<>(JSON.parseObject(builder.buildBodToJsonString()));
        map.forEach((key, value) -> sb.append(key).append('=').append(urlEncode(value.toString())).append('&'));
        // remove last '&':
        sb.deleteCharAt(sb.length() - 1);
        // sign:
        Mac hmacSha256 = null;
        try {
            hmacSha256 = Mac.getInstance("HmacSHA256");
            SecretKeySpec secKey = new SecretKeySpec(appSecretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            hmacSha256.init(secKey);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("No such algorithm: " + e.getMessage());
        } catch (InvalidKeyException e) {
            throw new RuntimeException("Invalid key: " + e.getMessage());
        }
        String payload = sb.toString();
        byte[] hash = hmacSha256.doFinal(payload.getBytes(StandardCharsets.UTF_8));
        String actualSign = Base64.getEncoder().encodeToString(hash);
        builder.putToPost("Signature", actualSign);
    }

    /**
     * 使用标准URL Encode编码。注意和JDK默认的不同，空格被编码为%20而不是+。
     *
     * @param s String字符串
     * @return URL编码后的字符串
     */
    public static String urlEncode(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8").replaceAll("\\+", "%20");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("UTF-8 encoding not supported!");
        }
    }

    /**
     * Return epoch seconds
     */
    static long epochNow() {
        return Instant.now().getEpochSecond();
    }

    static String gmtNow() {
        return Instant.ofEpochSecond(epochNow()).atZone(ZONE_GMT).format(DT_FORMAT);
    }
}
