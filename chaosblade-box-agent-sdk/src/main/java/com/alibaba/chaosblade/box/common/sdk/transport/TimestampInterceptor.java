package com.alibaba.chaosblade.box.common.sdk.transport;

/**
 * Add and check timestamp
 *
 * @author changjun.xcj
 */
public class TimestampInterceptor extends RequestInterceptorChain {

    /**
     * max invalid request time (millis)
     */
    public static final int MAX_INVALID_TIME = 2 * 60 * 1000;
    public static final String TIMESTAMP_KEY = "ts";

    @Override
    public void doHandle(Request request) throws RequestException {
        String ts = request.getParam("ts");
        if (ts == null || ts.length() == 0) {
            throw new InvalidTimestampException("missing timestamp");
        }
        try {
            long clientTime = Long.valueOf(ts);
            long serverTime = System.currentTimeMillis();
            if (serverTime - clientTime > MAX_INVALID_TIME) {
                throw new InvalidTimestampException("exceeding maximum failure time, server time: " + serverTime + ", "
                        + "client time: " + clientTime);
            }
        } catch (NumberFormatException e) {
            throw new InvalidTimestampException(ts + ", " + e.getMessage());
        }
    }

    @Override
    public void doInvoke(Request request) throws RequestException {
        request.addParam(TIMESTAMP_KEY, System.currentTimeMillis() + "");
    }
}
