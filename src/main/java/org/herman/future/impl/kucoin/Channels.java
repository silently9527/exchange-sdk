package org.herman.future.impl.kucoin;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

public class Channels {
    public static String markPriceChannel(String symbol) {
        return subscribe("/contract/instrument:" + symbol, false, true);
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

    public static String orderChannel(String symbol) {
        if(StringUtils.isNotEmpty(symbol)){
            return subscribe("/contractMarket/tradeOrders:" + symbol, true, true);
        }
        return subscribe("/contractMarket/tradeOrders", true, true);
    }
}
