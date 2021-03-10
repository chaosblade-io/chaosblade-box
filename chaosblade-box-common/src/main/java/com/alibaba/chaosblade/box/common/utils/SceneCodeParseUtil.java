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

package com.alibaba.chaosblade.box.common.utils;

import cn.hutool.core.util.StrUtil;
import com.alibaba.chaosblade.box.common.constants.ChaosConstant;

/**
 * @author yefei
 */
public class SceneCodeParseUtil {

    public static String getOriginal(String sceneCode) {
        String s = StrUtil.subBefore(sceneCode, ".", false);
        return s;
    }

    public static String getPrepareType(String sceneCode) {
        String[] split = sceneCode.split("[.]");
        return split[2];
    }

    public static Boolean isRecover(String sceneCode) {
        return sceneCode.endsWith(ChaosConstant.CHAOS_DESTROY_SUFFIX);
    }

    public static String getTarget(String sceneCode) {
        String[] split = sceneCode.split("[.]");
        return split[1];
    }

    public static String getAction(String sceneCode) {
        String[] split = sceneCode.split("[.]");
        return split[2];
    }
}
