import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import com.alibaba.chaosblade.platform.blade.kubeapi.crd.ChaosBlade;
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
public class BladeStatusTest {

    @Test
    public void test() throws Exception {
        ApiClient client = Config.defaultClient();
        Configuration.setDefaultApiClient(client);

        CustomObjectsApi apiInstance = new CustomObjectsApi();

        String group = "chaosblade.io";
        String version = "v1alpha1";
        String plural = "chaosblades";
        CompletableFuture<Object> completableFuture = new CompletableFuture<>();
        apiInstance.getClusterCustomObjectAsync(
                group,
                version,
                plural,
                "9b4a4b877d224ae6958e4c07b11e2a13",
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
