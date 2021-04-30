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

package com.alibaba.chaosblade.box.service.probes;

import com.alibaba.chaosblade.box.service.probes.model.InstallProbesRequest;
import com.alibaba.chaosblade.box.service.probes.model.ProbesRequest;
import com.alibaba.chaosblade.box.service.probes.model.ProbesResponse;

import java.util.List;

/**
 * @author yefei
 */
public interface ProbesService {

    /**
     *
     */
    List<ProbesResponse> getAnsibleHosts();

    /**
     * @return
     */
    List<ProbesResponse> getAnsibleHostsRegister(ProbesRequest probesRequest);

    /**
     *
     * @return
     */
    List<ProbesResponse> getProbesPageable(ProbesRequest probesRequest);

    /**
     *
     * @param probesRequest
     * @return
     */
    ProbesResponse banProbe(ProbesRequest probesRequest);

    /**
     *
     * @param probesRequest
     * @return
     */
    ProbesResponse unbanProbe(ProbesRequest probesRequest);

    /**
     *
     * @param installProbesRequest
     */
    List<ProbesResponse> installProbes(InstallProbesRequest installProbesRequest);

    /**
     *
     * @param probesRequest
     * @return
     */
    List<ProbesResponse> queryProbesInstallation(ProbesRequest probesRequest);

    /**
     *
     * @param installProbesRequest
     * @return
     */
    void uninstallProbe(InstallProbesRequest installProbesRequest);

    /**
     *
     * @param probesRequest
     * @return
     */
    void installProbe(ProbesRequest probesRequest);

    /**
     *
     * @param probesRequest
     */
    void deleteProbe(ProbesRequest probesRequest);

}

