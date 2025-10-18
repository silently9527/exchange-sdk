package org.herman.future.impl.coinw;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.herman.Constants;
import org.herman.future.FutureSubscriptionErrorHandler;
import org.herman.future.FutureSubscriptionListener;
import org.herman.future.RestApiInvoker;
import org.herman.future.impl.WebsocketRequest;
import org.herman.future.impl.WebsocketRequestClient;
import org.herman.future.impl.binance.BinanceFutureSubscriptionOptions;
import org.herman.future.impl.binance.BinanceRestApiRequestClient;
import org.herman.future.impl.binance.Channels;
import org.herman.future.model.enums.*;
import org.herman.future.model.event.*;
import org.herman.future.model.market.OrderBookEntry;
import org.herman.future.model.user.BalanceUpdateEvent;
import org.herman.future.model.user.OrderUpdateEvent;
import org.herman.future.model.user.PositionUpdateEvent;
import org.herman.utils.InputChecker;
import org.herman.utils.JsonWrapper;
import org.herman.utils.JsonWrapperArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CoinwWebsocketRequestClient implements WebsocketRequestClient {
    private static final Logger log = LoggerFactory.getLogger(CoinwWebsocketRequestClient.class);

    public CoinwWebsocketRequestClient(CoinwFutureSubscriptionOptions options) {
    }

    @Override
    public WebsocketRequest<JsonWrapper> unsubscribe(List<String> channels) {
        throw new UnsupportedOperationException();
    }

    @Override
    public WebsocketRequest<AggregateTradeEvent> subscribeAggregateTradeEvent(String symbol,
                                                                              FutureSubscriptionListener<AggregateTradeEvent> subscriptionListener,
                                                                              FutureSubscriptionErrorHandler errorHandler) {
        throw new UnsupportedOperationException();
    }

    @Override
    public WebsocketRequest<MarkPriceEvent> subscribeMarkPriceEvent(String symbol,
                                                                    FutureSubscriptionListener<MarkPriceEvent> subscriptionListener,
                                                                    FutureSubscriptionErrorHandler errorHandler) {
        InputChecker.checker()
                .shouldNotNull(symbol, "symbol")
                .shouldNotNull(subscriptionListener, "listener");
        WebsocketRequest<MarkPriceEvent> request = new CoinwWebsocketRequest<>(subscriptionListener, errorHandler);
        request.id = DigestUtils.md5Hex(Channels.markPriceChannel(symbol.toLowerCase()));
        request.name = "***Mark Price for " + symbol + "***";
        request.channels = Channels.markPriceParams(symbol.toLowerCase());
        request.connectionHandler = (connection) -> connection.send(Channels.markPriceChannel(symbol.toLowerCase()));

        request.jsonParser = (jsonWrapper) -> {
            JsonWrapper data = jsonWrapper.getJsonObject("data");
            MarkPriceEvent result = new MarkPriceEvent();
            result.setEventType(data.getString("e"));
            result.setEventTime(data.getLong("E"));
            result.setSymbol(data.getString("s"));
            result.setMarkPrice(data.getBigDecimal("p"));
            return result;
        };
        return request;
    }

    @Override
    public WebsocketRequest<CandlestickEvent> subscribeCandlestickEvent(String symbol, CandlestickInterval interval,
                                                                        FutureSubscriptionListener<CandlestickEvent> subscriptionListener,
                                                                        FutureSubscriptionErrorHandler errorHandler) {
        throw new UnsupportedOperationException();
    }

    @Override
    public WebsocketRequest<OrderBookEvent> subscribeBookDepthEvent(String symbol, Integer limit,
                                                                    FutureSubscriptionListener<OrderBookEvent> subscriptionListener,
                                                                    FutureSubscriptionErrorHandler errorHandler) {
        InputChecker.checker()
                .shouldNotNull(symbol, "symbol")
                .shouldNotNull(limit, "limit")
                .shouldNotNull(subscriptionListener, "listener");
        WebsocketRequest<OrderBookEvent> request = new CoinwWebsocketRequest<>(subscriptionListener, errorHandler);
        request.id = DigestUtils.md5Hex(Channels.bookDepthChannel(symbol.toLowerCase(), limit));
        request.name = "***Partial Book Depth for " + symbol + "***";
        request.channels = Channels.bookDepthParams(symbol.toLowerCase(), limit);
        request.connectionHandler = (connection) -> connection.send(Channels.bookDepthChannel(symbol.toLowerCase(), limit));

        request.jsonParser = (jsonWrapper) -> {
            OrderBookEvent result = new OrderBookEvent();
            JsonWrapper data = jsonWrapper.getJsonObject("data");

            result.setEventType(data.getString("e"));
            result.setEventTime(data.getLong("E"));
            result.setTransactionTime(data.getLong("T"));
            result.setSymbol(data.getString("s"));
            result.setFirstUpdateId(data.getLong("U"));
            result.setLastUpdateId(data.getLong("u"));
            result.setLastUpdateIdInlastStream(data.getLong("pu"));

            List<OrderBookEntry> elementList = new LinkedList<>();
            JsonWrapperArray dataArray = data.getJsonArray("b");
            dataArray.forEachAsArray((item) -> {
                OrderBookEntry element = new OrderBookEntry();
                element.setPrice(item.getBigDecimalAt(0));
                element.setQty(item.getBigDecimalAt(1));
                elementList.add(element);
            });
            result.setBids(elementList);

            List<OrderBookEntry> askList = new LinkedList<>();
            JsonWrapperArray askArray = data.getJsonArray("a");
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
    public WebsocketRequest<SymbolTickerEvent> subscribeSymbolTickerEvent(String symbol, FutureSubscriptionListener<SymbolTickerEvent> subscriptionListener, FutureSubscriptionErrorHandler errorHandler) {
        throw new UnsupportedOperationException();
    }

    @Override
    public WebsocketRequest<List<SymbolTickerEvent>> subscribeAllTickerEvent(FutureSubscriptionListener<List<SymbolTickerEvent>> subscriptionListener,
                                                                             FutureSubscriptionErrorHandler errorHandler) {
        throw new UnsupportedOperationException();
    }

    @Override
    public WebsocketRequest<SymbolBookTickerEvent> subscribeSymbolBookTickerEvent(String symbol, FutureSubscriptionListener<SymbolBookTickerEvent> subscriptionListener,
                                                                                  FutureSubscriptionErrorHandler errorHandler) {
        throw new UnsupportedOperationException();
    }

    @Override
    public WebsocketRequest<SymbolBookTickerEvent> subscribeAllBookTickerEvent(FutureSubscriptionListener<SymbolBookTickerEvent> subscriptionListener, FutureSubscriptionErrorHandler errorHandler) {
        throw new UnsupportedOperationException();
    }

    @Override
    public WebsocketRequest<List<BalanceUpdateEvent>> subscribeAccountEvent(String currency, FutureSubscriptionListener<List<BalanceUpdateEvent>> callback, FutureSubscriptionErrorHandler errorHandler) {
        throw new UnsupportedOperationException();
    }

    @Override
    public WebsocketRequest<List<PositionUpdateEvent>> subscribePositionEvent(String symbol, FutureSubscriptionListener<List<PositionUpdateEvent>> callback,
                                                                              FutureSubscriptionErrorHandler errorHandler) {
        throw new UnsupportedOperationException();
    }

    @Override
    public WebsocketRequest<OrderUpdateEvent> subscribeOrderUpdateEvent(String symbol, FutureSubscriptionListener<OrderUpdateEvent> callback, FutureSubscriptionErrorHandler errorHandler) {
        throw new UnsupportedOperationException();
    }

}
