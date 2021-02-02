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

package com.alibaba.chaosblade.platform.cmmon.enums;

import java.util.Arrays;

/**
 * 演练机器的状态
 *
 * @author yefei
 */
public enum ExperimentDeviceStatus {

    /**
     * 运行成功
     */
    READY((byte) 0),

    /**
     * 运行失败
     */
    RUNNING((byte) 1),

    /**
     * 任务异常中断
     */
    ERROR((byte) 2),

    /**
     * 任务被终止
     */
    SUCCESS((byte) 3);

    byte value;

    ExperimentDeviceStatus(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return this.value;
    }

    public static ExperimentDeviceStatus of(byte value) {
        return Arrays.stream(ExperimentDeviceStatus.values())
                .filter(o -> o.value == value)
                .findFirst()
                .orElse(null);
    }
}
