package org.herman.future.impl.okex;

import org.apache.commons.lang3.StringUtils;
import org.herman.future.FutureSubscriptionErrorHandler;
import org.herman.future.FutureSubscriptionListener;
import org.herman.future.impl.WebsocketRequest;
import org.herman.future.impl.WebsocketRequestClient;
import org.herman.future.model.enums.*;
import org.herman.future.model.event.*;
import org.herman.future.model.market.OrderBookEntry;
import org.herman.future.model.user.BalanceUpdateEvent;
import org.herman.future.model.user.OrderUpdateEvent;
import org.herman.future.model.user.PositionUpdateEvent;
import org.herman.utils.InputChecker;
import org.herman.utils.JsonWrapper;
import org.herman.utils.JsonWrapperArray;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class OkexWebsocketRequestClient implements WebsocketRequestClient {
    private final OkexFutureSubscriptionOptions options;

    public OkexWebsocketRequestClient(OkexFutureSubscriptionOptions options) {
        this.options = options;
    }

    @Override
    public WebsocketRequest<AggregateTradeEvent> subscribeAggregateTradeEvent(String symbol, FutureSubscriptionListener<AggregateTradeEvent> subscriptionListener, FutureSubscriptionErrorHandler errorHandler) {
        throw new UnsupportedOperationException();
    }

    @Override
    public WebsocketRequest<MarkPriceEvent> subscribeMarkPriceEvent(String symbol, FutureSubscriptionListener<MarkPriceEvent> subscriptionListener, FutureSubscriptionErrorHandler errorHandler) {
        InputChecker.checker()
                .shouldNotNull(symbol, "symbol")
                .shouldNotNull(subscriptionListener, "listener");
        WebsocketRequest<MarkPriceEvent> request = new WebsocketRequest<>(subscriptionListener, errorHandler);
        request.name = "***Mark Price for " + symbol + "***";
        request.connectionHandler = (connection) -> connection.send(Channels.markPriceChannel(symbol));

        request.jsonParser = (jsonWrapper) -> {
            JsonWrapper data = jsonWrapper.getJsonArray("data").getJsonObjectAt(0);
            MarkPriceEvent result = new MarkPriceEvent();
            result.setEventType("MarkPrice");
            result.setEventTime(data.getLong("ts"));
            result.setSymbol(data.getString("instId"));
            result.setMarkPrice(data.getBigDecimal("markPx"));
            return result;
        };
        return request;
    }

    @Override
    public WebsocketRequest<CandlestickEvent> subscribeCandlestickEvent(String symbol, CandlestickInterval interval, FutureSubscriptionListener<CandlestickEvent> subscriptionListener, FutureSubscriptionErrorHandler errorHandler) {
        InputChecker.checker()
                .shouldNotNull(symbol, "symbol")
                .shouldNotNull(interval, "interval")
                .shouldNotNull(subscriptionListener, "listener");
        WebsocketRequest<CandlestickEvent> request = new WebsocketRequest<>(subscriptionListener, errorHandler);
        request.name = "***Candlestick for " + symbol + "***";
        request.connectionHandler = (connection) -> connection.send(Channels.candlestickChannel(symbol, interval));

        request.jsonParser = (jsonWrapper) -> {
            CandlestickEvent result = new CandlestickEvent();
            JsonWrapperArray data = jsonWrapper.getJsonArray("data").getArrayAt(0);
            JsonWrapper arg = jsonWrapper.getJsonObject("arg");

            result.setSymbol(arg.getString("instId"));
            result.setEventType("Candlestick");
            result.setEventTime(System.currentTimeMillis());

            result.setOpenTime(Long.parseLong(data.getStringAt(0)));
            result.setOpen(data.getBigDecimalAt(1));
            result.setHigh(data.getBigDecimalAt(2));
            result.setLow(data.getBigDecimalAt(3));
            result.setClose(data.getBigDecimalAt(4));
            result.setVolume(data.getBigDecimalAt(5));
            result.setQuoteAssetVolume(data.getBigDecimalAt(7));
            return result;
        };
        return request;
    }

    @Override
    public WebsocketRequest<OrderBookEvent> subscribeBookDepthEvent(String symbol, Integer limit, FutureSubscriptionListener<OrderBookEvent> callback, FutureSubscriptionErrorHandler errorHandler) {
        InputChecker.checker()
                .shouldNotNull(symbol, "symbol")
                .shouldNotNull(callback, "listener");
        WebsocketRequest<OrderBookEvent> request = new WebsocketRequest<>(callback, errorHandler);
        request.name = "***Partial Book Depth for " + symbol + "***";
        request.connectionHandler = (connection) -> connection.send(Channels.bookDepthChannel(symbol));

        request.jsonParser = (jsonWrapper) -> {
            OrderBookEvent result = new OrderBookEvent();
            JsonWrapper data = jsonWrapper.getJsonArray("data").getJsonObjectAt(0);

            result.setEventType("BookDepth");
            result.setEventTime(data.getLong("ts"));
            result.setTransactionTime(data.getLong("ts"));
            result.setSymbol(data.getString("instId"));
            result.setLastUpdateId(data.getLong("seqId"));

            List<OrderBookEntry> elementList = new LinkedList<>();
            JsonWrapperArray dataArray = data.getJsonArray("bids");
            dataArray.forEachAsArray((item) -> {
                OrderBookEntry element = new OrderBookEntry();
                element.setPrice(item.getBigDecimalAt(0));
                element.setQty(item.getBigDecimalAt(1));
                elementList.add(element);
            });
            result.setBids(elementList);

            List<OrderBookEntry> askList = new LinkedList<>();
            JsonWrapperArray askArray = data.getJsonArray("asks");
            askArray.forEachAsArray((item) -> {
                OrderBookEntry element = new OrderBookEntry();
                element.setPrice(item.getBigDecimalAt(0));
                element.setQty(item.getBigDecimalAt(1));
                askList.add(element);
            });
            result.setAsks(askList);

            return result;
        };
        return request;
    }

    @Override
    public WebsocketRequest<SymbolTickerEvent> subscribeSymbolTickerEvent(String symbol, FutureSubscriptionListener<SymbolTickerEvent> callback, FutureSubscriptionErrorHandler errorHandler) {
        InputChecker.checker()
                .shouldNotNull(symbol, "symbol")
                .shouldNotNull(callback, "listener");
        WebsocketRequest<SymbolTickerEvent> request = new WebsocketRequest<>(callback, errorHandler);
        request.name = "***Individual Symbol Ticker for " + symbol + "***";
        request.connectionHandler = (connection) -> connection.send(Channels.tickerChannel(symbol));

        request.jsonParser = (jsonWrapper) -> {
            SymbolTickerEvent result = new SymbolTickerEvent();
            JsonWrapper data = jsonWrapper.getJsonArray("data").getJsonObjectAt(0);
            result.setEventType("SymbolTicker");
            result.setEventTime(data.getLong("ts"));
            result.setSymbol(data.getString("instId"));
            result.setPrice(data.getBigDecimal("last"));
            result.setVolume(data.getBigDecimal("lastSz"));
            result.setQuoteAssetVolume(data.getBigDecimal("volCcy24h"));
            return result;
        };
        return request;
    }

    @Override
    public WebsocketRequest<List<SymbolTickerEvent>> subscribeAllTickerEvent(FutureSubscriptionListener<List<SymbolTickerEvent>> callback, FutureSubscriptionErrorHandler errorHandler) {
        throw new UnsupportedOperationException();
    }

    @Override
    public WebsocketRequest<SymbolBookTickerEvent> subscribeSymbolBookTickerEvent(String symbol, FutureSubscriptionListener<SymbolBookTickerEvent> callback, FutureSubscriptionErrorHandler errorHandler) {
        InputChecker.checker()
                .shouldNotNull(symbol, "symbol")
                .shouldNotNull(callback, "listener");
        WebsocketRequest<SymbolBookTickerEvent> request = new WebsocketRequest<>(callback, errorHandler);
        request.name = "***Individual Symbol Ticker for " + symbol + "***";
        request.connectionHandler = (connection) -> connection.send(Channels.tickerChannel(symbol));

        request.jsonParser = (jsonWrapper) -> {
            SymbolBookTickerEvent result = new SymbolBookTickerEvent();
            JsonWrapper data = jsonWrapper.getJsonArray("data").getJsonObjectAt(0);
            result.setOrderBookUpdateId(data.getLong("ts"));
            result.setSymbol(data.getString("instId"));
            result.setBestBidPrice(data.getBigDecimal("bidPx"));
            result.setBestBidQty(data.getBigDecimal("bidSz"));
            result.setBestAskPrice(data.getBigDecimal("askPx"));
            result.setBestAskQty(data.getBigDecimal("askSz"));
            return result;
        };
        return request;
    }

    @Override
    public WebsocketRequest<SymbolBookTickerEvent> subscribeAllBookTickerEvent(FutureSubscriptionListener<SymbolBookTickerEvent> callback, FutureSubscriptionErrorHandler errorHandler) {
        throw new UnsupportedOperationException();
    }

    @Override
    public WebsocketRequest<List<BalanceUpdateEvent>> subscribeAccountEvent(String currency, FutureSubscriptionListener<List<BalanceUpdateEvent>> callback, FutureSubscriptionErrorHandler errorHandler) {
        InputChecker.checker()
                .shouldNotNull(callback, "listener");
        WebsocketRequest<List<BalanceUpdateEvent>> request = new WebsocketRequest<>(callback, errorHandler);
        request.name = "***User Account***";
        request.authHandler = (connection) -> {
            String timestamp = (new Date().getTime() / 1000) + "";
            String signature = ApiSignature.doSignature(options.getSecretKey(), timestamp, "/users/self/verify", "", "", "GET");
            connection.send(Channels.authenticationChannel(options, timestamp, signature));
        };
        request.connectionHandler = (connection) -> connection.send(Channels.accountChannel(currency));

        request.jsonParser = (jsonWrapper) -> {
            List<BalanceUpdateEvent> balanceList = new LinkedList<>();
            JsonWrapperArray details = jsonWrapper.getJsonArray("data").getJsonObjectAt(0).getJsonArray("details");
            details.forEach(item -> {
                String itemCurrency = item.getString("ccy");
                if (StringUtils.isNotEmpty(currency) && !itemCurrency.equalsIgnoreCase(currency)) {
                    return;
                }
                BalanceUpdateEvent balance = new BalanceUpdateEvent();
                balance.setAsset(itemCurrency);
                balance.setWalletBalance(item.getBigDecimal("eq"));
                balanceList.add(balance);
            });
            return balanceList;
        };
        return request;
    }

    @Override
    public WebsocketRequest<List<PositionUpdateEvent>> subscribePositionEvent(String symbol, FutureSubscriptionListener<List<PositionUpdateEvent>> callback, FutureSubscriptionErrorHandler errorHandler) {
        InputChecker.checker()
                .shouldNotNull(callback, "listener");
        WebsocketRequest<List<PositionUpdateEvent>> request = new WebsocketRequest<>(callback, errorHandler);
        request.name = "***User Position***";
        request.authHandler = (connection) -> {
            String timestamp = (new Date().getTime() / 1000) + "";
            String signature = ApiSignature.doSignature(options.getSecretKey(), timestamp, "/users/self/verify", "", "", "GET");
            connection.send(Channels.authenticationChannel(options, timestamp, signature));
        };
        request.connectionHandler = (connection) -> connection.send(Channels.positionChannel(symbol));

        request.jsonParser = (jsonWrapper) -> {
            List<PositionUpdateEvent> positionList = new LinkedList<>();
            JsonWrapperArray datalist = jsonWrapper.getJsonArray("data");
            datalist.forEach(item -> {
                String itemSymbol = item.getString("instId");
                if (StringUtils.isNotEmpty(symbol) && !itemSymbol.equalsIgnoreCase(symbol)) {
                    return;
                }
                PositionUpdateEvent position = new PositionUpdateEvent();
                position.setSymbol(itemSymbol);
                position.setAmount(item.getBigDecimal("pos")); // 仓位
                position.setEntryPrice(item.getBigDecimal("avgPx")); // 入仓价格
                position.setSide("net".equalsIgnoreCase(item.getString("posSide")) ? PositionSide.BOTH : PositionSide.valueOf(item.getString("posSide").toUpperCase()));
                position.setUnrealizedPnl(item.getBigDecimal("upl")); // 持仓未实现盈亏
                positionList.add(position);
            });
            return positionList;
        };
        return request;
    }

    @Override
    public WebsocketRequest<OrderUpdateEvent> subscribeOrderUpdateEvent(String symbol, FutureSubscriptionListener<OrderUpdateEvent> callback, FutureSubscriptionErrorHandler errorHandler) {
        InputChecker.checker()
                .shouldNotNull(symbol, "symbol")
                .shouldNotNull(callback, "listener");
        WebsocketRequest<OrderUpdateEvent> request = new WebsocketRequest<>(callback, errorHandler);
        request.name = "***User Order***";
        request.authHandler = (connection) -> {
            String timestamp = (new Date().getTime() / 1000) + "";
            String signature = ApiSignature.doSignature(options.getSecretKey(), timestamp, "/users/self/verify", "", "", "GET");
            connection.send(Channels.authenticationChannel(options, timestamp, signature));
        };
        request.connectionHandler = (connection) -> connection.send(Channels.orderChannel(symbol));

        request.jsonParser = (jsonWrapper) -> {
            OrderUpdateEvent orderUpdate = new OrderUpdateEvent();

            JsonWrapper jsondata = jsonWrapper.getJsonArray("data").getJsonObjectAt(0);
            String itemSymbol = jsondata.getStringOrDefault("instId", "");
            orderUpdate.setSymbol(itemSymbol);

            if (StringUtils.isNotEmpty(symbol) && !itemSymbol.equalsIgnoreCase(symbol)) {
                return null;
            }

            orderUpdate.setClientOrderId(jsondata.getString("clOrdId"));
            orderUpdate.setOrderId(jsondata.getLong("ordId"));
            orderUpdate.setSide(OrderSide.valueOf(jsondata.getString("side").toUpperCase()));
            orderUpdate.setType("limit".equals(jsondata.getString("ordType")) ? OrderType.LIMIT : OrderType.MARKET);
            orderUpdate.setOrigQty(jsondata.getBigDecimal("sz"));
            orderUpdate.setPrice(StringUtils.isEmpty(jsondata.getString("px")) ? null : jsondata.getBigDecimal("px"));
            String status = jsondata.getString("state");
            if ("live".equalsIgnoreCase(status)) {
                orderUpdate.setStatus(OrderStatus.NEW);
            } else if ("partially_filled".equalsIgnoreCase(status)) {
                orderUpdate.setStatus(OrderStatus.PARTIALLY_FILLED);
            } else if ("canceled".equalsIgnoreCase(status)) {
                orderUpdate.setStatus(OrderStatus.CANCELED);
            } else if ("filled".equalsIgnoreCase(status)) {
                orderUpdate.setStatus(OrderStatus.FILLED);
            }
            orderUpdate.setAvgPrice(jsondata.getBigDecimal("avgPx"));

            orderUpdate.setCommissionAsset(jsondata.getStringOrDefault("fillFeeCcy", "USDT"));
            orderUpdate.setCommissionAmount(jsondata.getBigDecimalOrDefault("fillFee", BigDecimal.ZERO));
            orderUpdate.setOrderTradeTime(jsondata.containKey("fillTime") ? jsondata.getLong("fillTime") : null);
            orderUpdate.setIsReduceOnly(jsondata.getBoolean("reduceOnly"));
            return orderUpdate;
        };
        return request;
    }
}
