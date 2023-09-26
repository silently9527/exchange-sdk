package org.herman.future.impl.okex;

import org.herman.Constants;
import org.herman.future.impl.AbstractRestApiRequestClient;
import org.herman.future.impl.RestApiRequest;
import org.herman.future.model.ResponseResult;
import org.herman.future.model.enums.*;
import org.herman.future.model.market.Candlestick;
import org.herman.future.model.market.ExchangeInformation;
import org.herman.future.model.market.FundingRate;
import org.herman.future.model.market.MarkPrice;
import org.herman.future.model.trade.AccountBalance;
import org.herman.future.model.trade.AccountInformation;
import org.herman.future.model.trade.Leverage;
import org.herman.future.model.trade.Order;
import org.herman.utils.UrlParamsBuilder;

import java.math.BigDecimal;
import java.util.List;

public class OkexRestApiRequestClient extends AbstractRestApiRequestClient {

    public OkexRestApiRequestClient(String apiKey, String secretKey) {
        this.apiKey = apiKey;
        this.secretKey = secretKey;
        this.serverUrl = Constants.Future.OKEX_REST_API_BASE_URL;
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
            System.out.println(jsonWrapper);
            ExchangeInformation result = new ExchangeInformation();
//            result.setTimezone(jsonWrapper.getString("timezone"));
//            result.setServerTime(jsonWrapper.getLong("serverTime"));
//
//            List<RateLimit> elementList = new LinkedList<>();
//            JsonWrapperArray dataArray = jsonWrapper.getJsonArray("rateLimits");
//            dataArray.forEach((item) -> {
//                RateLimit element = new RateLimit();
//                element.setRateLimitType(item.getString("rateLimitType"));
//                element.setInterval(item.getString("interval"));
//                element.setIntervalNum(item.getLong("intervalNum"));
//                element.setLimit(item.getLong("limit"));
//                elementList.add(element);
//            });
//            result.setRateLimits(elementList);
//
//            List<ExchangeFilter> filterList = new LinkedList<>();
//            JsonWrapperArray filterArray = jsonWrapper.getJsonArray("exchangeFilters");
//            filterArray.forEach((item) -> {
//                ExchangeFilter filter = new ExchangeFilter();
//                filter.setFilterType(item.getString("filterType"));
//                filter.setMaxNumOrders(item.getLong("maxNumOrders"));
//                filter.setMaxNumAlgoOrders(item.getLong("maxNumAlgoOrders"));
//                filterList.add(filter);
//            });
//            result.setExchangeFilters(filterList);
//
//            List<ExchangeInfoEntry> symbolList = new LinkedList<>();
//            JsonWrapperArray symbolArray = jsonWrapper.getJsonArray("symbols");
//            symbolArray.forEach((item) -> {
//                ExchangeInfoEntry symbol = new ExchangeInfoEntry();
//                symbol.setSymbol(item.getString("symbol"));
//                symbol.setStatus(item.getString("status"));
//                symbol.setMaintMarginPercent(item.getBigDecimal("maintMarginPercent"));
//                symbol.setRequiredMarginPercent(item.getBigDecimal("requiredMarginPercent"));
//                symbol.setBaseAsset(item.getString("baseAsset"));
//                symbol.setContractType(item.getString("contractType"));
//                symbol.setOnboardDate(item.getLong("onboardDate"));
//                symbol.setQuoteAsset(item.getString("quoteAsset"));
//                symbol.setPricePrecision(item.getLong("pricePrecision"));
//                symbol.setQuantityPrecision(item.getLong("quantityPrecision"));
//                symbol.setBaseAssetPrecision(item.getLong("baseAssetPrecision"));
//                symbol.setQuotePrecision(item.getLong("quotePrecision"));
//                symbol.setOrderTypes(item.getJsonArray("orderTypes").convert2StringList());
//                symbol.setTimeInForce(item.getJsonArray("orderTypes").convert2StringList());
//                List<List<Map<String, String>>> valList = new LinkedList<>();
//                JsonWrapperArray valArray = item.getJsonArray("filters");
//                valArray.forEach((val) -> {
//                    valList.add(val.convert2DictList());
//                });
//                symbol.setFilters(valList);
//                symbolList.add(symbol);
//            });
//            result.setSymbols(symbolList);

            return result;
        });
        return request;
    }

    @Override
    public RestApiRequest<List<MarkPrice>> getMarkPrice(String symbol) {
        return null;
    }

    @Override
    public RestApiRequest<List<FundingRate>> getFundingRateHistory(String symbol, Long startTime, Long endTime, Integer limit) {
        return null;
    }

    @Override
    public RestApiRequest<FundingRate> getFundingRate(String symbol) {
        return null;
    }

    @Override
    public RestApiRequest<List<Candlestick>> getCandlestick(String symbol, CandlestickInterval interval, Long startTime, Long endTime, Integer limit) {
        return null;
    }

    @Override
    public RestApiRequest<AccountInformation> getAccountInformation() {
        return null;
    }

    @Override
    public RestApiRequest<List<AccountBalance>> getBalance() {
        return null;
    }

    @Override
    public RestApiRequest<String> postOrder(String symbol, OrderSide side, PositionSide positionSide, OrderType orderType, TimeInForce timeInForce, BigDecimal quantity, BigDecimal price, Boolean reduceOnly, String newClientOrderId, BigDecimal stopPrice, WorkingType workingType) {
        return null;
    }

    @Override
    public RestApiRequest<String> cancelOrder(String symbol, String orderId, String origClientOrderId) {
        return null;
    }

    @Override
    public RestApiRequest<ResponseResult> cancelAllOpenOrder(String symbol) {
        return null;
    }

    @Override
    public RestApiRequest<Order> getOrder(String symbol, String orderId, String origClientOrderId) {
        return null;
    }

    @Override
    public RestApiRequest<List<Order>> getOpenOrders(String symbol) {
        return null;
    }

    @Override
    public RestApiRequest<Leverage> changeInitialLeverage(String symbol, Integer leverage) {
        return null;
    }
}
