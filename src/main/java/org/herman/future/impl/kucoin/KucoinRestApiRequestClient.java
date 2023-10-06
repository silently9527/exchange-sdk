package org.herman.future.impl.kucoin;

import com.alibaba.fastjson.JSONArray;
import okhttp3.Request;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.HmacUtils;
import org.apache.commons.lang3.StringUtils;
import org.herman.exception.ApiException;
import org.herman.future.RestApiInvoker;
import org.herman.future.impl.AbstractRestApiRequestClient;
import org.herman.future.impl.RestApiRequest;
import org.herman.future.impl.kucoin.model.InstanceServer;
import org.herman.future.model.ResponseResult;
import org.herman.future.model.enums.*;
import org.herman.future.model.market.*;
import org.herman.future.model.trade.*;
import org.herman.utils.InputChecker;
import org.herman.utils.JsonWrapper;
import org.herman.utils.JsonWrapperArray;
import org.herman.utils.UrlParamsBuilder;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

public class KucoinRestApiRequestClient extends AbstractRestApiRequestClient {
    private final String passphrase;
    private final ChooseServerStrategy serverStrategy;

    public KucoinRestApiRequestClient(String serverUrl, String apiKey, String secretKey, String passphrase) {
        super(apiKey, secretKey, serverUrl);
        this.passphrase = passphrase;
        this.serverStrategy = new RandomChooseStrategy();
    }

    @Override
    protected Request createRequestWithSignature(String url, String address, UrlParamsBuilder builder) {
        if (builder == null) {
            throw new ApiException(ApiException.RUNTIME_ERROR,
                    "[Invoking] Builder is null when create request with Signature");
        }
        String requestUrl = url + address;
        String timestamp = String.valueOf(System.currentTimeMillis());
        String signature = ApiSignature.createSignature(apiKey, secretKey, timestamp, address, builder);
        String passPhrase = Base64.encodeBase64String(HmacUtils.hmacSha256(secretKey, passphrase));

        if (builder.hasPostParam()) {
            requestUrl += builder.buildUrl();
            return new Request.Builder().url(requestUrl).post(builder.buildPostBody())
                    .addHeader("Content-Type", "application/json")
                    .addHeader("client_SDK_Version", getClientSdkVersion())
                    .addHeader("KC-API-KEY", apiKey)
                    .addHeader("KC-API-SIGN", signature)
                    .addHeader("KC-API-TIMESTAMP", timestamp)
                    .addHeader("KC-API-PASSPHRASE", passPhrase)
                    .addHeader("KC-API-KEY-VERSION", "2")
                    .build();
        } else if (builder.checkMethod("PUT")) {
            requestUrl += builder.buildUrl();
            return new Request.Builder().url(requestUrl)
                    .put(builder.buildPostBody())
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .addHeader("client_SDK_Version", getClientSdkVersion())
                    .addHeader("KC-API-KEY", apiKey)
                    .addHeader("KC-API-SIGN", signature)
                    .addHeader("KC-API-TIMESTAMP", timestamp)
                    .addHeader("KC-API-PASSPHRASE", passPhrase)
                    .addHeader("KC-API-KEY-VERSION", "2")
                    .build();
        } else if (builder.checkMethod("DELETE")) {
            requestUrl += builder.buildUrl();
            return new Request.Builder().url(requestUrl)
                    .delete()
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .addHeader("client_SDK_Version", getClientSdkVersion())
                    .addHeader("KC-API-KEY", apiKey)
                    .addHeader("KC-API-SIGN", signature)
                    .addHeader("KC-API-TIMESTAMP", timestamp)
                    .addHeader("KC-API-PASSPHRASE", passPhrase)
                    .addHeader("KC-API-KEY-VERSION", "2")
                    .build();
        } else {
            requestUrl += builder.buildUrl();
            return new Request.Builder().url(requestUrl)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .addHeader("client_SDK_Version", getClientSdkVersion())
                    .addHeader("KC-API-KEY", apiKey)
                    .addHeader("KC-API-SIGN", signature)
                    .addHeader("KC-API-TIMESTAMP", timestamp)
                    .addHeader("KC-API-PASSPHRASE", passPhrase)
                    .addHeader("KC-API-KEY-VERSION", "2")
                    .build();
        }
    }

    @Override
    protected String getClientSdkVersion() {
        return "kucoin_futures-1.0.0-java";
    }

