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
public enum RunStatus {

    /**
     * 准备运行
     */
    READY((byte) 0),

    /**
     * 运行中
     */
    RUNNING((byte) 1),

    /**
     * 暂停
     */
    SUSPEND((byte) 2),

    /**
     * 停止当中
     */
    STOPPING((byte) 3),

    /**
     * 已经结束
     */
    FINISHED((byte) 4);

    private Byte value;

    RunStatus(Byte value) {
        this.value = value;

    }

    public byte getValue() {
        return value;
    }

    public static RunStatus parse(byte value) {
        return Arrays.stream(RunStatus.values())
                .filter(o -> o.value == value)
                .findFirst()
                .orElse(null);
    }
}
