package org.herman.future.impl.binance;

import org.herman.future.FutureSubscriptionOptions;

public class BinanceFutureSubscriptionOptions extends FutureSubscriptionOptions {
    protected String apiKey;
    protected String secretKey;

    public BinanceFutureSubscriptionOptions(String uri, String apiKey, String secretKey) {
        super(uri);
        this.apiKey = apiKey;
        this.secretKey = secretKey;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
}
