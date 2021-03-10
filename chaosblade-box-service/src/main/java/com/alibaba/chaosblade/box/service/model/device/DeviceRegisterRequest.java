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

package com.alibaba.chaosblade.box.service.model.device;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author yefei
 */
@Data
@Builder
public class DeviceRegisterRequest {

    private String agentId;

    private String ip;

    @JsonProperty("deviceId")
    private String hostName;

    @JsonProperty("osVersion")
    private String version;

    @JsonProperty("cpuNum")
    private Integer cpuCore;

    @JsonProperty("memSize")
    private BigDecimal memorySize;

    @JsonProperty("agentMode")
    private String installMode;

    @JsonProperty("v")
    private String agentVersion;

    private String uptime;

    private Byte deviceType;

    private String namespace;

    private String appInstance;

    private String appGroup;

    private Boolean enableK8sCollector;

}
