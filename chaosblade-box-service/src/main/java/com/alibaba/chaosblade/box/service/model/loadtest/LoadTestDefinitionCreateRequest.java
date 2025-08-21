package com.alibaba.chaosblade.box.service.model.loadtest;

import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;



/**
 * 压测定义创建请求
 *
 * @author ZhengMingZhuo
 */
@Data
@ApiModel("压测定义创建请求")
public class LoadTestDefinitionCreateRequest {

    @ApiModelProperty(value = "压测定义名称", required = true)
    private String name;

    @ApiModelProperty(value = "引擎类型：JMETER、K6、LOCUST、CUSTOM", required = true)
    private String engineType;

    @ApiModelProperty(value = "目标端点", required = true)
    private String endpoint;

    @ApiModelProperty(value = "入口类型：JMX、SCRIPT、URL", required = true)
    private String entry;

    @ApiModelProperty("文件URL引用")
    private String contentRef;

    @ApiModelProperty("URL型用例配置")
    private Object urlCase;

    @ApiModelProperty("命名空间")
    private String namespace;

    @ApiModelProperty(value = "用户信息", hidden = true)
    private ChaosUser user;
}
