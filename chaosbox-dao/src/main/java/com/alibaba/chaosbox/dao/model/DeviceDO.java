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

package com.alibaba.chaosbox.dao.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * t_chaos_device
 * @author yefei
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_chaos_device")
public class DeviceDO extends BaseDO {

    private String ip;

    private String hostname;

    private String version;

    private Integer cpuCore;

    private Integer memorySize;

    /**
     * 0-offline / 1-online
     */
    private Byte status;

    private Date connectTime;

    /**
     *  host、k8s_helm
     */
    private String installMode;

    private String uptime;

    /**
     *  0-host,1-node, 2-pod
     */
    private Byte type;

    private Date lastPingTime;

    private Date lastOnlineTime;

    private Boolean isExperimented;

    private Date lastExperimentTime;

    private Long lastTaskId;

    private Byte lastTaskStatus;

}
