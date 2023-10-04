package org.herman.future.model.enums;

/**
 * 1min, 5min, 15min, 30min, 60min, 1day, 1mon, 1week, 1year
 */
public enum CandlestickInterval {
    ONE_MINUTE("1m") {
        @Override
        public int getMinutes() {
            return 1;
        }
    },
    THREE_MINUTES("3m") {
        @Override
        public int getMinutes() {
            return 3;
        }
    },
    FIVE_MINUTES("5m") {
        @Override
        public int getMinutes() {
            return 5;
        }
    },
    FIFTEEN_MINUTES("15m") {
        @Override
        public int getMinutes() {
            return 15;
        }
    },
    HALF_HOURLY("30m") {
        @Override
        public int getMinutes() {
            return 30;
        }
    },
    HOURLY("1h") {
        @Override
        public int getMinutes() {
            return 60;
        }
    },
    TWO_HOURLY("2h") {
        @Override
        public int getMinutes() {
            return 60 * 2;
        }
    },
    FOUR_HOURLY("4h") {
        @Override
        public int getMinutes() {
            return 60 * 4;
        }
    },
    SIX_HOURLY("6h") {
        @Override
        public int getMinutes() {
            return 60 * 6;
        }
    },
    EIGHT_HOURLY("8h") {
        @Override
        public int getMinutes() {
            return 60 * 8;
        }
    },
    TWELVE_HOURLY("12h") {
        @Override
        public int getMinutes() {
            return 60 * 12;
        }
    },
    DAILY("1d") {
        @Override
        public int getMinutes() {
            return 60 * 24;
        }
    },
    THREE_DAILY("3d") {
        @Override
        public int getMinutes() {
            return 60 * 24 * 3;
        }
    },
    WEEKLY("1w") {
        @Override
        public int getMinutes() {
            return 60 * 24 * 7;
        }
    },
    MONTHLY("1M") {
        @Override
        public int getMinutes() {
            return 60 * 24 * 30;
        }
    };

    private final String code;

    CandlestickInterval(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return code;
    }

    public abstract int getMinutes();
}
