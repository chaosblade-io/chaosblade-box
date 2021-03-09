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

/**
 * @author yefei
 */
public enum DeviceStatus {

    WAIT_INSTALL(0),
    INSTALLING(1),
    INSTALL_FAIL(-1),

    ONLINE(2),
    OFFLINE(3),
    UNINSTALLING(4),
    UNINSTALL_FAIL(5),
    FORBIDDEN(9),
    ;

    private int status;

    DeviceStatus(int status) {
        this.status = status;
    }

    public byte getStatus() {
        return (byte) this.status;
    }
}
