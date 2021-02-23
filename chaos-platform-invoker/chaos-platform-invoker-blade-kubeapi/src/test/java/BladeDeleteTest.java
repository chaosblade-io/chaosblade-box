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

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.chaosblade.platform.blade.kubeapi.Constants;
import com.alibaba.chaosblade.platform.blade.kubeapi.crd.ChaosBlade;
import com.alibaba.chaosblade.platform.cmmon.utils.JsonUtils;
import io.kubernetes.client.openapi.ApiCallback;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CustomObjectsApi;
import io.kubernetes.client.util.Config;
import org.junit.Test;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @author yefei
 */
public class BladeDeleteTest {

    @Test
    public void test() throws Exception {
        ApiClient client = Config.defaultClient();
        Configuration.setDefaultApiClient(client);

        CustomObjectsApi apiInstance = new CustomObjectsApi();


        CompletableFuture<Object> completableFuture = new CompletableFuture<>();
        apiInstance.deleteClusterCustomObjectAsync(
                Constants.GROUP,
                Constants.VERSION,
                Constants.PLURAL,
                "b585e989506e48738318ffaf177d998f",
                10,
                false,
                null,
                null,
                null,
                new ApiCallback() {
                    @Override
                    public void onFailure(ApiException e, int statusCode, Map responseHeaders) {
                        completableFuture.completeExceptionally(e);
                    }

                    @Override
                    public void onSuccess(Object result, int statusCode, Map responseHeaders) {
                        completableFuture.complete(result);
                    }

                    @Override
                    public void onUploadProgress(long bytesWritten, long contentLength, boolean done) {

                    }

                    @Override
                    public void onDownloadProgress(long bytesRead, long contentLength, boolean done) {

                    }
                }
        );

        completableFuture.handle((r, e) -> {
            if (e != null) {
                e.printStackTrace();
            }
            ChaosBlade chaosBlade = BeanUtil.toBean(r, ChaosBlade.class);
            String listCluster = JsonUtils.writeValueAsString(r);
            System.out.println(listCluster);
            return null;
        });
        Thread.currentThread().join();
    }
}
