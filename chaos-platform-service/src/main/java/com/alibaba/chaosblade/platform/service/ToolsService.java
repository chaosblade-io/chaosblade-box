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

package com.alibaba.chaosblade.platform.service;

import com.alibaba.chaosblade.platform.service.model.device.DeviceResponse;
import com.alibaba.chaosblade.platform.cmmon.model.chaos.PluginSpecBean;
import com.alibaba.chaosblade.platform.scenario.api.model.ToolsOverview;
import com.alibaba.chaosblade.platform.service.model.tools.ToolsRequest;
import com.alibaba.chaosblade.platform.service.model.tools.ToolsStatisticsResponse;
import com.alibaba.chaosblade.platform.scenario.api.model.ToolsVersion;

/**
 * @author yefei
 */
public interface ToolsService {

    /**
     *
     * @param toolsRequest
     * @return
     */
    ToolsStatisticsResponse getChaostoolsDeployedStatistics(ToolsRequest toolsRequest);

    /**
     *
     * @param toolsRequest
     * @return
     */
    DeviceResponse deployChaostoolsToHost(ToolsRequest toolsRequest);

    /**
     *
     * @param toolsRequest
     * @return
     */
    DeviceResponse undeployChaostoolsForHost(ToolsRequest toolsRequest);

    /**
     *
     * @param toolsRequest
     * @return
     */
    DeviceResponse upgradeChaostoolsToHost(ToolsRequest toolsRequest);

    /**
     *
     * @return
     */
    ToolsOverview toolsOverview(String toolsName);

    /**
     *
     * @return
     */
    ToolsVersion toolsVersion(String toolsName, String version);

    /**
     *
     * @param toolsName
     * @param version
     * @param sceneName
     * @return
     */
    PluginSpecBean toolsScene(String toolsName, String version, String sceneName);
}
