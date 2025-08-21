package com.alibaba.chaosblade.box.service.model.loadtest;

import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 压测定义查询请求
 *
 * @author ZhengMingZhuo
 */
@Data
@ApiModel("压测定义查询请求")
public class LoadTestDefinitionQueryRequest {

    @ApiModelProperty("页码，从1开始")
    private Integer pageNum = 1;

    @ApiModelProperty("页大小")
    private Integer pageSize = 20;

    @ApiModelProperty("压测定义名称（模糊查询）")
    private String name;

    @ApiModelProperty("引擎类型：JMETER、K6、LOCUST、CUSTOM")
    private String engineType;

    @ApiModelProperty("命名空间")
    private String namespace;

    @ApiModelProperty(value = "用户信息", hidden = true)
    private ChaosUser user;
}
