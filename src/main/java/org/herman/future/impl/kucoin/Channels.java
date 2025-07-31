package org.herman.future.impl.kucoin;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.herman.future.model.enums.CandlestickInterval;

import java.util.UUID;

public class Channels {

    public static String ping() {
        String uuid = UUID.randomUUID().toString();
        JSONObject jb = new JSONObject();
        jb.put("id", uuid);
        jb.put("type", "ping");
        return jb.toJSONString();
    }

    public static String subscribe(String topic, boolean privateChannel, boolean response) {
        String uuid = UUID.randomUUID().toString();
        JSONObject jb = new JSONObject();
        jb.put("id", uuid);
        jb.put("type", "subscribe");
        jb.put("topic", topic);
        jb.put("privateChannel", privateChannel);
        jb.put("response", response);
        return jb.toJSONString();
    }

    public static String markPriceChannel(String symbol) {
        return subscribe(markPriceTopic(symbol), false, true);
    }

    public static String markPriceTopic(String symbol) {
        return "/contract/instrument:" + symbol;
    }

    public static String orderChannel(String symbol) {
        return subscribe(orderTopic(symbol), true, true);
    }

    public static String orderTopic(String symbol) {
        if (StringUtils.isNotEmpty(symbol)) {
            return "/contractMarket/tradeOrders:" + symbol;
        }
        return "/contractMarket/tradeOrders";
    }

    public static String bookDepthChannel(String symbol, Integer limit) {
        return subscribe(bookDepthTopic(symbol, limit), false, true);
    }

    public static String bookDepthTopic(String symbol, Integer limit) {
        if (limit <= 5) {
            return "/contractMarket/level2Depth5:" + symbol;
        }
        return "/contractMarket/level2Depth50:" + symbol;
    }

    public static String bookTickerChannel(String symbol) {
        return subscribe(bookTickerTopic(symbol), false, true);
    }

    public static String bookTickerTopic(String symbol) {
        return "/contractMarket/tickerV2:" + symbol;
    }

    public static String lastPriceChannel(String symbol) {
        return subscribe(lastPriceTopic(symbol), false, true);
    }

    public static String lastPriceTopic(String symbol) {
        return "/contractMarket/execution:" + symbol;
    }

    public static String candlestickChannel(String symbol, CandlestickInterval interval) {
        return subscribe(candlestickTopic(symbol, interval), false, true);
    }

    public static String candlestickTopic(String symbol, CandlestickInterval interval) {
        return "/contractMarket/candle:" + symbol + "_" + interval.getMinutes();
    }

    public static String positionChannel(String symbol) {
        return subscribe(positionTopic(symbol), true, true);
    }

    public static String positionTopic(String symbol) {
        if (StringUtils.isNotEmpty(symbol)) {
            return "/contract/position:" + symbol;
        }
        return "/contract/positionAll";
    }

    public static String accountChannel() {
        return subscribe(accountTopic(), true, true);
    }

    public static String accountTopic() {
        return "/contractAccount/wallet";
    }
}
