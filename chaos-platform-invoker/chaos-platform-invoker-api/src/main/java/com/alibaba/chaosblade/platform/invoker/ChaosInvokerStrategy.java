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

package com.alibaba.chaosblade.platform.invoker;

import cn.hutool.core.util.StrUtil;
import com.alibaba.chaosblade.platform.cmmon.enums.ChaosTools;
import com.alibaba.chaosblade.platform.cmmon.enums.DeviceType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author yefei
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ChaosInvokerStrategy {

    /**
     * @return
     */
    ChaosTools value() default ChaosTools.CHAOS_BLADE;

    /**
     * @return
     */
    String[] phase() default StrUtil.EMPTY;

    /**
     * @return
     */
    DeviceType[] deviceType();

    /**
     *
     * @return
     */
    String sceneCode() default StrUtil.EMPTY;
}
