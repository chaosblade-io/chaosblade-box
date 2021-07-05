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

package com.alibaba.chaosblade.box.service.model.tools;

import com.alibaba.chaosblade.box.dao.page.PageQuery;
import lombok.Builder;
import lombok.Data;

/**
 * @author yefei
 */
@Data
@Builder
public class ToolsRequest extends PageQuery {

    private String channel;

    private Long machineId;

    private String name;

    private String version;

    private String url;

    private String helmValues;

    private String namespace;

    private Byte deviceType;

    private Byte status;

    private String hostname;

    private String clusterName;
}
