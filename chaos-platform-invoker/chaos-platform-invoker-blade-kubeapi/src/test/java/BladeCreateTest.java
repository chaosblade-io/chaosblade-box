import cn.hutool.core.util.IdUtil;
import com.alibaba.chaosblade.platform.blade.kubeapi.crd.ChaosBlade;
import com.alibaba.chaosblade.platform.blade.kubeapi.crd.ChaosBladeSpec;
import com.alibaba.chaosblade.platform.blade.kubeapi.crd.ExperimentSpec;
import com.alibaba.chaosblade.platform.blade.kubeapi.crd.FlagSpec;
import com.alibaba.chaosblade.platform.cmmon.utils.JsonUtils;
import io.kubernetes.client.openapi.ApiCallback;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CustomObjectsApi;
import io.kubernetes.client.openapi.models.V1ObjectMeta;
import io.kubernetes.client.util.Config;
import org.junit.Test;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @author yefei
 */
public class BladeCreateTest {

    @Test
    public void test() throws Exception {
        ApiClient client = Config.defaultClient();
        Configuration.setDefaultApiClient(client);

        CustomObjectsApi apiInstance = new CustomObjectsApi(client);

        V1ObjectMeta v1ObjectMeta = new V1ObjectMeta();
        v1ObjectMeta.setName(IdUtil.fastSimpleUUID());
        ChaosBlade chaosBlade = ChaosBlade.builder()
                .apiVersion("chaosblade.io/v1alpha1")
                .kind("ChaosBlade")
                .metadata(v1ObjectMeta)
                .spec(ChaosBladeSpec.builder()
                        .experiments(new ExperimentSpec[]{
                                ExperimentSpec.builder()
                                        .scope("pod")
                                        .target("cpu")
                                        .action("load")
                                        .matchers(new FlagSpec[]{
                                                FlagSpec.builder()
                                                        .name("cpu-percent")
                                                        .value(new String[]{"80"})
                                                        .build(),

                                                FlagSpec.builder()
                                                        .name("names")
                                                        .value(new String[]{"tomcat-754d84b64-726g9"})
                                                        .build(),

                                                FlagSpec.builder()
                                                        .name("debug")
                                                        .value(new String[]{"true"})
                                                        .build(),

                                                FlagSpec.builder()
                                                        .name("timeout")
                                                        .value(new String[]{"20"})
                                                        .build()
                                        })
                                        .build()
                        })
                        .build()).build();

        String group = "chaosblade.io";
        String version = "v1alpha1";
        String plural = "chaosblades";
        String pretty = "ture";
        CompletableFuture<Object> completableFuture = new CompletableFuture<>();
        apiInstance.createClusterCustomObjectAsync(
                group,
                version,
                plural,
                JsonUtils.writeValueAsBytes(chaosBlade),
                pretty,
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
            String listCluster = JsonUtils.writeValueAsString(r);
            System.out.println(listCluster);
            return null;
        });
        Thread.currentThread().join();
    }
}
