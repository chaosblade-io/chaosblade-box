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

import com.alibaba.chaosbox.service.DeviceService;
import com.alibaba.chaosbox.service.model.device.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author yefei
 */
@RestController
@RequestMapping("/api")
public class MachineController {

    @Autowired
    private DeviceService deviceService;

    @PostMapping("/GetMachinesForHost")
    public List<DeviceResponse> getMachinesForHost(@RequestBody DeviceRequest deviceRequest) {
        return deviceService.getMachinesForHost(deviceRequest);
    }

    @GetMapping("/GetK8sResourceStatistics")
    public KubernetesStatisticsResponse getKubernetesTotalStatistics() {
        return deviceService.getKubernetesTotalStatistics();
    }

    @PostMapping("/GetMachinesForNodePageable")
    public List<DeviceNodeResponse> getMachinesForNode(
            @RequestBody DeviceNodeRequest deviceNodeRequest) {
        return deviceService.getMachinesForNode(deviceNodeRequest);
    }

    @PostMapping("/GetMachinesForPodPageable")
    public List<DevicePodResponse> getMachinesForPod(
            @RequestBody DevicePodRequest devicePodRequest) {

        return deviceService.getMachinesForPod(devicePodRequest);
    }

    @PostMapping("/BanMachine")
    public DeviceResponse banMachine(@RequestBody DeviceRequest deviceRequest) {
        return deviceService.banMachine(deviceRequest);
    }

    @PostMapping("/UnbanMachine")
    public DeviceResponse UnbanMachine(@RequestBody DeviceRequest deviceRequest) {
        return deviceService.unbanMachine(deviceRequest);
    }

    @RequestMapping("/GetHostTotalStatistics")
    public HostStatisticsResponse getHostTotalStatistics() {
        return deviceService.getHostTotalStatistics();
    }
}
