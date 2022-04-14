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

package com.alibaba.chaosblade.box.common.common.util;

import cn.hutool.core.util.StrUtil;

/**
 * @author yefei
 */
public class SystemPropertiesUtils {

    public static String getPropertiesFileEncoding() {
        return System.getProperty("file.encoding");
    }

    public static String getPropertiesValue(String key) {
        if (StrUtil.isBlank(key)) {
            throw new RuntimeException("key not be null!");
        }
        return System.getProperty(key);
    }

    public static String getEnvValue(String key) {
        if (StrUtil.isBlank(key)) {
            throw new RuntimeException("key not be null!");
        }
        return System.getenv(key);
    }

    /**
     *
     * @param key
     * @return
     */
    public static String getEnvOrPropertiesValue(String key) {
        if (StrUtil.isBlank(key)) {
            throw new RuntimeException("key not be null!");
        }
        String envValue = getEnvValue(key);
        if (StrUtil.isBlank(envValue)) {
            return getPropertiesValue(key);
        }
        return envValue;
    }

}
