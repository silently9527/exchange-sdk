package org.herman.future.impl.okex;

import com.alibaba.fastjson.JSONObject;
import okhttp3.Request;
import org.apache.commons.lang3.StringUtils;
import org.herman.Constants;
import org.herman.exception.ApiException;
import org.herman.future.RestApiInvoker;
import org.herman.future.impl.AbstractRestApiRequestClient;
import org.herman.future.impl.RestApiRequest;
import org.herman.future.model.ResponseResult;
import org.herman.future.model.enums.*;
import org.herman.future.model.market.*;
import org.herman.future.model.trade.*;
import org.herman.utils.DateUtils;
import org.herman.utils.JsonWrapper;
import org.herman.utils.JsonWrapperArray;
import org.herman.utils.UrlParamsBuilder;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class OkexRestApiRequestClient extends AbstractRestApiRequestClient {
    private final String passphrase;

    public OkexRestApiRequestClient(String apiKey, String secretKey, String passphrase) {
        this.apiKey = apiKey;
        this.secretKey = secretKey;
        this.passphrase = passphrase;
        this.serverUrl = Constants.Future.OKEX_REST_API_BASE_URL;
    }

    @Override
    protected Request createRequestWithSignature(String url, String address, UrlParamsBuilder builder) {
        if (builder == null) {
            throw new ApiException(ApiException.RUNTIME_ERROR,
                    "[Invoking] Builder is null when create request with Signature");
        }
        String requestUrl = url + address;
        String timestamp = DateUtils.getUnixTime();
        String signature = new ApiSignature().createSignature(apiKey, secretKey, timestamp, address, builder);
        if (builder.hasPostParam()) {
            requestUrl += builder.buildUrl();
            return new Request.Builder().url(requestUrl).post(builder.buildPostBody())
                    .addHeader("Content-Type", "application/json")
                    .addHeader("client_SDK_Version", getClientSdkVersion())
                    .addHeader("OK-ACCESS-KEY", apiKey)
                    .addHeader("OK-ACCESS-SIGN", signature)
                    .addHeader("OK-ACCESS-TIMESTAMP", timestamp)
                    .addHeader("OK-ACCESS-PASSPHRASE", passphrase)
                    .build();
        } else if (builder.checkMethod("PUT")) {
            requestUrl += builder.buildUrl();
            return new Request.Builder().url(requestUrl)
                    .put(builder.buildPostBody())
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .addHeader("client_SDK_Version", getClientSdkVersion())
                    .addHeader("OK-ACCESS-KEY", apiKey)
                    .addHeader("OK-ACCESS-SIGN", signature)
                    .addHeader("OK-ACCESS-TIMESTAMP", timestamp)
                    .addHeader("OK-ACCESS-PASSPHRASE", passphrase)
                    .build();
        } else if (builder.checkMethod("DELETE")) {
            requestUrl += builder.buildUrl();
            return new Request.Builder().url(requestUrl)
                    .delete()
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .addHeader("client_SDK_Version", getClientSdkVersion())
                    .addHeader("OK-ACCESS-KEY", apiKey)
                    .addHeader("OK-ACCESS-SIGN", signature)
                    .addHeader("OK-ACCESS-TIMESTAMP", timestamp)
                    .addHeader("OK-ACCESS-PASSPHRASE", passphrase)
                    .build();
        } else {
            requestUrl += builder.buildUrl();
            return new Request.Builder().url(requestUrl)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .addHeader("client_SDK_Version", getClientSdkVersion())
                    .addHeader("OK-ACCESS-KEY", apiKey)
                    .addHeader("OK-ACCESS-SIGN", signature)
                    .addHeader("OK-ACCESS-TIMESTAMP", timestamp)
                    .addHeader("OK-ACCESS-PASSPHRASE", passphrase)
                    .build();
        }
    }

    @Override
    protected String getClientSdkVersion() {
        return "okex_futures-1.0.0-java";
    }

    @Override
    public RestApiRequest<ExchangeInformation> getExchangeInformation() {
        RestApiRequest<ExchangeInformation> request = new RestApiRequest<>();
        UrlParamsBuilder builder = UrlParamsBuilder.build()
                .putToUrl("instType", "SWAP");
        request.request = createRequestByGet("/api/v5/public/instruments", builder);

        request.jsonParser = (jsonWrapper -> {
            ExchangeInformation result = new ExchangeInformation();

            List<ExchangeInfoEntry> symbolList = new LinkedList<>();
            JsonWrapperArray symbolArray = jsonWrapper.getJsonArray("data");
            symbolArray.forEach((item) -> {
                ExchangeInfoEntry symbol = new ExchangeInfoEntry();
                symbol.setSymbol(item.getString("instId"));
                symbol.setBaseAsset(item.getString("ctValCcy"));
                symbol.setQuoteAsset(item.getString("settleCcy"));

                if (item.getString("state").equals("live")) {
                    symbol.setStatus(FutureStatus.TRADING);
                } else if (item.getString("state").equals("suspend")) {
                    symbol.setStatus(FutureStatus.PAUSE);
                } else {
                    symbol.setStatus(FutureStatus.UN_KNOW);
                }
                symbol.setOnboardDate(item.getLong("listTime"));
                symbol.setFutureType(FutureType.PERPETUAL);

                symbol.setMultiplier(item.getBigDecimal("ctVal"));
                symbol.setTickSize(item.getBigDecimal("tickSz"));
                symbol.setStepSize(item.getBigDecimal("lotSz"));
                symbol.setMinQty(item.getBigDecimal("minSz"));
                symbolList.add(symbol);
            });
            result.setSymbols(symbolList);

            return result;
        });
        return request;
    }

    @Override
    public RestApiRequest<List<MarkPrice>> getMarkPrice(String symbol) {
        RestApiRequest<List<MarkPrice>> request = new RestApiRequest<>();
        UrlParamsBuilder builder = UrlParamsBuilder.build()
                .putToUrl("instType", "SWAP")
                .putToUrl("instId", symbol);
        request.request = createRequestByGet("/api/v5/public/mark-price", builder);

        request.jsonParser = (jsonWrapper -> {
            List<MarkPrice> result = new LinkedList<>();
            JsonWrapperArray dataArray = jsonWrapper.getJsonArray("data");
            dataArray.forEach((item) -> {
                MarkPrice element = new MarkPrice();
                element.setSymbol(item.getString("instId"));
                element.setMarkPrice(item.getBigDecimal("markPx"));
                element.setTime(item.getLong("ts"));
                result.add(element);
            });

            return result;
        });
        return request;
    }

    @Override
    public RestApiRequest<List<FundingRate>> getFundingRateHistory(String symbol, Long startTime, Long endTime, Integer limit) {
        RestApiRequest<List<FundingRate>> request = new RestApiRequest<>();
        UrlParamsBuilder builder = UrlParamsBuilder.build()
                .putToUrl("instId", symbol)
                .putToUrl("before", startTime)
                .putToUrl("after", endTime)
                .putToUrl("limit", limit);
        request.request = createRequestByGet("/api/v5/public/funding-rate-history", builder);

        request.jsonParser = (jsonWrapper -> {
            List<FundingRate> result = new LinkedList<>();
            JsonWrapperArray dataArray = jsonWrapper.getJsonArray("data");
            dataArray.forEach(item -> {
                FundingRate element = new FundingRate();
                element.setSymbol(item.getString("instId"));
                element.setFundingRate(item.getBigDecimal("realizedRate"));
                element.setFundingTime(item.getLong("fundingTime"));
                result.add(element);
            });

            return result;
        });
        return request;
    }

    @Override
    public RestApiRequest<FundingRate> getFundingRate(String symbol) {
        RestApiRequest<FundingRate> request = new RestApiRequest<>();
        UrlParamsBuilder builder = UrlParamsBuilder.build()
                .putToUrl("instId", symbol);
        request.request = createRequestByGet("/api/v5/public/funding-rate", builder);

        request.jsonParser = (jsonWrapper -> {
            List<FundingRate> result = new LinkedList<>();
            JsonWrapperArray dataArray = jsonWrapper.getJsonArray("data");
            dataArray.forEach((item) -> {
                FundingRate element = new FundingRate();
                element.setSymbol(item.getString("instId"));
                element.setFundingRate(item.getBigDecimal("fundingRate"));
                element.setFundingTime(item.getLong("fundingTime"));
                result.add(element);
            });

            return result.get(0);

        });
        return request;
    }

    @Override
    public RestApiRequest<List<Candlestick>> getCandlestick(String symbol, CandlestickInterval interval, Long startTime, Long endTime, Integer limit) {
        RestApiRequest<List<Candlestick>> request = new RestApiRequest<>();
        String bar = interval.getCode();
        if (!interval.name().contains("MINUTE")) {
            bar = bar.toUpperCase();
        }
        UrlParamsBuilder builder = UrlParamsBuilder.build()
                .putToUrl("instId", symbol)
                .putToUrl("bar", bar)
                .putToUrl("before", startTime)
                .putToUrl("after", endTime)
                .putToUrl("limit", limit);
        request.request = createRequestByGet("/api/v5/market/candles", builder);

        request.jsonParser = (jsonWrapper -> {
            List<Candlestick> result = new LinkedList<>();
            JsonWrapperArray dataArray = jsonWrapper.getJsonArray("data");
            dataArray.forEachAsArray((item) -> {
                Candlestick element = new Candlestick();
                element.setOpenTime(Long.parseLong(item.getStringAt(0)));
                element.setOpen(item.getBigDecimalAt(1));
                element.setHigh(item.getBigDecimalAt(2));
                element.setLow(item.getBigDecimalAt(3));
                element.setClose(item.getBigDecimalAt(4));
                element.setVolume(item.getBigDecimalAt(6));
                element.setQuoteAssetVolume(item.getBigDecimalAt(7));
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
        UrlParamsBuilder builder = UrlParamsBuilder.build();
        request.request = createRequestByGetWithSignature("/api/v5/account/balance", builder);

        request.jsonParser = (jsonWrapper -> {
            List<AccountBalance> result = new LinkedList<>();
            JsonWrapperArray dataArray = jsonWrapper.getJsonArray("data").getJsonObjectAt(0).getJsonArray("details");
            dataArray.forEach((item) -> {
                AccountBalance element = new AccountBalance();
                element.setAsset(item.getString("ccy"));
                element.setBalance(item.getBigDecimal("eq"));
                element.setAvailableBalance(item.getBigDecimal("availBal"));
                element.setCrossUnPnl(item.getBigDecimal("upl"));
                result.add(element);
            });
            return result;
        });
        return request;
    }

    @Override
    public RestApiRequest<String> postOrder(String symbol, OrderSide side, PositionSide positionSide, OrderType orderType, TimeInForce timeInForce, BigDecimal quantity, BigDecimal price, Boolean reduceOnly, String newClientOrderId, BigDecimal stopPrice, WorkingType workingType) {
        RestApiRequest<String> request = new RestApiRequest<>();
        UrlParamsBuilder builder = UrlParamsBuilder.build()
                .putToPost("instId", symbol)
                .putToPost("tdMode", "cross")
                .putToPost("clOrdId", newClientOrderId)
                .putToPost("side", side.name().toLowerCase())
                .putToPost("ordType", orderType.name().toLowerCase())
                .putToPost("sz", quantity.stripTrailingZeros().toPlainString())
                .putToPost("px", price.stripTrailingZeros().toPlainString())
                .putToPost("reduceOnly", reduceOnly.toString());

        if (!PositionSide.BOTH.equals(positionSide)) {
            builder.putToPost("posSide", positionSide.name().toLowerCase());
        }

        if (TimeInForce.FOK.equals(timeInForce) || TimeInForce.IOC.equals(timeInForce)) {
            builder.putToPost("ordType", timeInForce.name().toLowerCase());
        }

        request.request = createRequestByPostWithSignature("/api/v5/trade/order", builder);

        request.jsonParser = (jsonWrapper -> jsonWrapper.getJsonArray("data").getJsonObjectAt(0).getString("ordId"));
        return request;
    }

    @Override
    public RestApiRequest<String> cancelOrder(String symbol, String orderId, String origClientOrderId) {
        RestApiRequest<String> request = new RestApiRequest<>();
        UrlParamsBuilder builder = UrlParamsBuilder.build()
                .putToPost("instId", symbol)
                .putToPost("ordId", orderId)
                .putToPost("clOrdId", origClientOrderId);
        request.request = createRequestByPostWithSignature("/api/v5/trade/cancel-order", builder);

        request.jsonParser = (jsonWrapper -> jsonWrapper.getJsonArray("data").getJsonObjectAt(0).getString("ordId"));
        return request;
    }

    @Override
    public RestApiRequest<ResponseResult> cancelAllOpenOrder(String symbol) {
        List<Order> orders = RestApiInvoker.callSync(this.getOpenOrders(symbol));

        List<JSONObject> params = orders.stream().map(order -> {
            JSONObject jo = new JSONObject();
            jo.put("instId", symbol);
            jo.put("ordId", order.getOrderId());
            return jo;
        }).collect(Collectors.toList());

        RestApiRequest<ResponseResult> request = new RestApiRequest<>();
        UrlParamsBuilder builder = UrlParamsBuilder.build()
                .putToPost(params);
        request.request = createRequestByPostWithSignature("/api/v5/trade/cancel-batch-orders", builder);

        request.jsonParser = (jsonWrapper -> {
            ResponseResult responseResult = new ResponseResult();
            responseResult.setCode(jsonWrapper.getInteger("code") == 0 ? 200 : jsonWrapper.getInteger("code"));
            responseResult.setMsg(jsonWrapper.getString("msg"));
            return responseResult;
        });
        return request;
    }

    @Override
    public RestApiRequest<Order> getOrder(String symbol, String orderId, String origClientOrderId) {
        RestApiRequest<Order> request = new RestApiRequest<>();
        UrlParamsBuilder builder = UrlParamsBuilder.build()
                .putToUrl("instId", symbol)
                .putToUrl("ordId", orderId)
                .putToUrl("clOrdId", origClientOrderId);
        request.request = createRequestByGetWithSignature("/api/v5/trade/order", builder);

        request.jsonParser = (jsonWrapper -> {
            JsonWrapper item = jsonWrapper.getJsonArray("data").getJsonObjectAt(0);
            return parseOrderDetail(item);
        });
        return request;
    }

    private Order parseOrderDetail(JsonWrapper item) {
        Order element = new Order();
        element.setOrderId(item.getLong("ordId"));
        element.setClientOrderId(item.getString("clOrdId"));
        element.setExecutedQty(item.getBigDecimal("accFillSz"));
        element.setOrigQty(item.getBigDecimal("sz"));
        element.setPrice(StringUtils.isEmpty(item.getString("px")) ? null : item.getBigDecimal("px"));
        element.setSide(OrderSide.valueOf(item.getString("side").toUpperCase()));
        element.setReduceOnly(item.getBoolean("reduceOnly"));
        element.setPositionSide("net".equals(item.getString("posSide")) ? PositionSide.BOTH : PositionSide.valueOf(item.getString("posSide").toUpperCase()));
        String status = item.getString("state");
        if ("live".equalsIgnoreCase(status)) {
            element.setStatus(OrderStatus.NEW);
        } else if ("partially_filled".equalsIgnoreCase(status)) {
            element.setStatus(OrderStatus.PARTIALLY_FILLED);
        } else if ("canceled".equalsIgnoreCase(status)) {
            element.setStatus(OrderStatus.CANCELED);
        } else if ("filled".equalsIgnoreCase(status)) {
            element.setStatus(OrderStatus.FILLED);
        }
        element.setSymbol(item.getString("instId"));
        element.setType("limit".equals(item.getString("ordType")) ? OrderType.LIMIT : OrderType.MARKET);
        element.setUpdateTime(item.getLong("uTime"));
        element.setTime(item.getLong("cTime"));
        return element;
    }

    @Override
    public RestApiRequest<List<Order>> getOpenOrders(String symbol) {
        RestApiRequest<List<Order>> request = new RestApiRequest<>();
        UrlParamsBuilder builder = UrlParamsBuilder.build()
                .putToUrl("instType", "SWAP")
                .putToUrl("instId", symbol);
        request.request = createRequestByGetWithSignature("/api/v5/trade/orders-pending", builder);

        request.jsonParser = (jsonWrapper -> {
            List<Order> result = new LinkedList<>();
            JsonWrapperArray dataArray = jsonWrapper.getJsonArray("data");
            dataArray.forEach((item) -> {
                result.add(parseOrderDetail(item));
            });
            return result;
        });
        return request;
    }

    @Override
    public RestApiRequest<Leverage> changeInitialLeverage(String symbol, Integer leverage) {
        RestApiRequest<Leverage> request = new RestApiRequest<>();
        UrlParamsBuilder builder = UrlParamsBuilder.build()
                .putToUrl("instId", symbol)
                .putToUrl("mgnMode", "cross")
                .putToUrl("lever", leverage);
        request.request = createRequestByPostWithSignature("/api/v5/account/set-leverage", builder);

        request.jsonParser = (jsonWrapper -> {
            Leverage result = new Leverage();
            result.setLeverage(jsonWrapper.getBigDecimal("lever"));
            result.setSymbol(jsonWrapper.getString("instId"));
            return result;
        });
        return request;
    }

    @Override
    public RestApiRequest<OrderBook> getOrderBook(String symbol, Integer limit) {
        RestApiRequest<OrderBook> request = new RestApiRequest<>();
        UrlParamsBuilder builder = UrlParamsBuilder.build()
                .putToUrl("instId", symbol);
        request.request = createRequestByGet("/api/v5/market/books-lite", builder);

        request.jsonParser = (jsonWrapper -> {
            OrderBook result = new OrderBook();
            JsonWrapper data = jsonWrapper.getJsonArray("data").getJsonObjectAt(0);
            result.setLastUpdateId(data.getLong("ts"));

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
        });
        return request;
    }


    @Override
    public RestApiRequest<List<Order>> getAllOrders(String symbol, String orderId, Long startTime, Long endTime, Integer limit) {
        RestApiRequest<List<Order>> request = new RestApiRequest<>();
        UrlParamsBuilder builder = UrlParamsBuilder.build()
                .putToUrl("instType", "SWAP")
                .putToUrl("instId", symbol)
                .putToUrl("begin", startTime)
                .putToUrl("end", endTime)
                .putToUrl("after", orderId)
                .putToUrl("limit", limit);
        request.request = createRequestByGetWithSignature("/api/v5/trade/orders-history", builder);

        request.jsonParser = (jsonWrapper -> {
            List<Order> result = new LinkedList<>();
            JsonWrapperArray dataArray = jsonWrapper.getJsonArray("data");
            dataArray.forEach((item) -> {
                result.add(parseOrderDetail(item));
            });
            return result;
        });
        return request;
    }

    @Override
    public RestApiRequest<List<PositionRisk>> getPositionRisk(String symbol) {
        RestApiRequest<List<PositionRisk>> request = new RestApiRequest<>();
        UrlParamsBuilder builder = UrlParamsBuilder.build()
                .putToUrl("instType", "SWAP")
                .putToUrl("instId", symbol);
        request.request = createRequestByGetWithSignature("/api/v5/account/positions", builder);

        request.jsonParser = (jsonWrapper -> {
            List<PositionRisk> result = new LinkedList<>();
            JsonWrapperArray dataArray = jsonWrapper.getJsonArray("data");
            dataArray.forEach((item) -> {
                PositionRisk positionRisk = new PositionRisk();
                positionRisk.setEntryPrice(item.getBigDecimal("avgPx"));
                positionRisk.setLeverage(item.getBigDecimal("lever"));
                positionRisk.setLiquidationPrice(StringUtils.isEmpty(item.getString("liqPx")) ? null : item.getBigDecimal("liqPx"));
                positionRisk.setMarkPrice(item.getBigDecimal("markPx"));
                positionRisk.setPositionAmt(item.getBigDecimal("pos"));
                positionRisk.setSymbol(item.getString("instId"));
                positionRisk.setIsolatedMargin(StringUtils.isEmpty(item.getString("margin")) ? null : item.getBigDecimal("margin"));
                positionRisk.setPositionSide("net".equals(item.getString("posSide")) ? PositionSide.BOTH : PositionSide.valueOf(item.getString("posSide").toUpperCase()));
                positionRisk.setMarginType(MarginType.valueOf(item.getString("mgnMode")));
                positionRisk.setUnrealizedProfit(item.getBigDecimal("upl"));
                positionRisk.setUpdateTime(item.getLong("uTime"));
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
                .putToUrl("instType", "SWAP")
                .putToUrl("limit", limit)
                .putToUrl("instId", symbol);
        request.request = createRequestByGetWithSignature("/api/v5/trade/fills", builder);

        request.jsonParser = (jsonWrapper -> {
            List<Trade> result = new LinkedList<>();
            JsonWrapperArray dataArray = jsonWrapper.getJsonArray("data");
            dataArray.forEach((item) -> {
                Trade element = new Trade();
                element.setId(item.getLong("tradeId"));
                element.setPrice(item.getBigDecimal("fillPx"));
                element.setQty(item.getBigDecimal("fillSz"));
                element.setTime(item.getLong("fillTime"));
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
        request.jsonParser = (jsonWrapper -> {
            List<SymbolPrice> result = new LinkedList<>();
            JsonWrapperArray dataArray = jsonWrapper.getJsonArray("data");
            dataArray.forEach((item) -> {
                SymbolPrice element = new SymbolPrice();
                element.setSymbol(item.getString("instId"));
                element.setPrice(item.getBigDecimal("last"));
                result.add(element);
            });
            return result;
        });

        if (StringUtils.isEmpty(symbol)) {
            UrlParamsBuilder builder = UrlParamsBuilder.build()
                    .putToUrl("instType", "SWAP");
            request.request = createRequestByGet("/api/v5/market/tickers", builder);
            return request;
        }

        UrlParamsBuilder builder = UrlParamsBuilder.build()
                .putToUrl("instId", symbol);
        request.request = createRequestByGet("/api/v5/market/ticker", builder);
        return request;
    }

    @Override
    public RestApiRequest<List<SymbolOrderBook>> getSymbolOrderBookTicker(String symbol) {
        RestApiRequest<List<SymbolOrderBook>> request = new RestApiRequest<>();
        request.jsonParser = (jsonWrapper -> {
            List<SymbolOrderBook> result = new LinkedList<>();
            JsonWrapperArray dataArray = jsonWrapper.getJsonArray("data");
            dataArray.forEach((item) -> {
                SymbolOrderBook element = new SymbolOrderBook();
                element.setSymbol(item.getString("instId"));
                element.setBidPrice(item.getBigDecimal("bidPx"));
                element.setBidQty(item.getBigDecimal("bidSz"));
                element.setAskPrice(item.getBigDecimal("askPx"));
                element.setAskQty(item.getBigDecimal("askSz"));
                result.add(element);
            });
            return result;
        });

        if (StringUtils.isEmpty(symbol)) {
            UrlParamsBuilder builder = UrlParamsBuilder.build()
                    .putToUrl("instType", "SWAP");
            request.request = createRequestByGet("/api/v5/market/tickers", builder);
            return request;
        }

        UrlParamsBuilder builder = UrlParamsBuilder.build()
                .putToUrl("instId", symbol);
        request.request = createRequestByGet("/api/v5/market/ticker", builder);
        return request;
    }

    @Override
    public RestApiRequest<List<AggregateTrade>> getAggregateTrades(String symbol, String fromId, Long startTime, Long endTime, Integer limit) {
        throw new UnsupportedOperationException();
    }
}
