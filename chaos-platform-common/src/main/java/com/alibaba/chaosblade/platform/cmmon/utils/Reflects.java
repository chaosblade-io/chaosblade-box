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

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author yefei
 */
public class Reflects {

    /**
     * @param clazz
     * @return
     */
    public static Object getTypeDefaultValue(Class<?> clazz) {
        if (clazz.isPrimitive()) {
            if (clazz == byte.class) {
                return (byte) 0;
            }
            if (clazz == short.class) {
                return (short) 0;
            }
            if (clazz == int.class) {
                return 0;
            }
            if (clazz == long.class) {
                return 0L;
            }
            if (clazz == float.class) {
                return 0F;
            }
            if (clazz == double.class) {
                return 0D;
            }
            if (clazz == char.class) {
                return (char) 0;
            }
            if (clazz == boolean.class) {
                return false;
            }
        }
        return null;
    }


    /**
     * @param o     interface
     * @param index index
     */
    public static Class<?> getInterfaceGeneric(Object o, int interfaceIndex, int index) {
        Type[] types = o.getClass().getGenericInterfaces();
        ParameterizedType parameterizedType = (ParameterizedType) types[interfaceIndex];
        Type type = parameterizedType.getActualTypeArguments()[index];
        return checkType(type, index);
    }


    /**
     * @param o     object
     * @param index index
     */
    public static Class<?> getClassGeneric(Object o, int index) {
        Type type = o.getClass().getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type actType = parameterizedType.getActualTypeArguments()[index];
            return checkType(actType, index);
        } else {
            String className = type == null ? "null" : type.getClass().getName();
            throw new IllegalArgumentException("Expected a Class, ParameterizedType"
                    + ", but <" + type + "> is of type " + className);
        }
    }

    private static Class<?> checkType(Type type, int index) {
        if (type instanceof Class<?>) {
            return (Class<?>) type;
        } else if (type instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) type;
            return (Class<?>) pt.getRawType();
            //return checkType(t, index);
        } else {
            String className = type == null ? "null" : type.getClass().getName();
            throw new IllegalArgumentException("Expected a Class, ParameterizedType"
                    + ", but <" + type + "> is of type " + className);
        }
    }
}