    @Override
    public RestApiRequest<List<Future>> getFutures() {
        RestApiRequest<List<Future>> request = new RestApiRequest<>();
        UrlParamsBuilder builder = UrlParamsBuilder.build();
        request.request = createRequestByGet("/api/v1/contracts/active", builder);

        request.jsonParser = (jsonWrapper -> {
            List<Future> futures = new LinkedList<>();
            JsonWrapperArray symbolArray = jsonWrapper.getJsonArray("data");
            symbolArray.forEach((item) -> {
                Future entry = parseFuture(item);
                futures.add(entry);
            });
            return futures;
        });
        return request;
    }

    private Future parseFuture(JsonWrapper item) {
        Future entry = new Future();
        entry.setSymbol(item.getString("symbol"));
        entry.setBaseAsset(item.getString("baseCurrency"));
        entry.setQuoteAsset(item.getString("quoteCurrency"));

        if (item.getString("status").equals("Open")) {
            entry.setStatus(FutureStatus.TRADING);
        } else if (item.getString("status").equals("Pause")) {
            entry.setStatus(FutureStatus.PAUSE);
        } else {
            entry.setStatus(FutureStatus.UN_KNOW);
        }
        entry.setOnboardDate(item.getLong("firstOpenDate"));
        entry.setFutureType("FFWCSX".equals(item.getString("type")) ? FutureType.PERPETUAL : FutureType.SETTLEMENT);

        entry.setMultiplier(item.getBigDecimal("multiplier"));
        entry.setTickSize(item.getBigDecimal("tickSize"));
        entry.setMinQty(item.getBigDecimal("lotSize"));
        entry.setSource(item);
        return entry;
    }

    @Override
    public RestApiRequest<Future> getFuture(String symbol) {
        RestApiRequest<Future> request = new RestApiRequest<>();
        UrlParamsBuilder builder = UrlParamsBuilder.build();
        request.request = createRequestByGet("/api/v1/contracts/" + symbol, builder);

        request.jsonParser = (jsonWrapper -> {
            JsonWrapper item = jsonWrapper.getJsonObject("data");
            return parseFuture(item);
        });
        return request;
    }

    @Override
    public RestApiRequest<List<MarkPrice>> getMarkPrice(String symbol) {
        RestApiRequest<List<MarkPrice>> request = new RestApiRequest<>();
        UrlParamsBuilder builder = UrlParamsBuilder.build();
        request.request = createRequestByGet("/api/v1/mark-price/" + symbol + "/current", builder);

        request.jsonParser = (jsonWrapper -> {
            List<MarkPrice> result = new LinkedList<>();

            JsonWrapper data = jsonWrapper.getJsonObject("data");
            MarkPrice element = new MarkPrice();
            element.setSymbol(data.getString("symbol"));
            element.setMarkPrice(data.getBigDecimal("value"));
            element.setTime(data.getLong("timePoint"));

            result.add(element);
            return result;
        });
        return request;
    }

    @Override
    public RestApiRequest<List<FundingRate>> getFundingRateHistory(String symbol, Long startTime, Long endTime, Integer limit) {
        throw new UnsupportedOperationException();
    }

    @Override
    public RestApiRequest<FundingRate> getFundingRate(String symbol) {
        Future future = RestApiInvoker.callSync(this.getFuture(symbol));

        JsonWrapper source = (JsonWrapper) future.getSource();

        RestApiRequest<FundingRate> request = new RestApiRequest<>();
        UrlParamsBuilder builder = UrlParamsBuilder.build();
        request.request = createRequestByGet("/api/v1/funding-rate/" + source.getString("fundingRateSymbol") + "/current", builder);

        request.jsonParser = (jsonWrapper -> {
            List<FundingRate> result = new LinkedList<>();

            JsonWrapper data = jsonWrapper.getJsonObject("data");
            FundingRate element = new FundingRate();
            element.setSymbol(symbol);
            element.setFundingRate(data.getBigDecimal("value"));
            element.setFundingTime(data.getLong("timePoint"));

            result.add(element);
            return result.get(0);

        });
        return request;
    }

