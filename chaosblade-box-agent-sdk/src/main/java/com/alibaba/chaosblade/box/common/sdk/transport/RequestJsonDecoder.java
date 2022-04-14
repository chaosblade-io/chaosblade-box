package com.alibaba.chaosblade.box.common.sdk.transport;

import com.alibaba.fastjson.JSON;

/**
 * @author Changjun Xiao
 */
public class RequestJsonDecoder implements Decoder<String, Request> {

    @Override
    public Request decode(String json) throws DecoderException {
        try {
            return JSON.parseObject(json, Request.class);
        } catch (Exception e) {
            throw new DecoderException(e);
        }
    }
}
