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

package com.alibaba.chaosblade.box.web.controller;

import com.alibaba.chaosblade.box.service.collect.CollectorTimer;
import com.alibaba.chaosblade.box.service.probes.ProbesService;
import com.alibaba.chaosblade.box.service.probes.model.InstallProbesRequest;
import com.alibaba.chaosblade.box.service.probes.model.ProbesRequest;
import com.alibaba.chaosblade.box.service.probes.model.ProbesResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
public class ProbesController {

    @Autowired
    private ProbesService probesService;

    @Value("${chaos.collector.enable}")
    private boolean enableCollect;

    @Autowired
    private CollectorTimer collectorTimer;

    @PostMapping("/GetAnsibleHosts")
    public List<ProbesResponse> getMachinesForHost() {
        return probesService.getAnsibleHosts();
    }

    @PostMapping("/GetAnsibleHostsRegister")
    public List<ProbesResponse> getAnsibleHostsRegister(@RequestBody ProbesRequest probesRequest) {
        return probesService.getAnsibleHostsRegister(probesRequest);
    }

    @PostMapping("/GetProbesPageable")
    public List<ProbesResponse> getProbesPageable(@RequestBody ProbesRequest probesRequest) {
        return probesService.getProbesPageable(probesRequest);
    }

    @PostMapping("/InstallProbesByAnsible")
    public List<ProbesResponse> installProbesByAnsible(@RequestBody InstallProbesRequest installProbesRequest) {

        return probesService.installProbes(installProbesRequest);
    }

    @PostMapping("/InstallProbe")
    public void installProbe(@RequestBody ProbesRequest probesRequest) {
        probesService.installProbe(probesRequest);
    }

    @PostMapping("/UninstallProbe")
    public void uninstallProbe(@RequestBody InstallProbesRequest installProbesRequest) {
        probesService.uninstallProbe(installProbesRequest);
    }

    @PostMapping("/QueryProbesInstallation")
    public List<ProbesResponse> QueryProbesInstallation(@RequestBody ProbesRequest probesRequest) {
        return probesService.queryProbesInstallation(probesRequest);
    }

    @PostMapping("/BanProbe")
    public ProbesResponse banProbe(@RequestBody ProbesRequest probesRequest) {
        return probesService.banProbe(probesRequest);
    }

    @PostMapping("/UnbanProbe")
    public ProbesResponse unbanProbe(@RequestBody ProbesRequest probesRequest) {
        return probesService.unbanProbe(probesRequest);
    }

    @PostMapping("/DeleteProbe")
    public void deleteProbe(@RequestBody ProbesRequest probesRequest) {
        probesService.deleteProbe(probesRequest);
    }

    @PostMapping("/QueryCollectStatus")
    public boolean queryCollectStatus() throws Exception {
        collectorTimer.dryRun();
        return enableCollect;
    }

}