    @Override
    public RestApiRequest<List<Candlestick>> getCandlestick(String symbol, CandlestickInterval interval, Long startTime, Long endTime, Integer limit) {
        RestApiRequest<List<Candlestick>> request = new RestApiRequest<>();
        UrlParamsBuilder builder = UrlParamsBuilder.build()
                .putToUrl("symbol", symbol)
                .putToUrl("granularity", interval.getMinutes())
                .putToUrl("from", startTime)
                .putToUrl("to", endTime);
        request.request = createRequestByGet("/api/v1/kline/query", builder);

        request.jsonParser = (jsonWrapper -> {
            List<Candlestick> result = new LinkedList<>();
            JsonWrapperArray dataArray = jsonWrapper.getJsonArray("data");
            dataArray.forEachAsArray((item) -> {
                Candlestick element = new Candlestick();
                element.setOpenTime(item.getLongAt(0));
                element.setOpen((BigDecimal) item.getObjectAt(1));
                element.setHigh((BigDecimal) item.getObjectAt(2));
                element.setLow((BigDecimal) item.getObjectAt(3));
                element.setClose((BigDecimal) item.getObjectAt(4));
                element.setVolume(BigDecimal.valueOf(item.getIntegerAt(5)));
                result.add(element);
            });

            return result;
        });
        return request;
    }

    @Override
    public RestApiRequest<AccountInformation> getAccountInformation() {
        throw new UnsupportedOperationException();
    }

    @Override
    public RestApiRequest<List<AccountBalance>> getBalance() {
        RestApiRequest<List<AccountBalance>> request = new RestApiRequest<>();
        UrlParamsBuilder builder = UrlParamsBuilder.build()
                .putToUrl("currency", "USDT");
        request.request = createRequestByGetWithSignature("/api/v1/account-overview", builder);

        request.jsonParser = (jsonWrapper -> {
            List<AccountBalance> result = new LinkedList<>();
            JsonWrapper data = jsonWrapper.getJsonObject("data");

            AccountBalance element = new AccountBalance();
            element.setAsset(data.getString("currency"));
            element.setBalance(data.getBigDecimal("accountEquity"));
            element.setAvailableBalance(data.getBigDecimal("availableBalance"));
            element.setCrossUnPnl(data.getBigDecimal("unrealisedPNL"));

            result.add(element);
            return result;
        });
        return request;
    }

    @Override
    public RestApiRequest<String> postOrder(String symbol, OrderSide side, PositionSide positionSide, OrderType orderType,
                                            TimeInForce timeInForce, BigDecimal quantity, BigDecimal price, Boolean reduceOnly,
                                            String newClientOrderId, BigDecimal stopPrice, WorkingType workingType, Integer leverage) {
        RestApiRequest<String> request = new RestApiRequest<>();
        UrlParamsBuilder builder = UrlParamsBuilder.build()
                .putToPost("clientOid", newClientOrderId)
                .putToPost("symbol", symbol)
                .putToPost("side", side.name().toLowerCase())
                .putToPost("type", orderType.name().toLowerCase())
                .putToPost("leverage", leverage)
                .putToPost("size", quantity.stripTrailingZeros().toPlainString())
                .putToPost("price", price.stripTrailingZeros().toPlainString())
                .putToPost("timeInForce", timeInForce)
                .putToPost("reduceOnly", reduceOnly.toString());

        request.request = createRequestByPostWithSignature("/api/v1/orders", builder);

        request.jsonParser = (jsonWrapper -> jsonWrapper.getJsonObject("data").getString("orderId"));
        return request;
    }

    @Override
    public RestApiRequest<String> cancelOrder(String symbol, String orderId, String origClientOrderId) {
        InputChecker.checker()
                .shouldNotNull(orderId, "orderId");
        RestApiRequest<String> request = new RestApiRequest<>();
        UrlParamsBuilder builder = UrlParamsBuilder.build();
        request.request = createRequestByDeleteWithSignature("/api/v1/orders/" + orderId, builder);

        request.jsonParser = (jsonWrapper -> jsonWrapper.getJsonObject("data").getJsonArray("cancelledOrderIds").getStringAt(0));
        return request;
    }

    @Override
    public RestApiRequest<ResponseResult> cancelAllOpenOrder(String symbol) {
        RestApiRequest<ResponseResult> request = new RestApiRequest<>();
        UrlParamsBuilder builder = UrlParamsBuilder.build()
                .putToUrl("symbol", symbol);
        request.request = createRequestByDeleteWithSignature("/api/v1/orders", builder);

        request.jsonParser = (jsonWrapper -> {
            ResponseResult responseResult = new ResponseResult();
            responseResult.setCode(jsonWrapper.getInteger("code") == 200000 ? 200 : jsonWrapper.getInteger("code"));
            responseResult.setMsg("OK");
            return responseResult;
        });
        return request;
    }

