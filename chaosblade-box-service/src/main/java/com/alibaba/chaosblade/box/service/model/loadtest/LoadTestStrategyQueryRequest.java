package com.alibaba.chaosblade.box.service.model.loadtest;

import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 压测策略查询请求
 *
 * @author ZhengMingZhuo
 */
@Data
@ApiModel("压测策略查询请求")
public class LoadTestStrategyQueryRequest {

    @ApiModelProperty("页码")
    private Integer pageNum = 1;

    @ApiModelProperty("页大小")
    private Integer pageSize = 20;

    @ApiModelProperty("压测定义ID")
    private String definitionId;

    @ApiModelProperty("实验ID")
    private String experimentId;

    @ApiModelProperty("是否启用")
    private Boolean enable;

    @ApiModelProperty("命名空间")
    private String namespace;

    /**
     * 用户信息（由Controller层设置）
     */
    private ChaosUser user;
}
