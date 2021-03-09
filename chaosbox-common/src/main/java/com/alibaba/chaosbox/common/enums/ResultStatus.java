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

package com.alibaba.chaosbox.common.enums;

import java.util.Arrays;

/**
 * @author yefei
 */
public enum ResultStatus {

    /**
     * 运行成功
     */
    SUCCESS((byte) 0),

    /**
     * 运行失败
     */
    FAILED((byte) 1),

    /**
     * 任务跳过
     */
    REJECTED((byte) 2),

    /**
     * 任务异常中断
     */
    ERROR((byte) 3),

    /**
     * 任务被终止
     */
    STOPPED((byte) 4),

    /**
     * 停止失败
     */
    SOPPED_FAILED((byte) 5);

    byte value;

    ResultStatus(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return this.value;
    }

    public static ResultStatus of(byte value) {
        return Arrays.stream(ResultStatus.values())
                .filter(o -> o.value == value)
                .findFirst()
                .orElse(null);
    }
}
