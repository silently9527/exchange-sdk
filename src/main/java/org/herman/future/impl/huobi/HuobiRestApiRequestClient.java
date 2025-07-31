//package org.herman.future.impl.huobi;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//import okhttp3.Request;
//import okhttp3.RequestBody;
//import org.herman.exception.ApiException;
//import org.herman.future.RestApiInvoker;
//import org.herman.future.impl.AbstractRestApiRequestClient;
//import org.herman.future.impl.RestApiRequest;
//import org.herman.future.model.ResponseResult;
//import org.herman.future.model.enums.*;
//import org.herman.future.model.market.*;
//import org.herman.future.model.trade.*;
//import org.herman.utils.JsonWrapper;
//import org.herman.utils.JsonWrapperArray;
//import org.herman.utils.UrlParamsBuilder;
//
//import java.math.BigDecimal;
//import java.math.RoundingMode;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Map;
//import java.util.Properties;
//import java.util.stream.Collectors;
//
//import static org.herman.utils.UrlParamsBuilder.JSON_TYPE;
//
//public class HuobiRestApiRequestClient extends AbstractRestApiRequestClient {
//
//    public HuobiRestApiRequestClient(String serverUrl, String apiKey, String secretKey) {
//        super(apiKey, secretKey, serverUrl);
//    }
//
//    @Override
//    protected Request createRequestWithSignature(String url, String address, UrlParamsBuilder builder) {
//        if (builder == null) {
//            throw new ApiException(ApiException.RUNTIME_ERROR,
//                    "[Invoking] Builder is null when create request with Signature");
//        }
//        String requestUrl = url + address;
//        ApiSignature.createSignature(apiKey, secretKey, requestUrl, builder);
//
//        requestUrl += builder.buildUrl();
//        final JSONObject params = JSON.parseObject(builder.buildBodToJsonString());
//        RequestBody body = RequestBody.create(JSON_TYPE, JSON.toJSONString(params));
//
//        return new Request.Builder().url(requestUrl + "?" + toQueryString(params)).post(body).build();
//    }
//
//    private String toQueryString(Map<String, Object> params) {
//        return String.join("&",
//                params.entrySet().stream()
//                        .map((entry) -> entry.getKey() + "=" + ApiSignature.urlEncode(entry.getValue().toString()))
//                        .collect(Collectors.toList()));
//    }
//
//    @Override
//    protected String getClientSdkVersion() {
//        return "huobi_futures-1.0.0-java";
//    }
//
//    @Override
//    public RestApiRequest<List<Future>> getFutures() {
//        RestApiRequest<List<Future>> request = new RestApiRequest<>();
//        UrlParamsBuilder builder = UrlParamsBuilder.build();
//        request.request = createRequestByGet("/linear-swap-api/v1/swap_contract_info", builder);
//
//        request.jsonParser = (jsonWrapper -> {
//            List<Future> futures = new LinkedList<>();
//            JsonWrapperArray symbolArray = jsonWrapper.getJsonArray("data");
//            symbolArray.forEach((item) -> {
//                Future entry = parseFuture(item);
//                futures.add(entry);
//            });
//            return futures;
//        });
//        return request;
//    }
//
//    private Future parseFuture(JsonWrapper item) {
//        Future entry = new Future();
//        entry.setSymbol(item.getString("contract_code"));
//        entry.setBaseAsset(item.getString("symbol"));
//        entry.setQuoteAsset(item.getString("trade_partition"));
//
//        entry.setStatus(FutureStatus.TRADING);
//
//        entry.setOnboardDate(item.getLong("create_date"));
//        entry.setFutureType("swap".equals(item.getString("business_type")) ? FutureType.PERPETUAL : FutureType.SETTLEMENT);
//
//        entry.setMultiplier(item.getBigDecimal("contract_size"));
//        entry.setTickSize(item.getBigDecimal("price_tick"));
//        entry.setSource(item);
//
//
//        RestApiRequest<Integer> request = new RestApiRequest<>();
//        UrlParamsBuilder builder = UrlParamsBuilder.build();
//        builder.putToPost("contract_code", entry.getSymbol());
//        builder.putToPost("contract_type", item.getString("business_type"));
//        request.request = createRequestByPostWithSignature("/linear-swap-api/v1/swap_cross_available_level_rate", builder);
//
//        request.jsonParser = (jsonWrapper -> {
//            final JsonWrapper data = jsonWrapper.getJsonArray("data").getJsonObjectAt(0);
//            final String[] available_level_rates = data.getString("available_level_rate").split(",");
//            return Integer.parseInt(available_level_rates[available_level_rates.length - 1]);
//        });
//
//        final Integer maxLeverage = RestApiInvoker.callSync(request);
//        entry.setMaxLeverage(maxLeverage);
//        return entry;
//    }
//
//    @Override
//    public RestApiRequest<Future> getFuture(String symbol) {
//        RestApiRequest<Future> request = new RestApiRequest<>();
//        UrlParamsBuilder builder = UrlParamsBuilder.build();
//        builder.putToUrl("contract_code", symbol);
//        request.request = createRequestByGet("/linear-swap-api/v1/swap_contract_info", builder);
//
//        request.jsonParser = (jsonWrapper -> {
//            JsonWrapper item = jsonWrapper.getJsonArray("data").getJsonObjectAt(0);
//            return parseFuture(item);
//        });
//        return request;
//    }
//
//    @Override
//    public RestApiRequest<List<MarkPrice>> getMarkPrice(String symbol) {
//        RestApiRequest<List<MarkPrice>> request = new RestApiRequest<>();
//        UrlParamsBuilder builder = UrlParamsBuilder.build();
//        builder.putToUrl("contract_code", symbol);
//        builder.putToUrl("period", "1min");
//        builder.putToUrl("size", 1);
//        request.request = createRequestByGet("/index/market/history/linear_swap_mark_price_kline", builder);
//
//        request.jsonParser = (jsonWrapper -> {
//            List<MarkPrice> result = new LinkedList<>();
//
//            JsonWrapper data = jsonWrapper.getJsonArray("data").getJsonObjectAt(0);
//            MarkPrice element = new MarkPrice();
//            element.setSymbol(symbol);
//            element.setMarkPrice(data.getBigDecimal("close"));
//            element.setTime(data.getLong("id"));
//
//            result.add(element);
//            return result;
//        });
//        return request;
//    }
//
//    @Override
//    public RestApiRequest<List<FundingRate>> getFundingRateHistory(String symbol, Long startTime, Long endTime, Integer limit) {
//        throw new UnsupportedOperationException();
//    }
//
//    @Override
//    public RestApiRequest<FundingRate> getFundingRate(String symbol) {
//        throw new UnsupportedOperationException();
//    }
//
//    @Override
//    public RestApiRequest<List<Candlestick>> getCandlestick(String symbol, CandlestickInterval interval, Long startTime, Long endTime, Integer limit) {
//        throw new UnsupportedOperationException();
//    }
//
//    @Override
//    public RestApiRequest<AccountInformation> getAccountInformation() {
//        throw new UnsupportedOperationException();
//    }
//
//    @Override
//    public RestApiRequest<List<AccountBalance>> getBalance() {
//        throw new UnsupportedOperationException();
//    }
//
//    @Override
//    public RestApiRequest<String> postOrder(String symbol, OrderSide side, OrderType orderType, BigDecimal quantity, BigDecimal price, Properties ext) {
//        throw new UnsupportedOperationException();
//    }
//
//    @Override
//    public RestApiRequest<String> cancelOrder(String symbol, String orderId, String origClientOrderId) {
//        throw new UnsupportedOperationException();
//    }
//
//    @Override
//    public RestApiRequest<ResponseResult> cancelAllOpenOrder(String symbol) {
//        throw new UnsupportedOperationException();
//    }
//
//    @Override
//    public RestApiRequest<Order> getOrder(String symbol, String orderId, String origClientOrderId) {
//        throw new UnsupportedOperationException();
//    }
//
//    @Override
//    public RestApiRequest<List<Order>> getOpenOrders(String symbol) {
//        throw new UnsupportedOperationException();
//    }
//
//    @Override
//    public RestApiRequest<Leverage> changeInitialLeverage(String symbol, Integer leverage) {
//        throw new UnsupportedOperationException();
//    }
//
//    @Override
//    public RestApiRequest<OrderBook> getOrderBook(String symbol, Integer limit) {
//        throw new UnsupportedOperationException();
//    }
//
//    @Override
//    public RestApiRequest<List<Order>> getAllOrders(String symbol, String currentPage, Long startTime, Long endTime, Integer limit) {
//        throw new UnsupportedOperationException();
//    }
//
//    @Override
//    public RestApiRequest<List<PositionRisk>> getPositionRisk(String symbol) {
//        throw new UnsupportedOperationException();
//    }
//
//    @Override
//    public RestApiRequest<List<Trade>> getRecentTrades(String symbol, Integer limit) {
//        throw new UnsupportedOperationException();
//    }
//
//    @Override
//    public RestApiRequest<List<SymbolPrice>> getSymbolPriceTicker(String symbol) {
//        throw new UnsupportedOperationException();
//    }
//
//    @Override
//    public RestApiRequest<List<SymbolOrderBook>> getSymbolOrderBookTicker(String symbol) {
//        RestApiRequest<List<SymbolOrderBook>> request = new RestApiRequest<>();
//
//        UrlParamsBuilder builder = UrlParamsBuilder.build()
//                .putToUrl("contract_code", symbol);
//        request.request = createRequestByGet("/linear-swap-ex/market/bbo", builder);
//
//        request.jsonParser = (jsonWrapper -> {
//            List<SymbolOrderBook> result = new LinkedList<>();
//            JsonWrapper data = jsonWrapper.getJsonArray("ticks").getJsonObjectAt(0);
//            SymbolOrderBook element = new SymbolOrderBook();
//
//            element.setSymbol(data.getString("contract_code"));
//
//            final JsonWrapperArray bid = data.getJsonArray("bid");
//            element.setBidPrice(new BigDecimal(bid.getObjectAt(0).toString()));
//            element.setBidQty(new BigDecimal(bid.getObjectAt(1).toString()));
//
//            final JsonWrapperArray ask = data.getJsonArray("ask");
//            element.setAskPrice(new BigDecimal(ask.getObjectAt(0).toString()));
//            element.setAskQty(new BigDecimal(ask.getObjectAt(1).toString()));
//
//            result.add(element);
//            return result;
//        });
//        return request;
//    }
//
//    @Override
//    public RestApiRequest<List<AggregateTrade>> getAggregateTrades(String symbol, String fromId, Long startTime, Long endTime, Integer limit) {
//        throw new UnsupportedOperationException();
//    }
//
//    @Override
//    public BigDecimal formatTradeSize(BigDecimal multiplier, BigDecimal tradeSize) {
//        return tradeSize.divide(multiplier, 0, RoundingMode.DOWN);
//    }
//
//    @Override
//    public RestApiRequest<PositionRisk> addIsolatedMargin(String symbol, BigDecimal margin) {
//        throw new UnsupportedOperationException();
//    }
//
//    @Override
//    public RestApiRequest<String> switchMarginMode(String symbol, String marginMode) {
//        throw new UnsupportedOperationException();
//    }
//
//    @Override
//    public RestApiRequest<String> getMarginMode(String symbol) {
//        throw new UnsupportedOperationException();
//    }
//
//    @Override
//    public RestApiRequest<MaxOpenSize> getMaxOpenSize(String symbol, BigDecimal price, Integer leverage) {
//        throw new UnsupportedOperationException();
//    }
//
//    @Override
//    public RestApiRequest<List<LeverageBracket>> getLeverageBrackets(String symbol) {
//        return null;
//    }
//
//    @Override
//    public RestApiRequest<List<OpenInterestStat>> getOpenInterestHistory(String symbol, String period, Integer limit, Long startTime, Long endTime) {
//        return null;
//    }
//
//    @Override
//    public RestApiRequest<Boolean> changePositionMode(PositionMode positionMode) {
//        return null;
//    }
//
//}
