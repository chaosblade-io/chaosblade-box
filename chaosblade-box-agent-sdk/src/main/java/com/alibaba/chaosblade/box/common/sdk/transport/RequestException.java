package com.alibaba.chaosblade.box.common.sdk.transport;

/**
 * @author changjun.xcj
 */
public class RequestException extends Exception {
    public RequestException() {
        super();
    }

    public RequestException(String message) {
        super(message);
    }

    public RequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public RequestException(Throwable e) {
        super(e);
    }
}
