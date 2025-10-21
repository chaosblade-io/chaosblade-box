/*
 * Copyright 2025 The ChaosBlade Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alibaba.chaosblade.box.cache.utils;

import com.google.gson.Gson;
import java.nio.charset.StandardCharsets;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

/**
 * Author: sunju
 *
 * <p>Date: 2019/11/12
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
    return gson.fromJson(
        new String(bytes, StandardCharsets.UTF_8),
        new ParameterizedTypeReference<T>() {}.getType());
  }
}
