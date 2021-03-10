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

package com.alibaba.chaosblade.box.service.model.scene;

import com.alibaba.chaosblade.box.service.model.scene.param.SceneParamResponse;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author yefei
 */
@Data
@Builder
public class SceneResponse {

    private Long scenarioId;

    private String code;

    private String name;

    private String description;

    private Byte status;

    private String version;

    private String original;

    private List<String> supportScopeTypes;

    private List<SceneCategoryResponse> categories;

    private List<SceneParamResponse> parameters;

    private Date createTime;

    private Date modifyTime;

    private Integer count;

    private Boolean requiredJavaAgent;

    private Long preScenarioId;

}
