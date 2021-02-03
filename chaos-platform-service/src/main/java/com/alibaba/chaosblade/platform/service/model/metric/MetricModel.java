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

package com.alibaba.chaosblade.platform.service.model.metric;

import com.alibaba.chaosblade.platform.cmmon.jackson.ArgumentsToMapDeserializer;
import com.alibaba.chaosblade.platform.cmmon.jackson.MapToArgumentsDeserializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.util.Map;

/**
 * @author yefei
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MetricModel {

    private String name;

    private Long categoryId;

    private Long parentId;

    private Integer level;

    private String code;

    private MetricComponent component;

    @JsonDeserialize(using = ArgumentsToMapDeserializer.class)
    @JsonSerialize(using = MapToArgumentsDeserializer.class)
    private Map<String, String> params;

}
