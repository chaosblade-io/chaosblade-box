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

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * t_chaos_probes
 *
 * @author
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_chaos_probes")
public class ProbesDO extends BaseDO {

    private Byte installMode;

    private String version;

    private Boolean success;

    private String ip;

    private Long deviceId;

    private String hostname;

    private String clusterId;

    private String clusterName;

    private String nodeName;

    /**
     * 0-Host，1-Kubernetes
     */
    private Byte agentType;

    /**
     * 0-online，1-offline
     */
    private Byte status;

    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String errorMessage;

    private Date lastPingTime;

    private Date lastOnlineTime;

    private Boolean deployBlade;

}
