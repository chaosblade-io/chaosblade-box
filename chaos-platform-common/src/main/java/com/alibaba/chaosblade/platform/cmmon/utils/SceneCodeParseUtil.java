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
import com.alibaba.chaosblade.platform.cmmon.constants.ChaosConstant;

/**
 * @author yefei
 */
public class SceneCodeParseUtil {

    private final static String DEFAULT_ORIGINAL = "chaosblade";

    public static String getOriginal(String sceneCode) {
        String s = StrUtil.subBefore(sceneCode, ".", false);
        if (s.equals(ChaosConstant.CHAOS)) {
            return DEFAULT_ORIGINAL;
        } else {
            return s;
        }
    }

    public static String removeOriginal(String sceneCode) {
        String s = StrUtil.subBefore(sceneCode, ".", false);
        if (s.equals(ChaosConstant.CHAOS)) {
            return sceneCode;
        } else {
            return sceneCode.substring(sceneCode.indexOf(".") + 1);
        }
    }

    public static Boolean isPrepare(String sceneCode) {
        return removeOriginal(sceneCode).startsWith("chaos.prepare");
    }

    public static String getPrepareType(String sceneCode) {
        String[] split = removeOriginal(sceneCode).split("[.]");
        return split[2];
    }

    public static Boolean isRevoke(String sceneCode) {
        return removeOriginal(sceneCode).startsWith("chaos.revoke");
    }

    public static String getTarget(String sceneCode) {
        String[] split = removeOriginal(sceneCode).split("[.]");
        return split[1];
    }

    public static String getAction(String sceneCode) {
        String[] split = removeOriginal(sceneCode).split("[.]");
        return split[2];
    }
}
