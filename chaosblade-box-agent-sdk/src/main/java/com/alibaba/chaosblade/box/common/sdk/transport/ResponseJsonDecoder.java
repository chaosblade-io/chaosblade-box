package com.alibaba.chaosblade.box.common.sdk.transport;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.chaosblade.box.common.common.domain.response.Response;

/**
 * @author Changjun Xiao
 */
public class ResponseJsonDecoder<R> implements Decoder<String, Response<R>> {

    private Class<?> clazz;

    /**
     * type argument
     *
     * @param clazz
     */
    public ResponseJsonDecoder(Class<?> clazz) {
        this.clazz = clazz;
    }

    @Override
    public Response<R> decode(String json) throws DecoderException {
        try {
            return JSON.parseObject(json, new TypeReference<Response<R>>(clazz) {
            });
        } catch (Exception e) {
            throw new DecoderException(e);
        }
    }
}
