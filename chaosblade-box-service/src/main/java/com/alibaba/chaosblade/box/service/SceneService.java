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

package com.alibaba.chaosblade.box.service;

import com.alibaba.chaosblade.box.service.model.scene.SceneRequest;
import com.alibaba.chaosblade.box.service.model.scene.SceneResponse;
import com.alibaba.chaosblade.box.service.model.scene.SceneImportRequest;
import com.alibaba.chaosblade.box.service.model.scene.SceneImportResponse;

import java.io.InputStream;
import java.util.List;

/**
 *
 * @author yefei
 */
public interface SceneService {

    /**
     *
     * @param sceneRequest
     * @return
     */
    List<SceneResponse> getScenariosPageable(SceneRequest sceneRequest);

    /**
     *
     * @param sceneRequest
     */
    SceneResponse scenesUpdate(SceneRequest sceneRequest);

    /**
     *
     * @param sceneImportRequest
     * @return
     */
    SceneImportResponse importScenarios(SceneImportRequest sceneImportRequest);

    /**
     *
     * @param sceneRequest
     * @return
     */
    SceneResponse getScenarioById(SceneRequest sceneRequest);

    /**
     *
     * @param sceneRequest
     * @return
     */
    SceneResponse banScenario(SceneRequest sceneRequest);

    /**
     *
     * @param sceneRequest
     * @return
     */
    SceneResponse unbanScenario(SceneRequest sceneRequest);

    /**
     *
     * @param sceneImportRequest
     * @return
     */
    SceneImportResponse uploadScenarios(SceneImportRequest sceneImportRequest);
}