    @Override
    public RestApiRequest<Order> getOrder(String symbol, String orderId, String origClientOrderId) {
        RestApiRequest<Order> request = new RestApiRequest<>();

        if (StringUtils.isNotEmpty(origClientOrderId)) {
            UrlParamsBuilder builder = UrlParamsBuilder.build()
                    .putToUrl("clientOid", origClientOrderId);
            request.request = createRequestByGetWithSignature("/api/v1/orders/byClientOid", builder);
        } else {
            UrlParamsBuilder builder = UrlParamsBuilder.build();
            request.request = createRequestByGetWithSignature("/api/v1/orders/" + orderId, builder);
        }

        request.jsonParser = (jsonWrapper -> parseOrderDetail(jsonWrapper.getJsonObject("data")));
        return request;
    }

    @Override
    public RestApiRequest<List<Order>> getOpenOrders(String symbol) {
        RestApiRequest<List<Order>> request = new RestApiRequest<>();
        UrlParamsBuilder builder = UrlParamsBuilder.build()
                .putToUrl("status", "active")
                .putToUrl("pageSize", 100);
        if (StringUtils.isNotEmpty(symbol)) {
            builder.putToUrl("symbol", symbol);
        }
        request.request = createRequestByGetWithSignature("/api/v1/orders", builder);

        request.jsonParser = (jsonWrapper -> {
            List<Order> result = new LinkedList<>();
            JsonWrapper data = jsonWrapper.getJsonObject("data");
            JsonWrapperArray items = data.getJsonArray("items");
            items.forEach((item) -> result.add(parseOrderDetail(item)));
            return result;
        });
        return request;
    }

    private Order parseOrderDetail(JsonWrapper jsonWrapper) {
        Order result = new Order();
        result.setSymbol(jsonWrapper.getString("symbol"));
        result.setClientOrderId(jsonWrapper.getString("clientOid"));
        result.setCumQuote(jsonWrapper.getBigDecimal("dealValue"));
        result.setExecutedQty(jsonWrapper.getBigDecimal("dealSize"));
        result.setOrderId(jsonWrapper.getLong("id"));
        result.setOrigQty(jsonWrapper.getBigDecimal("size"));
        result.setPrice(jsonWrapper.getBigDecimal("price"));
        result.setReduceOnly(jsonWrapper.getBoolean("reduceOnly"));
        result.setSide(OrderSide.valueOf(jsonWrapper.getString("side").toUpperCase()));
        result.setPositionSide(PositionSide.BOTH);
        String itemStatus = jsonWrapper.getString("status");
        if ("active".equals(itemStatus) && result.getExecutedQty().doubleValue() == 0) {
            result.setStatus(OrderStatus.NEW);
        } else if ("active".equals(itemStatus) && result.getExecutedQty().doubleValue() != 0) {
            result.setStatus(OrderStatus.PARTIALLY_FILLED);
        } else if ("done".equals(itemStatus)) {
            result.setStatus(OrderStatus.FILLED);
        } else {
            result.setStatus(OrderStatus.INVALID);
        }
//        result.setStopPrice(jsonWrapper.getBigDecimal("stopPrice"));
        result.setTimeInForce(TimeInForce.valueOf(jsonWrapper.getString("timeInForce").toUpperCase()));
        result.setType(OrderType.valueOf(jsonWrapper.getString("type").toUpperCase()));
        result.setUpdateTime(jsonWrapper.getLong("updatedAt"));
        result.setWorkingType(jsonWrapper.getString("stopPriceType"));
        return result;
    }

    @Override
    public RestApiRequest<Leverage> changeInitialLeverage(String symbol, Integer leverage) {
        throw new UnsupportedOperationException();
    }

