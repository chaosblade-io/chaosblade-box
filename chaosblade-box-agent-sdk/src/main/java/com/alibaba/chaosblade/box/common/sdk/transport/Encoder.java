package com.alibaba.chaosblade.box.common.sdk.transport;

/**
 * @author changjun.xcj
 */
public interface Encoder<I, O> {
    /**
     * Encode
     *
     * @param i
     * @return
     * @throws EncoderException
     */
     O encode(I i) throws EncoderException;
}
