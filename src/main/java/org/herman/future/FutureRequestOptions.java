package org.herman.future;


import org.herman.exception.ApiException;

import java.net.URL;

public class FutureRequestOptions {

    private String url;

    public FutureRequestOptions() {
    }

    public FutureRequestOptions(FutureRequestOptions option) {
        this.url = option.url;
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

    public String getUrl() {
        return url;
    }
}
