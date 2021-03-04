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

package com.alibaba.chaosblade.platform.web.controller;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.chaosblade.platform.cmmon.exception.ExceptionMessageEnum;
import com.alibaba.chaosblade.platform.cmmon.utils.Preconditions;
import com.alibaba.chaosblade.platform.service.SceneService;
import com.alibaba.chaosblade.platform.service.model.scene.SceneRequest;
import com.alibaba.chaosblade.platform.service.model.scene.SceneResponse;
import com.alibaba.chaosblade.platform.service.model.scene.SceneImportRequest;
import com.alibaba.chaosblade.platform.service.model.scene.SceneImportResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author yefei
 */
@RestController
@RequestMapping("/api")
public class SceneController {

    @Autowired
    private SceneService sceneService;

    @PostMapping("/ImportScenarios")
    public SceneImportResponse importScenarios(@RequestBody SceneImportRequest sceneImportRequest) {
        Preconditions.checkNotNull(sceneImportRequest, ExceptionMessageEnum.SCENE_IS_NULL);
        Preconditions.checkArgument(CollUtil.isEmpty(sceneImportRequest.getScenarios()), ExceptionMessageEnum.SCENE_IS_NULL);
        return sceneService.importScenarios(sceneImportRequest);
    }

    @PostMapping("/GetScenarioById")
    public SceneResponse getScenarioById(@RequestBody SceneRequest sceneRequest) {
        return sceneService.getScenarioById(sceneRequest);
    }

    @PostMapping("/GetScenariosPageable")
    public List<SceneResponse> getScenariosPageable(@RequestBody SceneRequest sceneRequest) {
        return sceneService.getScenariosPageable(sceneRequest);
    }

    @PostMapping("/BanScenario")
    public SceneResponse banScenario(@RequestBody SceneRequest sceneRequest) {
        return sceneService.banScenario(sceneRequest);
    }

    @PostMapping("/UnbanScenario")
    public SceneResponse unbanScenario(@RequestBody SceneRequest sceneRequest) {
        return sceneService.unbanScenario(sceneRequest);
    }

    @PostMapping("/UpdateScenario")
    public SceneResponse scenesUpdate(@RequestBody SceneRequest sceneRequest) {
        return sceneService.scenesUpdate(sceneRequest);
    }
}