    @Override
    public RestApiRequest<OrderBook> getOrderBook(String symbol, Integer limit) {
        RestApiRequest<OrderBook> request = new RestApiRequest<>();
        UrlParamsBuilder builder = UrlParamsBuilder.build()
                .putToUrl("symbol", symbol);

        request.request = createRequestByGet(limit <= 20 ? "/api/v1/level2/depth20" : "/api/v1/level2/depth100", builder);

        request.jsonParser = (jsonWrapper -> {
            OrderBook result = new OrderBook();
            JsonWrapper data = jsonWrapper.getJsonObject("data");
            result.setLastUpdateId(data.getLong("ts"));

            List<OrderBookEntry> elementList = new LinkedList<>();
            JsonWrapperArray dataArray = data.getJsonArray("bids");
            dataArray.forEachAsArray((item) -> {
                OrderBookEntry element = new OrderBookEntry();
                element.setPrice((BigDecimal) item.getObjectAt(0));
                element.setQty(BigDecimal.valueOf((Integer) item.getObjectAt(1)));
                elementList.add(element);
            });
            result.setBids(elementList);

            List<OrderBookEntry> askList = new LinkedList<>();
            JsonWrapperArray askArray = data.getJsonArray("asks");
            askArray.forEachAsArray((item) -> {
                OrderBookEntry element = new OrderBookEntry();
                element.setPrice((BigDecimal) item.getObjectAt(0));
                element.setQty(BigDecimal.valueOf((Integer) item.getObjectAt(1)));
                askList.add(element);
            });
            result.setAsks(askList);

            return result;
        });
        return request;
    }

    @Override
    public RestApiRequest<List<Order>> getAllOrders(String symbol, String currentPage, Long startTime, Long endTime, Integer limit) {
        RestApiRequest<List<Order>> request = new RestApiRequest<>();
        UrlParamsBuilder builder = UrlParamsBuilder.build()
                .putToUrl("startAt", startTime)
                .putToUrl("endAt", endTime)
                .putToUrl("currentPage", currentPage)
                .putToUrl("pageSize", limit);
        if (StringUtils.isNotEmpty(symbol)) {
            builder.putToUrl("symbol", symbol);
        }
        request.request = createRequestByGetWithSignature("/api/v1/orders", builder);

        request.jsonParser = (jsonWrapper -> {
            List<Order> result = new LinkedList<>();
            JsonWrapper data = jsonWrapper.getJsonObject("data");
            JsonWrapperArray items = data.getJsonArray("items");
            items.forEach((item) -> result.add(parseOrderDetail(item)));
            return result;
        });
        return request;
    }

    @Override
    public RestApiRequest<List<PositionRisk>> getPositionRisk(String symbol) {
        RestApiRequest<List<PositionRisk>> request = new RestApiRequest<>();
        if (StringUtils.isEmpty(symbol)) {
            UrlParamsBuilder builder = UrlParamsBuilder.build()
                    .putToUrl("currency", "USDT");
            request.request = createRequestByGetWithSignature("/api/v1/positions", builder);
        } else {
            UrlParamsBuilder builder = UrlParamsBuilder.build()
                    .putToUrl("symbol", symbol);
            request.request = createRequestByGetWithSignature("/api/v1/position", builder);
        }

        request.jsonParser = (jsonWrapper -> {
            List<PositionRisk> result = new LinkedList<>();
            JsonWrapperArray dataArray;
            if (StringUtils.isEmpty(symbol)) {
                dataArray = jsonWrapper.getJsonArray("data");
            } else {
                JsonWrapper data = jsonWrapper.getJsonObject("data");
                JSONArray array = new JSONArray();
                array.add(data.getJson());
                dataArray = new JsonWrapperArray(array);
            }

            dataArray.forEach((item) -> {
                PositionRisk positionRisk = new PositionRisk();
                positionRisk.setEntryPrice(item.getBigDecimal("avgEntryPrice"));
                positionRisk.setLeverage(item.getBigDecimal("realLeverage"));
                positionRisk.setLiquidationPrice(StringUtils.isEmpty(item.getString("liquidationPrice")) ? null : item.getBigDecimal("liquidationPrice"));
                positionRisk.setMarkPrice(item.getBigDecimal("markPrice"));
                positionRisk.setPositionAmt(item.getBigDecimal("currentQty"));
                positionRisk.setSymbol(item.getString("symbol"));
                positionRisk.setIsolatedMargin(StringUtils.isEmpty(item.getString("posInit")) ? null : item.getBigDecimal("posInit"));
                positionRisk.setPositionSide(PositionSide.BOTH);
                positionRisk.setMarginType(MarginType.isolated);
                positionRisk.setUnrealizedProfit(item.getBigDecimal("unrealisedPnl"));
//                positionRisk.setUpdateTime(item.getLong("uTime"));
                positionRisk.setSource(item);
                result.add(positionRisk);
            });
            return result;
        });
        return request;
    }

