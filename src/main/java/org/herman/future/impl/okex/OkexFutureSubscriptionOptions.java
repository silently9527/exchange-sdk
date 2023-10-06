package org.herman.future.impl.okex;

import org.herman.future.FutureSubscriptionOptions;

public class OkexFutureSubscriptionOptions extends FutureSubscriptionOptions {
    private String passphrase;
    private String apiKey;
    private String secretKey;

    public OkexFutureSubscriptionOptions(String uri, String apiKey, String secretKey, String passphrase) {
        super(uri);
        this.passphrase = passphrase;
    }

    public String getPassphrase() {
        return passphrase;
    }

    public void setPassphrase(String passphrase) {
        this.passphrase = passphrase;
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
