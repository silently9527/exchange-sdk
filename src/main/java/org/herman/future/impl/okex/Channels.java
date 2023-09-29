package org.herman.future.impl.okex;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.herman.future.model.enums.CandlestickInterval;

import java.util.Arrays;

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
}