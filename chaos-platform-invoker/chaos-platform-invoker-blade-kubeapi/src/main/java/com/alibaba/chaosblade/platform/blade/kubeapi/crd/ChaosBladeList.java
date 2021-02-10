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

import io.kubernetes.client.common.KubernetesListObject;
import io.kubernetes.client.common.KubernetesObject;
import io.kubernetes.client.openapi.models.V1ListMeta;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author yefei
 */
@Data
@Builder
public class ChaosBladeList implements KubernetesListObject {

    private List<ChaosBlade> items;

    private V1ListMeta v1ListMeta;

    @Override
    public V1ListMeta getMetadata() {
        return v1ListMeta;
    }

    @Override
    public List<? extends KubernetesObject> getItems() {
        return items;
    }

    @Override
    public String getApiVersion() {
        return "chaosblade.io/v1alpha1";
    }

    @Override
    public String getKind() {
        return "ChaosBlade";
    }
}
