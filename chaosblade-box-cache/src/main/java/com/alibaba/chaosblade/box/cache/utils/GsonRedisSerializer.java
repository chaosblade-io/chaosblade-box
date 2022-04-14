package com.alibaba.chaosblade.box.cache.utils;

import com.google.gson.Gson;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.nio.charset.StandardCharsets;

/**
 * Author: sunju
 *
 * Date:   2019/11/12
 */
public class GsonRedisSerializer<T> implements RedisSerializer<T> {

    private final Gson gson = new Gson();

    @Override
    public byte[] serialize(T object) throws SerializationException {
        if (null == object) {
            return new byte[0];
        }
        return gson.toJson(object).getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
        if (null == bytes || bytes.length == 0) {
            return null;
        }
        return gson.fromJson(new String(bytes, StandardCharsets.UTF_8), new ParameterizedTypeReference<T>() {}.getType());
    }

}
