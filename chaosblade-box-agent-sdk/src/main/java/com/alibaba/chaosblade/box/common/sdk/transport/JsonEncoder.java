package com.alibaba.chaosblade.box.common.sdk.transport;

import com.alibaba.fastjson.JSON;

/**
 * @author changjun.xcj
 */
public class JsonEncoder implements Encoder<Object, String> {

    @Override
    public String encode(Object object) throws EncoderException {
        try {
            return JSON.toJSONString(object);
        } catch (Exception e) {
            throw new EncoderException(e);
        }
    }
}
