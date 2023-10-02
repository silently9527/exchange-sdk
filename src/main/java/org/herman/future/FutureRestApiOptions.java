package org.herman.future;


import org.herman.exception.ApiException;

import java.net.URL;

public class FutureRestApiOptions {

    private String url;
    protected String apiKey;
    protected String secretKey;
    protected String passphrase;

    public FutureRestApiOptions(String url, String apiKey, String secretKey, String passphrase) {
        this.url = url;
        this.apiKey = apiKey;
        this.secretKey = secretKey;
        this.passphrase = passphrase;
    }

    public FutureRestApiOptions(String url, String apiKey, String secretKey) {
        this(url, apiKey, secretKey, "");
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


    /**
     * Set the URL for request.
     *
     * @param url The URL name like "https://fapi.binance.com".
     */
    public void setUrl(String url) {
        try {
            URL u = new URL(url);
            this.url = u.toString();
        } catch (Exception e) {
            throw new ApiException(ApiException.INPUT_ERROR, "The URI is incorrect: " + e.getMessage());
        }
        this.url = url;
    }

    public String getPassphrase() {
        return passphrase;
    }

    public void setPassphrase(String passphrase) {
        this.passphrase = passphrase;
    }

    public String getUrl() {
        return url;
    }
}
