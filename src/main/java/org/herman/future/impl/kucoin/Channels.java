package org.herman.future.impl.kucoin;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.herman.future.model.enums.CandlestickInterval;

import java.util.UUID;

public class Channels {

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
        return subscribe("/contract/instrument:" + symbol, false, true);
    }

    public static String orderChannel(String symbol) {
        if (StringUtils.isNotEmpty(symbol)) {
            return subscribe("/contractMarket/tradeOrders:" + symbol, true, true);
        }
        return subscribe("/contractMarket/tradeOrders", true, true);
    }

    public static String bookDepthChannel(String symbol, Integer limit) {
        if (limit <= 5) {
            return subscribe("/contractMarket/level2Depth5:" + symbol, false, true);
        }
        return subscribe("/contractMarket/level2Depth50:" + symbol, false, true);
    }

    public static String bookTickerChannel(String symbol) {
        return subscribe("/contractMarket/tickerV2:" + symbol, false, true);
    }

    public static String lastPriceChannel(String symbol) {
        return subscribe("/contractMarket/execution:" + symbol, false, true);
    }

    public static String candlestickChannel(String symbol, CandlestickInterval interval) {
        return subscribe("/contractMarket/candle:" + symbol + "_" + interval.getMinutes(), false, true);
    }

    public static String positionChannel(String symbol) {
        if (StringUtils.isNotEmpty(symbol)) {
            return subscribe("/contract/position:" + symbol, true, true);
        }
        return subscribe("/contract/positionAll", true, true);
    }

    public static String accountChannel() {
        return subscribe("/contractAccount/wallet", true, true);
    }
}
