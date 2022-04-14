package com.alibaba.chaosblade.box.common.infrastructure.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.util.IOUtils;
import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;

/**
 * @author sunju
 *
 */
@UtilityClass
public class ObjectUtil {

    public static <T> T deepClone(T object) {
        try {
            return JSON.parseObject(JSON.toJSONString(object), new TypeReference<T>() {});
        } catch (Exception e) {
            return object;
        }
    }

    public static <T> T parseResourceObject(String resourcePath, Type type, Feature... features) throws IOException {
        InputStream messageInputstream = Thread.currentThread().getContextClassLoader().getResourceAsStream(
            resourcePath);
        return JSON.parseObject(messageInputstream, IOUtils.UTF8, type, features);
    }

}
