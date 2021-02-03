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

package com.alibaba.chaosblade.platform.service.model.experiment;

import com.alibaba.chaosblade.platform.dao.page.PageQuery;
import com.alibaba.chaosblade.platform.service.model.experiment.jackson.ExperimentStatusDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yefei
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExperimentRequest extends PageQuery {

    private Long experimentId;

    private String experimentName;

    private String description;

    private String namespace;

    private String runMode;

    @JsonDeserialize(using = ExperimentStatusDeserializer.class)
    private Integer lastTaskResult;

    @JsonDeserialize(using = ExperimentStatusDeserializer.class)
    private Integer lastTaskStatus;

}
