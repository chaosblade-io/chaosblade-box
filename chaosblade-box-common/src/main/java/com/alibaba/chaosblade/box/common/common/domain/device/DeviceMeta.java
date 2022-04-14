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

package com.alibaba.chaosblade.box.common.common.domain.device;

import cn.hutool.core.util.StrUtil;
import com.alibaba.chaosblade.box.common.common.enums.DeviceType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * @author yefei
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode
public class DeviceMeta {

    @JsonProperty("machineId")
    private Long deviceId;

    @JsonProperty("machineType")
    private Byte deviceType;

    private String hostname;

    private String ip;

    private Long clusterId;

    private String nodeName;

    private String namespace;

    private String podName;

    private String containerName;

    public String identity() {
        DeviceType deviceType = DeviceType.transByCode(this.deviceType);
        switch (deviceType) {
            case HOST:
                return hostname;
            case POD:
                if (StrUtil.isBlank(containerName)) {
                    return String.format("%s/%s/%s/%s", clusterId, nodeName, namespace, podName);
                } else {
                    return String.format("%s/%s/%s/%s/%s", clusterId, nodeName, namespace, podName, containerName);
                }
            default:
                return null;
        }
    }

}
