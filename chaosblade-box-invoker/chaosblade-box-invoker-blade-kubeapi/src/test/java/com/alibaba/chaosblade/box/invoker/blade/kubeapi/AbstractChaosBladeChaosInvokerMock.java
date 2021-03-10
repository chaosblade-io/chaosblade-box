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

package com.alibaba.chaosblade.box.invoker.blade.kubeapi;

import com.alibaba.chaosblade.box.invoker.blade.kubeapi.crd.ExperimentStatus;
import com.alibaba.testable.core.annotation.MockMethod;
import io.kubernetes.client.openapi.ApiCallback;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.CustomObjectsApi;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yefei
 */
@Slf4j
public class AbstractChaosBladeChaosInvokerMock {

    @MockMethod
    private okhttp3.Call getClusterCustomObjectAsync(
            CustomObjectsApi self,
            String group, String version, String plural, String name, final ApiCallback<Object> _callback)
            throws ApiException {
        log.info("mock check blade status");

        Map<String, Object> result = new HashMap<>();
        Map<String, Object> status = new HashMap<>();

        result.put("status", status);
        List<ExperimentStatus> value = new ArrayList<>();
        value.add(ExperimentStatus.builder()
                .success(true)
                .build());

        status.put("expStatuses", value);
        status.put("phase", "Running");

        _callback.onSuccess(result, 404, new HashMap<>());
        return null;
    }
}
