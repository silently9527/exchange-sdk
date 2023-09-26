package org.herman.future;

import okhttp3.*;
import org.herman.exception.ApiException;
import org.herman.future.impl.RestApiRequest;
import org.herman.utils.JsonWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class RestApiInvoker {

    private static final Logger log = LoggerFactory.getLogger(RestApiInvoker.class);
    private static final OkHttpClient client = new OkHttpClient();

    public static void checkResponse(JsonWrapper json) {
        try {
            if (json.containKey("success")) {
                boolean success = json.getBoolean("success");
                if (!success) {
                    String err_code = json.getStringOrDefault("code", "");
                    String err_msg = json.getStringOrDefault("msg", "");
                    if ("".equals(err_code)) {
                        throw new ApiException(ApiException.EXEC_ERROR, "[Executing] " + err_msg);
                    } else {
                        throw new ApiException(ApiException.EXEC_ERROR,
                                "[Executing] " + err_code + ": " + err_msg);
                    }
                }
            } else if (json.containKey("code")) {

                int code = json.getInteger("code");
                if (code != 200) {
                    String message = json.getStringOrDefault("msg", "");
                    throw new ApiException(ApiException.EXEC_ERROR,
                            "[Executing] " + code + ": " + message);
                }
            }
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            throw new ApiException(ApiException.RUNTIME_ERROR,
                    "[Invoking] Unexpected error: " + e.getMessage());
        }
    }

    public static <T> T callSync(RestApiRequest<T> request) {
        try {
            String str;
            log.debug("Request URL " + request.request.url());
            Response response = client.newCall(request.request).execute();
            // System.out.println(response.body().string());
            if (response != null && response.body() != null) {
                str = response.body().string();
                response.close();
            } else {
                throw new ApiException(ApiException.ENV_ERROR,
                        "[Invoking] Cannot get the response from server");
            }
            log.debug("Response =====> " + str);
            JsonWrapper jsonWrapper = JsonWrapper.parseFromString(str);
            checkResponse(jsonWrapper);
            return request.jsonParser.parseJson(jsonWrapper);
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            throw new ApiException(ApiException.ENV_ERROR,
                    "[Invoking] Unexpected error: " + e.getMessage());
        }
    }

    public static WebSocket createWebSocket(Request request, WebSocketListener listener) {
        return client.newWebSocket(request, listener);
    }

}
