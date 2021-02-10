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

package com.alibaba.chaosblade.platform.metric.controller;

import com.alibaba.chaosblade.platform.service.SceneParamService;
import com.alibaba.chaosblade.platform.service.model.scene.param.SceneParamRequest;
import com.alibaba.chaosblade.platform.service.model.scene.param.SceneParamResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author yefei
 */
@RestController
@RequestMapping("/api")
public class SceneParamController {

    @Autowired
    private SceneParamService sceneParamService;

    @RequestMapping("/sceneparam/select")
    public List<SceneParamResponse> selectSceneParam(@RequestBody SceneParamRequest sceneParamRequest) {
        return sceneParamService.selectSceneParam(sceneParamRequest);
    }

}
