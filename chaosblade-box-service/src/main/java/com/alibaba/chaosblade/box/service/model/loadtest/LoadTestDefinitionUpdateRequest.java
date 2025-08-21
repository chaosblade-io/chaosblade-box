package com.alibaba.chaosblade.box.service.model.loadtest;

import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;



/**
 * 压测定义更新请求
 *
 * @author ZhengMingZhuo
 */
@Data
@ApiModel("压测定义更新请求")
public class LoadTestDefinitionUpdateRequest {

    @ApiModelProperty(value = "压测定义ID", required = true)
    private String id;

    @ApiModelProperty("压测定义名称")
    private String name;

    @ApiModelProperty("引擎类型：JMETER、K6、LOCUST、CUSTOM")
    private String engineType;

    @ApiModelProperty("目标端点")
    private String endpoint;

    @ApiModelProperty("入口类型：JMX、SCRIPT、URL")
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
