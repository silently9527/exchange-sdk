package org.herman.future.impl.binance;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.herman.future.model.enums.CandlestickInterval;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

public abstract class Channels {

    public static final String OP_SUB = "sub";
    public static final String OP_REQ = "req";

    public static String unsubscribe(List<String> channels) {
        JSONObject json = new JSONObject();
        json.put("params", channels);
        json.put("id", System.currentTimeMillis());
        json.put("method", "UNSUBSCRIBE");
        return json.toJSONString();
    }

    public static String aggregateTradeChannel(String symbol) {
        JSONObject json = new JSONObject();
        json.put("params", aggTradeParams(symbol));
        json.put("id", System.currentTimeMillis());
        json.put("method", "SUBSCRIBE");
        return json.toJSONString();
    }

    public static List<String> aggTradeParams(String symbol) {
        List<String> params = new ArrayList<>();
        String[] split = symbol.split(",");
        Arrays.stream(split).forEach(
                sy -> params.add(sy.toLowerCase() + "@aggTrade")
        );
        return params;
    }

    public static String markPriceChannel(String symbol) {
        JSONObject json = new JSONObject();
        json.put("params", markPriceParams(symbol));
        json.put("id", System.currentTimeMillis());
        json.put("method", "SUBSCRIBE");
        return json.toJSONString();
    }

    public static List<String> markPriceParams(String symbol) {
        List<String> params = new ArrayList<>();
        String[] split = symbol.split(",");
        Arrays.stream(split).forEach(
                sy -> params.add(sy.toLowerCase() + "@markPrice")
        );
        return params;
    }

    public static String candlestickChannel(String symbol, CandlestickInterval interval) {
        JSONObject json = new JSONObject();
        json.put("params", candlestickParams(symbol, interval));
        json.put("id", System.currentTimeMillis());
        json.put("method", "SUBSCRIBE");
        return json.toJSONString();
    }

    public static List<String> candlestickParams(String symbol, CandlestickInterval interval) {
        List<String> params = new ArrayList<>();
        String[] split = symbol.split(",");
        Arrays.stream(split).forEach(
                sy -> params.add(sy.toLowerCase() + "@kline_" + interval)
        );
        return params;
    }

    public static String miniTickerChannel(String symbol) {
        JSONObject json = new JSONObject();
        JSONArray params = new JSONArray();
        params.add(symbol + "@miniTicker");
        json.put("params", params);
        json.put("id", System.currentTimeMillis());
        json.put("method", "SUBSCRIBE");
        return json.toJSONString();
    }

    public static String miniTickerChannel() {
        JSONObject json = new JSONObject();
        JSONArray params = new JSONArray();
        params.add("!miniTicker@arr");
        json.put("params", params);
        json.put("id", System.currentTimeMillis());
        json.put("method", "SUBSCRIBE");
        return json.toJSONString();
    }

    public static String tickerChannel(String symbol) {
        JSONObject json = new JSONObject();
        json.put("params", tickerParams(symbol));
        json.put("id", System.currentTimeMillis());
        json.put("method", "SUBSCRIBE");
        return json.toJSONString();
    }

    public static List<String> tickerParams(String symbol) {
        List<String> params = new ArrayList<>();
        String[] split = symbol.split(",");
        Arrays.stream(split).forEach(
                sy -> params.add(sy.toLowerCase() + "@ticker")
        );
        return params;
    }

    public static String tickerChannel() {
        JSONObject json = new JSONObject();
        JSONArray params = new JSONArray();
        params.add("!ticker@arr");
        json.put("params", params);
        json.put("id", System.currentTimeMillis());
        json.put("method", "SUBSCRIBE");
        return json.toJSONString();
    }

    public static String bookTickerChannel(String symbol) {
        JSONObject json = new JSONObject();
        json.put("params", bookTickerParams(symbol));
        json.put("id", System.currentTimeMillis());
        json.put("method", "SUBSCRIBE");
        return json.toJSONString();
    }

    public static List<String> bookTickerParams(String symbol) {
        List<String> params = new ArrayList<>();
        String[] split = symbol.split(",");
        Arrays.stream(split).forEach(
                sm -> params.add(sm.toLowerCase() + "@bookTicker")
        );
        return params;
    }

    public static String bookTickerChannel() {
        JSONObject json = new JSONObject();
        JSONArray params = new JSONArray();
        params.add("!bookTicker");
        json.put("params", params);
        json.put("id", System.currentTimeMillis());
        json.put("method", "SUBSCRIBE");
        return json.toJSONString();
    }

    public static String liquidationOrderChannel(String symbol) {
        JSONObject json = new JSONObject();
        JSONArray params = new JSONArray();
        params.add(symbol + "@forceOrder");
        json.put("params", params);
        json.put("id", System.currentTimeMillis());
        json.put("method", "SUBSCRIBE");
        return json.toJSONString();
    }

    public static String liquidationOrderChannel() {
        JSONObject json = new JSONObject();
        JSONArray params = new JSONArray();
        params.add("!forceOrder@arr");
        json.put("params", params);
        json.put("id", System.currentTimeMillis());
        json.put("method", "SUBSCRIBE");
        return json.toJSONString();
    }

    public static String bookDepthChannel(String symbol, Integer limit) {
        JSONObject json = new JSONObject();
        json.put("params", bookDepthParams(symbol, limit));
        json.put("id", System.currentTimeMillis());
        json.put("method", "SUBSCRIBE");
        return json.toJSONString();
    }

    public static List<String> bookDepthParams(String symbol, Integer limit) {
        List<String> params = new ArrayList<>();
        String[] split = symbol.split(",");
        Arrays.stream(split).forEach(
                sm -> params.add(sm.toLowerCase() + "@depth" + limit)
        );
        return params;
    }

    public static String diffDepthChannel(String symbol) {
        JSONObject json = new JSONObject();
        JSONArray params = new JSONArray();
        params.add(symbol + "@depth");
        json.put("params", params);
        json.put("id", System.currentTimeMillis());
        json.put("method", "SUBSCRIBE");
        return json.toJSONString();
    }

    public static String userDataChannel(String listenKey) {
        JSONObject json = new JSONObject();
        JSONArray params = new JSONArray();
        params.add(listenKey);
        json.put("params", params);
        json.put("id", System.currentTimeMillis());
        json.put("method", "SUBSCRIBE");
        return json.toJSONString();
    }

}