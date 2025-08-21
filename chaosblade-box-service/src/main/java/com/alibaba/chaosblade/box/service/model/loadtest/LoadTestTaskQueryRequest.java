package com.alibaba.chaosblade.box.service.model.loadtest;

import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 压测任务查询请求
 *
 * @author ZhengMingZhuo
 */
@Data
@ApiModel("压测任务查询请求")
public class LoadTestTaskQueryRequest {

    @ApiModelProperty(value = "页码", required = true)
    private Integer pageNum = 1;

    @ApiModelProperty(value = "页大小", required = true)
    private Integer pageSize = 10;

    @ApiModelProperty(value = "压测策略ID")
    private String strategyId;

    @ApiModelProperty(value = "演练任务ID")
    private String experimentTaskId;

    @ApiModelProperty(value = "任务状态")
    private String status;

    @ApiModelProperty(value = "命名空间")
    private String namespace;

    /**
     * 用户信息（由Controller层设置）
     */
    private ChaosUser user;
}
