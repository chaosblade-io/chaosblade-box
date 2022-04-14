package com.alibaba.chaosblade.box.common.sdk.transport;

/**
 * @author changjun.xcj
 */
public class EncoderException extends RequestException {

    public EncoderException() {
    }

    public EncoderException(String message) {
        super(message);
    }

    public EncoderException(String message, Throwable cause) {
        super(message, cause);
    }

    public EncoderException(Throwable cause) {
        super(cause);
    }
}
