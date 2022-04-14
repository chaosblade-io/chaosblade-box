package com.alibaba.chaosblade.box.common.infrastructure.util;

import com.alibaba.fastjson.JSON;
import feign.FeignException;
import feign.Response;
import feign.codec.DecodeException;
import feign.codec.Decoder;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;

/**
 * @author haibin
 * 
 *
 */
public class FastJsonDecoder implements Decoder {
    @Override
    public Object decode(Response response, Type type) throws IOException, DecodeException, FeignException {
        if (response.body() == null) { return null; }
        InputStream inputStream = response.body().asInputStream();
        return JSON.parseObject(inputStream, type);

    }
}
