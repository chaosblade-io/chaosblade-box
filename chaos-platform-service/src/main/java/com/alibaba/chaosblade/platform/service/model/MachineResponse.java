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

package com.alibaba.chaosblade.platform.service.model;

import com.alibaba.chaosblade.platform.service.model.tools.ToolsResponse;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * @author yefei
 */
@Data
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MachineResponse {

    @JsonProperty("machineId")
    private Long deviceId;

    @JsonProperty("machineType")
    private String deviceType;

    private String hostname;

    private String ip;

    private String nodeName;

    private String namespace;

    private String podName;

    private String containerName;

    private Date createTime;

    private Date heartbeatTime;

    private byte status;

    private boolean chaosed;

    private Date chaosTime;

    private String original;

    private boolean chaosRunning;

    private Long taskId;

    private Byte taskStatus;

    private List<ToolsResponse> chaostools;

}
