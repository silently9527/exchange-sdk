package org.herman.future.impl.okex;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.herman.future.FutureSubscriptionOptions;
import org.herman.future.model.enums.CandlestickInterval;

public abstract class Channels {

    public static final String OP_SUB = "subscribe";
    public static final String OP_REQ = "req";

    public static String markPriceChannel(String symbol) {
        JSONObject param = new JSONObject();
        param.put("channel", "mark-price");
        param.put("instId", symbol);
        return subscribe(param);
    }


    public static String candlestickChannel(String symbol, CandlestickInterval interval) {
        JSONObject param = new JSONObject();
        if (interval.name().contains("MINUTE")) {
            param.put("channel", "candle" + interval.getCode());
        } else {
            param.put("channel", "candle" + interval.getCode().toUpperCase());
        }
        param.put("instId", symbol);
        return subscribe(param);
    }

    private static String subscribe(JSONObject param) {
        JSONArray args = new JSONArray();
        args.add(param);
        JSONObject json = new JSONObject();
        json.put("op", OP_SUB);
        json.put("args", args);
        return json.toJSONString();
    }

    public static String bookDepthChannel(String symbol) {
        JSONObject param = new JSONObject();
        param.put("channel", "books5");
        param.put("instId", symbol);
        return subscribe(param);
    }

    public static String tickerChannel(String symbol) {
        JSONObject param = new JSONObject();
        param.put("channel", "tickers");
        param.put("instId", symbol);
        return subscribe(param);
    }

    public static String authenticationChannel(OkexFutureSubscriptionOptions options, String timestamp, String signature) {
        JSONObject param = new JSONObject();
        param.put("apiKey", options.getApiKey());
        param.put("passphrase", options.getPassphrase());
        param.put("timestamp", timestamp);
        param.put("sign", signature);

        JSONArray args = new JSONArray();
        args.add(param);
        JSONObject json = new JSONObject();
        json.put("op", "login");
        json.put("args", args);
        return json.toJSONString();
    }

    public static String accountChannel(String currency) {
        JSONObject param = new JSONObject();
        param.put("channel", "account");
        if (StringUtils.isNotEmpty(currency)) {
            param.put("ccy", currency);
        }
        return subscribe(param);
    }

    public static String positionChannel(String symbol) {
        JSONObject param = new JSONObject();
        param.put("channel", "positions");
        param.put("instType", "SWAP");
        if (StringUtils.isNotEmpty(symbol)) {
            param.put("instId", symbol);
        }
        return subscribe(param);
    }

    public static String orderChannel(String symbol) {
        JSONObject param = new JSONObject();
        param.put("channel", "orders");
        param.put("instType", "SWAP");
        if (StringUtils.isNotEmpty(symbol)) {
            param.put("instId", symbol);
        }
        return subscribe(param);
    }
}