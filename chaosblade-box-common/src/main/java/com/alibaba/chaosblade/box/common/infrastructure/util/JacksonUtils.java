package com.alibaba.chaosblade.box.common.infrastructure.util;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;

/** @author xf.yefei */
public class JacksonUtils {

  private static final ObjectMapper MAPPER = new ObjectMapper();

  static {
    MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    MAPPER.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
  }

  public static ObjectReader reader(TypeReference<?> typeReference) {
    return MAPPER.readerFor(typeReference);
  }

  public static ObjectReader reader(Class<?> clazz) {
    return MAPPER.readerFor(clazz);
  }

  public static ObjectReader reader() {
    return MAPPER.reader();
  }

  public static ObjectWriter writer() {
    return MAPPER.writer();
  }

  public static <T> T readValue(Class<T> clazz, String content) {
    if (content == null) {
      return null;
    }
    try {
      return MAPPER.readerFor(clazz).readValue(content);
    } catch (Exception e) {
      AnyThrow.throwUnchecked(e);
    }
    return null;
  }

  public static <T> T readValue(Class<T> clazz, byte[] content) {
    if (content == null) {
      return null;
    }
    try {
      return MAPPER.readerFor(clazz).readValue(content);
    } catch (Exception e) {
      AnyThrow.throwUnchecked(e);
    }
    return null;
  }

  public static <T> T readValue(TypeReference<T> typeReference, String content) {
    if (content == null) {
      return null;
    }
    try {
      return MAPPER.readerFor(typeReference).readValue(content);
    } catch (Exception e) {
      AnyThrow.throwUnchecked(e);
    }
    return null;
  }

  public static <T> T readValue(TypeReference<T> typeReference, byte[] content) {
    if (content == null) {
      return null;
    }
    try {
      return MAPPER.readerFor(typeReference).readValue(content);
    } catch (Exception e) {
      AnyThrow.throwUnchecked(e);
    }
    return null;
  }

  public static JsonNode readTree(String content) {
    if (content == null) {
      return null;
    }
    try {
      return MAPPER.reader().readTree(content);
    } catch (Exception e) {
      AnyThrow.throwUnchecked(e);
    }
    return null;
  }

  public static String writeValueAsString(Object o) {
    try {
      return MAPPER.writer().writeValueAsString(o);
    } catch (JsonProcessingException e) {
      AnyThrow.throwUnchecked(e);
    }
    return StrUtil.EMPTY;
  }

  public static byte[] writeValueAsBytes(Object o) {
    try {
      return MAPPER.writer().writeValueAsBytes(o);
    } catch (JsonProcessingException e) {
      AnyThrow.throwUnchecked(e);
    }
    return new byte[] {};
  }
}
