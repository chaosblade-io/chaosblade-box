package com.alibaba.chaosblade.box.common.sdk.transport;

/**
 * @author changjun.xcj
 */
public class DecoderException extends RequestException {

    public DecoderException() {
    }

    public DecoderException(String message) {
        super(message);
    }

    public DecoderException(String message, Throwable cause) {
        super(message, cause);
    }

    public DecoderException(Throwable cause) {
        super(cause);
    }
}
