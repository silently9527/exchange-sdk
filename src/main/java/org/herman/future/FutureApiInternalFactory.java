package org.herman.future;

import org.herman.future.impl.FutureRestApiClientImpl;
import org.herman.future.impl.RestApiRequestClient;
import org.herman.future.impl.WebSocketFutureSubscriptionClient;
import org.herman.future.impl.WebsocketRequestClient;
import org.herman.future.impl.binance.BinanceRestApiRequestClient;
import org.herman.future.impl.binance.BinanceWebsocketRequestClient;
import org.herman.future.impl.kucoin.KucoinRestApiRequestClient;
import org.herman.future.impl.okex.OkexRestApiRequestClient;
import org.herman.future.impl.okex.OkexWebsocketRequestClient;

public final class FutureApiInternalFactory {

    private static final FutureApiInternalFactory instance = new FutureApiInternalFactory();

    public static FutureApiInternalFactory getInstance() {
        return instance;
    }

    private FutureApiInternalFactory() {
    }

    public FutureRestApiClient createKucoinFutureRequestClient(FutureRestApiOptions options) {
        RestApiRequestClient requestImpl = new KucoinRestApiRequestClient(options);
        return new FutureRestApiClientImpl(requestImpl);
    }

    public FutureRestApiClient createBinanceFutureRequestClient(FutureRestApiOptions options) {
        RestApiRequestClient requestImpl = new BinanceRestApiRequestClient(options);
        return new FutureRestApiClientImpl(requestImpl);
    }

    public FutureRestApiClient createOkexFutureRequestClient(FutureRestApiOptions options) {
        RestApiRequestClient requestImpl = new OkexRestApiRequestClient(options);
        return new FutureRestApiClientImpl(requestImpl);
    }

    public FutureSubscriptionClient createBinanceFutureSubscriptionClient(FutureSubscriptionOptions options) {
        WebsocketRequestClient requestImpl = new BinanceWebsocketRequestClient(options);
        return new WebSocketFutureSubscriptionClient(options, requestImpl);
    }

    public FutureSubscriptionClient createOkexFutureSubscriptionClient(FutureSubscriptionOptions options) {
        WebsocketRequestClient requestImpl = new OkexWebsocketRequestClient(options);
        return new WebSocketFutureSubscriptionClient(options, requestImpl);
    }

}
