package org.herman.future.impl.kucoin;

import org.apache.commons.lang3.StringUtils;
import org.herman.future.FutureSubscriptionErrorHandler;
import org.herman.future.FutureSubscriptionListener;
import org.herman.future.impl.WebsocketRequest;
import org.herman.future.impl.WebsocketRequestClient;
import org.herman.future.model.enums.CandlestickInterval;
import org.herman.future.model.enums.OrderSide;
import org.herman.future.model.enums.OrderStatus;
import org.herman.future.model.enums.OrderType;
import org.herman.future.model.event.*;
import org.herman.future.model.user.BalanceUpdateEvent;
import org.herman.future.model.user.OrderUpdateEvent;
import org.herman.future.model.user.PositionUpdateEvent;
import org.herman.utils.InputChecker;
import org.herman.utils.JsonWrapper;

import java.math.BigDecimal;
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
        return null;
    }

    @Override
    public WebsocketRequest<OrderBookEvent> subscribeBookDepthEvent(String symbol, Integer limit, FutureSubscriptionListener<OrderBookEvent> callback, FutureSubscriptionErrorHandler errorHandler) {
        return null;
    }

    @Override
    public WebsocketRequest<SymbolMiniTickerEvent> subscribeSymbolTickerEvent(String symbol, FutureSubscriptionListener<SymbolMiniTickerEvent> callback, FutureSubscriptionErrorHandler errorHandler) {
        return null;
    }

    @Override
    public WebsocketRequest<List<SymbolMiniTickerEvent>> subscribeAllTickerEvent(FutureSubscriptionListener<List<SymbolMiniTickerEvent>> callback, FutureSubscriptionErrorHandler errorHandler) {
        return null;
    }

    @Override
    public WebsocketRequest<SymbolBookTickerEvent> subscribeSymbolBookTickerEvent(String symbol, FutureSubscriptionListener<SymbolBookTickerEvent> callback, FutureSubscriptionErrorHandler errorHandler) {
        return null;
    }

    @Override
    public WebsocketRequest<SymbolBookTickerEvent> subscribeAllBookTickerEvent(FutureSubscriptionListener<SymbolBookTickerEvent> callback, FutureSubscriptionErrorHandler errorHandler) {
        return null;
    }

    @Override
    public WebsocketRequest<List<PositionUpdateEvent>> subscribePositionEvent(String symbol, FutureSubscriptionListener<List<PositionUpdateEvent>> callback, FutureSubscriptionErrorHandler errorHandler) {
        return null;
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
        return null;
    }
}
