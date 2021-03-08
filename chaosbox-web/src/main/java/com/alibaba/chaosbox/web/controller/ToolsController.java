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

package com.alibaba.chaosbox.web.controller;

import com.alibaba.chaosbox.service.ToolsService;
import com.alibaba.chaosbox.service.model.device.DeviceResponse;
import com.alibaba.chaosbox.service.model.tools.ToolsRequest;
import com.alibaba.chaosbox.service.model.tools.ToolsStatisticsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yefei
 */
@RestController
@RequestMapping("/api")
public class ToolsController {

    @Autowired
    private ToolsService toolsService;

    @RequestMapping("/GetChaostoolsDeployedStatistics")
    public ToolsStatisticsResponse getChaostoolsDeployedStatistics(@RequestBody ToolsRequest toolsRequest) {
        return toolsService.getChaostoolsDeployedStatistics(toolsRequest);
    }

    @RequestMapping("/DeployChaostoolsToHost")
    public DeviceResponse deployChaostoolsToHost(@RequestBody ToolsRequest toolsRequest) {
        return toolsService.deployChaostoolsToHost(toolsRequest);
    }

    @RequestMapping("/UndeployChaostoolsForHost")
    public DeviceResponse undeployChaostoolsForHost(@RequestBody ToolsRequest toolsRequest) {
        return toolsService.undeployChaostoolsForHost(toolsRequest);
    }

    @RequestMapping("/UpgradeChaostoolsToHost")
    public DeviceResponse upgradeChaostoolsToHost(@RequestBody ToolsRequest toolsRequest) {
        return toolsService.upgradeChaostoolsToHost(toolsRequest);
    }
}
