package org.herman.future.impl.kucoin;

import org.apache.commons.lang3.StringUtils;
import org.herman.future.FutureSubscriptionErrorHandler;
import org.herman.future.FutureSubscriptionListener;
import org.herman.future.impl.WebsocketRequest;
import org.herman.future.impl.WebsocketRequestClient;
import org.herman.future.impl.okex.ApiSignature;
import org.herman.future.model.enums.CandlestickInterval;
import org.herman.future.model.enums.OrderSide;
import org.herman.future.model.enums.OrderStatus;
import org.herman.future.model.enums.PositionSide;
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

public class KucoinWebsocketRequestClient implements WebsocketRequestClient {
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
            String subject = jsonWrapper.getString("subject");
            if (!"mark.index.price".equals(subject)) {
                return null;
            }

            JsonWrapper data = jsonWrapper.getJsonObject("data");
            MarkPriceEvent result = new MarkPriceEvent();
            result.setEventType("MarkPrice");
            result.setEventTime(data.getLong("timestamp"));
            result.setSymbol(symbol);
            result.setMarkPrice(data.getBigDecimal("markPrice"));
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
            JsonWrapper data = jsonWrapper.getJsonObject("data");

            result.setEventType("Candlestick");
            result.setSymbol(data.getString("symbol"));
            result.setEventTime(System.currentTimeMillis());

