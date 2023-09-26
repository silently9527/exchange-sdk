package org.herman.future.impl;

import okhttp3.Request;

public class RestApiRequest<T> {

    public Request request;
    public RestApiJsonParser<T> jsonParser;
}
