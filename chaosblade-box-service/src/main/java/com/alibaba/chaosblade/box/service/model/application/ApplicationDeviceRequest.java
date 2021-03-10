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

package com.alibaba.chaosblade.box.service.model.application;

import lombok.Data;

/**
 * @author yefei
 */
@Data
public class ApplicationDeviceRequest  {

    /**
     * 命名空间
     */
    private String namespace;

    /**
     * 应用名
     */
    private String appName;

    /**
     * 进程Id
     */
    private Long pid;

    /**
     * 应用ID
     */
    private Long appId;

    /**
     * 分组名
     */
    private String groupName;

    /**
     * 集群
     */
    private String clusterId;

    /**
     * 设备类型，Host 是 0，Container 是 1
     */
    private Byte deviceType;

    /**
     * 设备全局唯一标识
     */
    private String configurationId;

    /**
     * 主机配置ID
     */
    private String hostConfigurationId;

    /**
     * 设备状态, 0 是可用，1 是不可用
     */
    private Byte isDeleted;

    /**
     * 最后一次健康检查时间
     */
    private Long lastHealthPingTime;

    /**
     * 连接时间
     */
    private Long connectTime;

    /**
     * 状态
     */
    private Byte status;

    /**
     * host_name
     */
    private String deviceName;

    private String privateIp;

    private String publicIp;

    /**
     * 资源划分维度
     */
    private Byte dimension;

}
