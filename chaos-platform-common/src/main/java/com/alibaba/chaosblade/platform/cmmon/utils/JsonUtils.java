/*
 * Copyright 1999-2021 Alibaba Group Holding Ltd.
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

package com.alibaba.chaosblade.platform.cmmon.utils;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author yefei
 */
@Slf4j
public class JsonUtils {

    private final static ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public static ObjectReader reader(TypeReference<?> typeReference) {
        return mapper.readerFor(typeReference);
    }

    public static ObjectReader reader(Class<?> clazz) {
        return mapper.readerFor(clazz);
    }

    public static ObjectReader reader() {
        return mapper.reader();
    }

    public static ObjectWriter writer() {
        return mapper.writer();
    }

    public static <T> T readValue(Class<T> clazz, String content) {
        try {
            return mapper.readerFor(clazz).readValue(content);
        } catch (Exception e) {
            AnyThrow.throwUnchecked(e);
        }
        return null;
    }

    public static <T> T readValue(Class<T> clazz, byte[] content) {
        try {
            return mapper.readerFor(clazz).readValue(content);
        } catch (Exception e) {
            AnyThrow.throwUnchecked(e);
        }
        return null;
    }

    public static <T> T readValue(TypeReference<T> typeReference, String content) {
        try {
            return mapper.readerFor(typeReference).readValue(content);
        } catch (Exception e) {
            AnyThrow.throwUnchecked(e);
        }
        return null;
    }

    public static <T> T readValue(TypeReference<T> typeReference, byte[] content) {
        try {
            return mapper.readerFor(typeReference).readValue(content);
        } catch (Exception e) {
            AnyThrow.throwUnchecked(e);
        }
        return null;
    }

    public static String writeValueAsString(Object o) {
        try {
            return mapper.writer().writeValueAsString(o);
        } catch (JsonProcessingException e) {
            AnyThrow.throwUnchecked(e);
        }
        return StrUtil.EMPTY;
    }

    public static byte[] writeValueAsBytes(Object o) {
        try {
            return mapper.writer().writeValueAsBytes(o);
        } catch (JsonProcessingException e) {
            AnyThrow.throwUnchecked(e);
        }
        return new byte[]{};
    }
}
