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

package com.alibaba.chaosblade.box.service.probes.model;

import com.alibaba.chaosblade.box.dao.page.PageQuery;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author yefei
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProbesRequest extends PageQuery {

    private Long probeId;

    private List<String> hosts;

    private List<Long> probeIds;

    private String hostname;

    private String host;

    private String ip;

    private Byte status;

    private Byte agentType;

    private String applicationName;

    private String groupName;

    private String commandOptions;

    private String clusterName;

    private Byte installMode;

    private String username;

    private String password;

    private int port;
}
