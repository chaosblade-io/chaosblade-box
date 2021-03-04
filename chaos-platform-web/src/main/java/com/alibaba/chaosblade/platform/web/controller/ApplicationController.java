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

import com.alibaba.chaosblade.platform.service.ApplicationService;
import com.alibaba.chaosblade.platform.service.model.application.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author yefei
 */
@RestController
@RequestMapping("/api")
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;

    @PostMapping("/application/select")
    public List<ApplicationResponse> selectApplication(@RequestBody ApplicationRequest applicationRequest) {
       return applicationService.selectApplication(applicationRequest);
    }

    @PostMapping("/application/group/select")
    public List<ApplicationResponse> selectApplicationGroup(@RequestBody ApplicationRequest applicationRequest) {
        return applicationService.selectApplicationGroup(applicationRequest);
    }

    @PostMapping("/application/device/select")
    public List<ApplicationDeviceResponse> selectApplicationDevice(@RequestBody ApplicationRequest applicationRequest) {
        return applicationService.selectApplicationDevice(applicationRequest);
    }

    @PostMapping("/application/insert")
    public void insertApplication(@RequestBody ApplicationDeviceRequest applicationDeviceRequest) {
        applicationService.insertApplicationDevice(applicationDeviceRequest);
    }

    @GetMapping("/GetApplicationTotalStatistics")
    public ApplicationStatisticsResponse getApplicationTotalStatistics(boolean active) {
        return applicationService.getApplicationTotalStatistics(active);
    }

    @PostMapping("/GetMachinesForApplicationPageable")
    public List<ApplicationResponse> getMachinesForApplication(@RequestBody ApplicationRequest applicationRequest) {
        return applicationService.getMachinesForApplication(applicationRequest);
    }

}
