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

package com.alibaba.chaosblade.platform.blade.kubeapi.crd;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.kubernetes.client.common.KubernetesObject;
import io.kubernetes.client.openapi.models.V1ObjectMeta;
import lombok.Builder;
import lombok.Data;

/**
 * @author yefei
 */
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChaosBlade implements KubernetesObject {

    private String apiVersion;

    private String kind;

    private ChaosBladeSpec spec;

    private ChaosBladeStatus status;

    private V1ObjectMeta metadata;

    @Override
    public V1ObjectMeta getMetadata() {
        return metadata;
    }

    @Override
    public String getApiVersion() {
        return apiVersion;
    }

    @Override
    public String getKind() {
        return kind;
    }
}
