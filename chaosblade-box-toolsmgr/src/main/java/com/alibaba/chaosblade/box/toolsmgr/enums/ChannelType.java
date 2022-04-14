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

package com.alibaba.chaosblade.box.toolsmgr.enums;


/**
 * @author yefei
 */
public enum ChannelType {

    SSH(ProbesInstallModel.SSH.getCode()),
    ANSIBLE(ProbesInstallModel.ANSIBLE.getCode()),
    PUPPET(-1),
    HELM((ProbesInstallModel.K8S_HELM.getCode()));

    private int code;

    public int getCode() {
        return code;
    }

    ChannelType(int code) {
        this.code = code;
    }

    public static ChannelType parseCode(int code) {
        for (ChannelType value : ChannelType.values()) {
            if (code == value.code) {
                return value;
            }
        }
        return null;
    }
}
