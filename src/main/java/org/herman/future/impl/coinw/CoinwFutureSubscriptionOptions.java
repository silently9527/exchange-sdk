package org.herman.future.impl.coinw;

import org.herman.future.FutureSubscriptionOptions;

public class CoinwFutureSubscriptionOptions extends FutureSubscriptionOptions {
    protected String apiKey;
    protected String secretKey;

    public CoinwFutureSubscriptionOptions(String uri, String apiKey, String secretKey) {
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
