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

import com.alibaba.chaosblade.platform.cmmon.utils.JsonUtils;
import com.alibaba.chaosblade.platform.service.DeviceService;
import com.alibaba.chaosblade.platform.service.model.device.DeviceRegisterRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author yefei
 */
@Slf4j
@RestController
public class AgentReportController {

    @Autowired
    private DeviceService deviceService;

    @RequestMapping("/chaos/AgentRegister")
    @ResponseBody
    public void agentReport(@RequestBody DeviceRegisterRequest registerRequest) {
        deviceService.deviceRegister(registerRequest);
    }

    @RequestMapping("/chaos/AgentHeartBeat")
    @ResponseBody
    public void agentHeartBeat(@RequestBody Map<String, String> param) {
        log.debug(JsonUtils.writeValueAsString(param));
    }

    @RequestMapping("/chaos/AgentClosed")
    @ResponseBody
    public void agentClose(@RequestBody DeviceRegisterRequest registerRequest) {
       log.info("agent close, {}", JsonUtils.writeValueAsString(registerRequest));
    }

}
