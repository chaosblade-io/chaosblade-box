package com.alibaba.chaosblade.box.common.experiment.task.flow;

import com.alibaba.chaosblade.box.common.app.sdk.ChaosAppResponse;
import com.alibaba.chaosblade.box.common.common.domain.response.Response;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * @author haibin
 *
 *
 */
@Data
public class ChaosBladeAppResponse extends ChaosAppResponse {

    //只有是数字和字母组合才算有效的ExpId
    public void setChaosBladeExpId(String chaosBladeExpId) {
        if (StringUtils.isAlphanumeric(chaosBladeExpId)) {
            this.chaosBladeExpId = chaosBladeExpId;
        }
    }

    /**
     * chaosBlade调用ID，warning: 只有调用chaosBlade才需要，自定义小程序不需要的
     */
    private String chaosBladeExpId;

    /**
     * chaosBlade执行结果，warning: 只有调用chaosBlade才需要，自定义小程序不需要的
     */
    private Response chaosBladeResponse;

    /**
     * status
     */
    private Integer status;

}
