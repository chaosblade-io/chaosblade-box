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

package com.alibaba.chaosblade.platform.service.model.flow;

import com.alibaba.chaosblade.platform.cmmon.jackson.ArgumentsToMapDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

import java.util.Map;

@Data
public class FlowActivity {

    private String activityName;

    private String sceneCode;

    private boolean manualChecked;

    private Long waitOfBefore;

    private Long waitOfAfter;

    @JsonDeserialize(using = ArgumentsToMapDeserializer.class)
    private Map<String, String> arguments;

}
