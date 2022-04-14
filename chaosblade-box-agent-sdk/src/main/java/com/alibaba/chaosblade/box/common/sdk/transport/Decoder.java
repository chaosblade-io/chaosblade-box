package com.alibaba.chaosblade.box.common.sdk.transport;

/**
 * @author changjun.xcj
 */
public interface Decoder<I, O> {
    /**
     * Decode
     *
     * @param i input
     * @return output
     * @throws DecoderException
     */
    O decode(I i) throws DecoderException;
}
