package org.herman.future;

import org.herman.future.impl.RestApiRequestClient;
import org.herman.future.impl.SyncFutureRequestClientImpl;
import org.herman.future.impl.binance.BinanceRestApiRequestClient;

public final class FutureApiInternalFactory {

    private static final FutureApiInternalFactory instance = new FutureApiInternalFactory();

    public static FutureApiInternalFactory getInstance() {
        return instance;
    }

    private FutureApiInternalFactory() {
    }

    public FutureRequestClient createBinanceFutureRequestClient(String apiKey, String secretKey) {
        RestApiRequestClient requestImpl = new BinanceRestApiRequestClient(apiKey, secretKey);
        return new SyncFutureRequestClientImpl(requestImpl);
    }

//    public FutureSubscriptionClient createFutureSubscriptionClient(FutureSubscriptionOptions options) {
//        FutureSubscriptionOptions subscriptionOptions = new FutureSubscriptionOptions(options);
//        FutureRequestOptions futureRequestOptions = new FutureRequestOptions();
//        try {
//            String host = new URI(options.getUri()).getHost();
//            futureRequestOptions.setUrl("https://" + host);
//        } catch (Exception e) {
//
//        }
//        FutureSubscriptionClient webSocketStreamClient = new WebSocketStreamClientImpl(subscriptionOptions);
//        return webSocketStreamClient;
//    }

}
