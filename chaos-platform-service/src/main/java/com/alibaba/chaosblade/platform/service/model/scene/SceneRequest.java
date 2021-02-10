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

package com.alibaba.chaosblade.platform.service.model.scene;

import com.alibaba.chaosblade.platform.cmmon.jackson.JsonToStringDeserializer;
import com.alibaba.chaosblade.platform.dao.page.PageQuery;
import com.alibaba.chaosblade.platform.service.model.device.KubernetesDevice;
import com.alibaba.chaosblade.platform.service.model.scene.param.SceneParamRequest;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
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
@AllArgsConstructor
@NoArgsConstructor
public class SceneRequest extends PageQuery {

    private Long scenarioId;

    private String categoryId;

    @JsonDeserialize(using = JsonToStringDeserializer.class)
    private String categoryIds;

    private String name;

    private String code;

    private String description;

    @JsonDeserialize(using = JsonToStringDeserializer.class)
    private String supportScopeTypes;

    private String original;

    private String version;

    private Byte status;

    private String scopeType;

    private List<SceneParamRequest> parameters;

    private String dimension;

    private List<KubernetesDevice> machines;

}
