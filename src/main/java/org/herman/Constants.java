package org.herman;


import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Constants used throughout Binance's API.
 */
public class Constants {


    /**
     * Default ToStringStyle used by toString methods. Override this to change the
     * output format of the overridden toString methods. - Example
     * ToStringStyle.JSON_STYLE
     */
    public static ToStringStyle TO_STRING_BUILDER_STYLE = ToStringStyle.SHORT_PREFIX_STYLE;


    public static class Future {
        /**
         * REST API base URL.
         */
        public static final String BINANCE_REST_API_BASE_URL = "https://fapi.binance.com";
        public static final String OKEX_REST_API_BASE_URL = "https://www.okx.com";
        public static final String KUCOIN_REST_API_BASE_URL = "https://api-futures.kucoin.com";

        /**
         * Streaming API base URL.
         */
        public static final String BINANCE_WS_API_BASE_URL = "wss://fstream.binance.com/stream";
        public static final String OKEX_PUBLIC_WS_API_BASE_URL = "wss://ws.okx.com:8443/ws/v5/public";
        public static final String OKEX_PRIVATE_WS_API_BASE_URL = "wss://ws.okx.com:8443/ws/v5/private";
        public static final String OKEX_BUSINESS_WS_API_BASE_URL = "wss://ws.okx.com:8443/ws/v5/business";

        /**
         * Default receiving window.
         */
        public static final long DEFAULT_RECEIVING_WINDOW = 60_000L;
    }

}
