package com.alibaba.chaosblade.box.common.infrastructure.util;

import com.alibaba.fastjson.JSON;
import feign.RequestTemplate;
import feign.codec.EncodeException;
import feign.codec.Encoder;

import java.lang.reflect.Type;

/**
 * @author haibin
 *
 *
 */
public class FastJsonEncoder implements Encoder {
    @Override
    public void encode(Object object, Type bodyType, RequestTemplate template) throws EncodeException {
        try {
            template.body(JSON.toJSONString(object));
        } catch (Exception e) {
            throw new EncodeException(e.getMessage(), e);
        }
    }
}
