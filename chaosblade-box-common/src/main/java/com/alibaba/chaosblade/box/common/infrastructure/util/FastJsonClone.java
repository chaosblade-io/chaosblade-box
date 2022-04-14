package com.alibaba.chaosblade.box.common.infrastructure.util;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;

/**
 * @author haibin
 *
 *
 */
public class FastJsonClone {

    public static <T extends Serializable> T clone(T source, Class<T> tClass) {
        return JSON.parseObject(JSON.toJSONString(source), tClass);
    }
}