            result.setOpenTime(data.getLong("time"));
            result.setOpen(data.getBigDecimal("open"));
            result.setHigh(data.getBigDecimal("high"));
            result.setLow(data.getBigDecimal("low"));
            result.setClose(data.getBigDecimal("close"));
            result.setVolume(data.getBigDecimal("volume"));
            result.setQuoteAssetVolume(data.getBigDecimal("turnover"));
            return result;
        };
        return request;
    }

    @Override
    public WebsocketRequest<OrderBookEvent> subscribeBookDepthEvent(String symbol, Integer limit, FutureSubscriptionListener<OrderBookEvent> callback, FutureSubscriptionErrorHandler errorHandler) {
        InputChecker.checker()
                .shouldNotNull(symbol, "symbol")
                .shouldNotNull(limit, "limit")
                .shouldNotNull(callback, "listener");
        WebsocketRequest<OrderBookEvent> request = new WebsocketRequest<>(callback, errorHandler);
        request.name = "***Partial Book Depth for " + symbol + "***";
        request.connectionHandler = (connection) -> connection.send(Channels.bookDepthChannel(symbol, limit));

        request.jsonParser = (jsonWrapper) -> {
            OrderBookEvent result = new OrderBookEvent();
            JsonWrapper data = jsonWrapper.getJsonObject("data");

            result.setEventType("BookDepth");
            result.setEventTime(data.getLong("ts"));
            result.setTransactionTime(data.getLong("ts"));
            result.setSymbol(symbol);
            result.setLastUpdateId(data.getLong("ts"));

            List<OrderBookEntry> elementList = new LinkedList<>();
            JsonWrapperArray dataArray = data.getJsonArray("bids");
            dataArray.forEachAsArray((item) -> {
                OrderBookEntry element = new OrderBookEntry();
                element.setPrice(item.getBigDecimalAt(0));
                element.setQty(BigDecimal.valueOf(item.getIntegerAt(1)));
                elementList.add(element);
            });
            result.setBids(elementList);

            List<OrderBookEntry> askList = new LinkedList<>();
            JsonWrapperArray askArray = data.getJsonArray("asks");
            askArray.forEachAsArray((item) -> {
                OrderBookEntry element = new OrderBookEntry();
                element.setPrice(item.getBigDecimalAt(0));
                element.setQty(BigDecimal.valueOf(item.getIntegerAt(1)));
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
        request.connectionHandler = (connection) -> connection.send(Channels.lastPriceChannel(symbol));

        request.jsonParser = (jsonWrapper) -> {
            SymbolTickerEvent result = new SymbolTickerEvent();
            JsonWrapper data = jsonWrapper.getJsonObject("data");
            result.setEventType("SymbolTicker");
            result.setEventTime(data.getLong("ts"));
            result.setSymbol(data.getString("symbol"));
            result.setPrice(data.getBigDecimal("price"));
            result.setVolume(data.getBigDecimal("size"));
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
        request.connectionHandler = (connection) -> connection.send(Channels.bookTickerChannel(symbol));

        request.jsonParser = (jsonWrapper) -> {
            SymbolBookTickerEvent result = new SymbolBookTickerEvent();
            JsonWrapper data = jsonWrapper.getJsonObject("data");
            result.setOrderBookUpdateId(data.getLong("ts"));
            result.setSymbol(data.getString("symbol"));
            result.setBestBidPrice(data.getBigDecimal("bestBidPrice"));
            result.setBestBidQty(data.getBigDecimal("bestBidSize"));
            result.setBestAskPrice(data.getBigDecimal("bestAskPrice"));
            result.setBestAskQty(data.getBigDecimal("bestAskSize"));
            return result;
        };
        return request;
    }

    @Override
    public WebsocketRequest<SymbolBookTickerEvent> subscribeAllBookTickerEvent(FutureSubscriptionListener<SymbolBookTickerEvent> callback, FutureSubscriptionErrorHandler errorHandler) {
        throw new UnsupportedOperationException();
    }

    @Override
    public WebsocketRequest<List<PositionUpdateEvent>> subscribePositionEvent(String symbol, FutureSubscriptionListener<List<PositionUpdateEvent>> callback, FutureSubscriptionErrorHandler errorHandler) {
        InputChecker.checker()
                .shouldNotNull(symbol, "symbol")
                .shouldNotNull(callback, "listener");
        WebsocketRequest<List<PositionUpdateEvent>> request = new WebsocketRequest<>(callback, errorHandler);
        request.name = "***User Position***";
        request.connectionHandler = (connection) -> connection.send(Channels.positionChannel(symbol));

        request.jsonParser = (jsonWrapper) -> {
            List<PositionUpdateEvent> positionList = new LinkedList<>();
            JsonWrapper data = jsonWrapper.getJsonObject("data");

            PositionUpdateEvent position = new PositionUpdateEvent();
            position.setSymbol(data.getString("symbol"));
            position.setAmount(data.getBigDecimal("currentQty")); // 仓位
            position.setEntryPrice(data.getBigDecimal("avgEntryPrice")); // 入仓价格
            position.setSide(PositionSide.BOTH);
            position.setUnrealizedPnl(data.getBigDecimal("unrealisedPnl")); // 持仓未实现盈亏
            position.setSource(data);
            positionList.add(position);
            return positionList;
        };
        return request;
    }

    @Override
    public WebsocketRequest<OrderUpdateEvent> subscribeOrderUpdateEvent(String symbol, FutureSubscriptionListener<OrderUpdateEvent> callback, FutureSubscriptionErrorHandler errorHandler) {
        InputChecker.checker()
                .shouldNotNull(callback, "listener");
        WebsocketRequest<OrderUpdateEvent> request = new WebsocketRequest<>(callback, errorHandler);
        request.name = "***User Order***";
        request.connectionHandler = (connection) -> connection.send(Channels.orderChannel(symbol));

        request.jsonParser = (jsonWrapper) -> {
            JsonWrapper data = jsonWrapper.getJsonObject("data");
            OrderUpdateEvent orderUpdate = new OrderUpdateEvent();

            orderUpdate.setOrderId(data.getLong("orderId"));
            orderUpdate.setSymbol(data.getString("symbol"));
//            orderUpdate.setType(OrderType.valueOf(data.getString("orderType").toUpperCase()));
            orderUpdate.setSide(OrderSide.valueOf(data.getString("side").toUpperCase()));
            orderUpdate.setPrice(data.getBigDecimal("price"));
            orderUpdate.setOrigQty(data.getBigDecimal("size"));
            orderUpdate.setCumulativeFilledQty(data.getBigDecimal("filledSize"));
            orderUpdate.setTradeId(data.containKey("tradeId") ? data.getLong("tradeId") : null);
            orderUpdate.setClientOrderId(data.containKey("clientOid") ? data.getString("clientOid") : null);
            orderUpdate.setOrderTradeTime(data.getLong("orderTime"));

            String itemStatus = data.getString("status");
            if ("open".equals(itemStatus) && orderUpdate.getCumulativeFilledQty().doubleValue() == 0) {
                orderUpdate.setStatus(OrderStatus.NEW);
            } else if ("open".equals(itemStatus) && orderUpdate.getCumulativeFilledQty().doubleValue() != 0) {
                orderUpdate.setStatus(OrderStatus.PARTIALLY_FILLED);
            } else if ("done".equals(itemStatus)) {
                orderUpdate.setStatus(OrderStatus.FILLED);
            } else {
                orderUpdate.setStatus(OrderStatus.INVALID);
            }
            return orderUpdate;
        };
        return request;
    }

    @Override
    public WebsocketRequest<List<BalanceUpdateEvent>> subscribeAccountEvent(String currency, FutureSubscriptionListener<List<BalanceUpdateEvent>> callback, FutureSubscriptionErrorHandler errorHandler) {
        InputChecker.checker()
                .shouldNotNull(callback, "listener");
        WebsocketRequest<List<BalanceUpdateEvent>> request = new WebsocketRequest<>(callback, errorHandler);
        request.name = "***User Account***";
        request.connectionHandler = (connection) -> connection.send(Channels.accountChannel());

        request.jsonParser = (jsonWrapper) -> {
            List<BalanceUpdateEvent> balanceList = new LinkedList<>();
            JsonWrapper data = jsonWrapper.getJsonObject("data");
            String itemCurrency = data.getString("currency");
            if (!"availableBalance.change".equals(jsonWrapper.getString("subject"))) {
                return null;
            }
            if (StringUtils.isNotEmpty(currency) && !itemCurrency.equalsIgnoreCase(currency)) {
                return null;
            }
            BalanceUpdateEvent balance = new BalanceUpdateEvent();
            balance.setAsset(itemCurrency);
            balance.setWalletBalance(data.getBigDecimal("availableBalance").add(data.getBigDecimal("holdBalance")));
            balanceList.add(balance);
            return balanceList;
        };
        return request;
    }
}
