package com.alibaba.chaosblade.box.common.infrastructure.metric;

import com.alibaba.chaosblade.box.common.app.sdk.scope.Host;
import lombok.Data;

/**
 * 用户自定义的metric请求
 *
 * @author haibin
 *
 *
 */
@Data
public class UserDefinitionMetricRequest extends MetricRequest {

    /**
     * 请求url
     */
    private String url;

    /**
     * 请求参数
     */
    private String jsonBody;

    private Host host;

}