    @Override
    public RestApiRequest<List<Trade>> getRecentTrades(String symbol, Integer limit) {
        RestApiRequest<List<Trade>> request = new RestApiRequest<>();
        UrlParamsBuilder builder = UrlParamsBuilder.build()
                .putToUrl("symbol", symbol);
        request.request = createRequestByGetWithSignature("/api/v1/trade/history", builder);

        request.jsonParser = (jsonWrapper -> {
            List<Trade> result = new LinkedList<>();
            JsonWrapperArray dataArray = jsonWrapper.getJsonArray("data");
            dataArray.forEach((item) -> {
                Trade element = new Trade();
                element.setId(item.getLong("tradeId"));
                element.setPrice(item.getBigDecimal("price"));
                element.setQty(item.getBigDecimal("size"));
                element.setTime(item.getLong("ts"));
                element.setSide(OrderSide.valueOf(item.getString("side").toUpperCase()));
                result.add(element);
            });
            return result;
        });
        return request;
    }

    @Override
    public RestApiRequest<List<SymbolPrice>> getSymbolPriceTicker(String symbol) {
        RestApiRequest<List<SymbolPrice>> request = new RestApiRequest<>();

        UrlParamsBuilder builder = UrlParamsBuilder.build()
                .putToUrl("symbol", symbol);
        request.request = createRequestByGet("/api/v1/ticker", builder);

        request.jsonParser = (jsonWrapper -> {
            List<SymbolPrice> result = new LinkedList<>();
            JsonWrapper data = jsonWrapper.getJsonObject("data");

            SymbolPrice element = new SymbolPrice();
            element.setSymbol(data.getString("symbol"));
            element.setPrice(data.getBigDecimal("price"));
            result.add(element);

            return result;
        });
        return request;
    }

    @Override
    public RestApiRequest<List<SymbolOrderBook>> getSymbolOrderBookTicker(String symbol) {
        RestApiRequest<List<SymbolOrderBook>> request = new RestApiRequest<>();

        UrlParamsBuilder builder = UrlParamsBuilder.build()
                .putToUrl("symbol", symbol);
        request.request = createRequestByGet("/api/v1/ticker", builder);

        request.jsonParser = (jsonWrapper -> {
            List<SymbolOrderBook> result = new LinkedList<>();
            JsonWrapper data = jsonWrapper.getJsonObject("data");
            SymbolOrderBook element = new SymbolOrderBook();

            element.setSymbol(data.getString("symbol"));
            element.setBidPrice(data.getBigDecimal("bestBidPrice"));
            element.setBidQty(data.getBigDecimal("bestBidSize"));
            element.setAskPrice(data.getBigDecimal("bestAskPrice"));
            element.setAskQty(data.getBigDecimal("bestAskSize"));

            result.add(element);
            return result;
        });
        return request;
    }

    @Override
    public RestApiRequest<List<AggregateTrade>> getAggregateTrades(String symbol, String fromId, Long startTime, Long endTime, Integer limit) {
        throw new UnsupportedOperationException();
    }

    public RestApiRequest<String> getPublicEndpoint() {
        return getEndpoint("/api/v1/bullet-public");
    }

    public RestApiRequest<String> getPrivateEndpoint() {
        return getEndpoint("/api/v1/bullet-private");
    }

    private RestApiRequest<String> getEndpoint(String url) {
        RestApiRequest<String> request = new RestApiRequest<>();

        UrlParamsBuilder builder = UrlParamsBuilder.build();
        request.request = createRequestByPostWithSignature(url, builder);

        request.jsonParser = (jsonWrapper -> {
            JsonWrapper data = jsonWrapper.getJsonObject("data");
            String token = data.getString("token");
            JsonWrapperArray instanceServers = data.getJsonArray("instanceServers");
            InstanceServer instanceServer = serverStrategy.choose(instanceServers.getArray().toJavaList(InstanceServer.class));

            return String.format("%s", instanceServer.getEndpoint() + "?token=" + token + "&acceptUserMessage=true");
        });
        return request;
    }
}
