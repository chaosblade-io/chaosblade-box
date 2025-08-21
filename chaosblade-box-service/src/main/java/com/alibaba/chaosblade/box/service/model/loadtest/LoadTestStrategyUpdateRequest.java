package com.alibaba.chaosblade.box.service.model.loadtest;

import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 压测策略更新请求
 *
 * @author ZhengMingZhuo
 */
@Data
@ApiModel("压测策略更新请求")
public class LoadTestStrategyUpdateRequest {

    @ApiModelProperty(value = "策略ID", required = true)
    private String id;

    @ApiModelProperty("是否启用")
    private Boolean enable;

    @ApiModelProperty("压测定义ID")
    private String definitionId;

    @ApiModelProperty("实验ID")
    private String experimentId;

    @ApiModelProperty("故障前预热时间（秒）")
    private Integer startBeforeFaultSec;

    @ApiModelProperty("压测持续时长（秒）")
    private Integer trafficDurationSec;

    @ApiModelProperty("压测异常时是否中止演练")
    private Boolean abortOnLoadFailure;

    @ApiModelProperty("命名空间")
    private String namespace;

    /**
     * 用户信息（由Controller层设置）
     */
    private ChaosUser user;
}
