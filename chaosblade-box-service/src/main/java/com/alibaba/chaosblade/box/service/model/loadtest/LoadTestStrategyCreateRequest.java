package com.alibaba.chaosblade.box.service.model.loadtest;

import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 压测策略创建请求
 *
 * @author ZhengMingZhuo
 */
@Data
@ApiModel("压测策略创建请求")
public class LoadTestStrategyCreateRequest {

    @ApiModelProperty(value = "是否启用", required = true)
    private Boolean enable;

    @ApiModelProperty(value = "压测定义ID", required = true)
    private String definitionId;

    @ApiModelProperty(value = "实验ID", required = true)
    private String experimentId;

    @ApiModelProperty(value = "故障前预热时间（秒）", required = true)
    private Integer startBeforeFaultSec;

    @ApiModelProperty(value = "压测持续时长（秒）", required = true)
    private Integer trafficDurationSec;

    @ApiModelProperty(value = "压测异常时是否中止演练", required = true)
    private Boolean abortOnLoadFailure;

    @ApiModelProperty(value = "命名空间")
    private String namespace;

    /**
     * 用户信息（由Controller层设置）
     */
    private ChaosUser user;
}
