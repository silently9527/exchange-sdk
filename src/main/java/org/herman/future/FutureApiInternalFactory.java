package org.herman.future;

import org.herman.Constants;
import org.herman.future.impl.*;
import org.herman.future.impl.binance.BinanceFutureSubscriptionOptions;
import org.herman.future.impl.binance.BinanceRestApiRequestClient;
import org.herman.future.impl.binance.BinanceWebsocketRequestClient;
import org.herman.future.impl.kucoin.KucoinFutureSubscriptionOptions;
import org.herman.future.impl.kucoin.KucoinRestApiRequestClient;
import org.herman.future.impl.kucoin.KucoinWebsocketRequestClient;
import org.herman.future.impl.okex.OkexFutureSubscriptionOptions;
import org.herman.future.impl.okex.OkexRestApiRequestClient;
import org.herman.future.impl.okex.OkexWebsocketRequestClient;

public final class FutureApiInternalFactory {

    private static final FutureApiInternalFactory instance = new FutureApiInternalFactory();

    public static FutureApiInternalFactory getInstance() {
        return instance;
    }

    private FutureApiInternalFactory() {
    }

    public FutureRestApiClient createKucoinFutureRestApiClient(String url, String apiKey, String secretKey, String passphrase) {
        RestApiRequestClient requestImpl = new KucoinRestApiRequestClient(url, apiKey, secretKey, passphrase);
        return new FutureRestApiClientImpl(requestImpl);
    }

    public FutureRestApiClient createBinanceFutureRestApiClient(String url, String apiKey, String secretKey) {
        RestApiRequestClient requestImpl = new BinanceRestApiRequestClient(url, apiKey, secretKey);
        return new FutureRestApiClientImpl(requestImpl);
    }

    public FutureRestApiClient createOkexFutureRestApiClient(String url, String apiKey, String secretKey, String passphrase) {
        RestApiRequestClient requestImpl = new OkexRestApiRequestClient(url, apiKey, secretKey, passphrase);
        return new FutureRestApiClientImpl(requestImpl);
    }

    public FutureSubscriptionClient createBinanceFutureSubscriptionClient(String uri, String apiKey, String secretKey) {
        BinanceFutureSubscriptionOptions options = new BinanceFutureSubscriptionOptions(uri, apiKey, secretKey);
        WebsocketRequestClient requestImpl = new BinanceWebsocketRequestClient(options);
        return new WebSocketFutureSubscriptionClient(options, requestImpl);
    }

    public FutureSubscriptionClient createOkexFutureSubscriptionClient(String uri, String apiKey, String secretKey, String passphrase) {
        OkexFutureSubscriptionOptions options = new OkexFutureSubscriptionOptions(uri, apiKey, secretKey, passphrase);
        WebsocketRequestClient requestImpl = new OkexWebsocketRequestClient(options);
        return new WebSocketFutureSubscriptionClient(options, requestImpl);
    }

    public FutureSubscriptionClient createKucoinFutureSubscriptionClient(String url, String apiKey, String secretKey, String passphrase, boolean isPrivate) {
        KucoinRestApiRequestClient requestImpl = new KucoinRestApiRequestClient(url, apiKey, secretKey, passphrase);

        String streamUrl;
        if (isPrivate) {
            streamUrl = RestApiInvoker.callSync(requestImpl.getPrivateEndpoint());
        } else {
            streamUrl = RestApiInvoker.callSync(requestImpl.getPublicEndpoint());

        }
        KucoinFutureSubscriptionOptions options = new KucoinFutureSubscriptionOptions(streamUrl);
        return new WebSocketFutureSubscriptionClient(options, new KucoinWebsocketRequestClient());
    }

}
