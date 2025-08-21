package com.alibaba.chaosblade.box.service.model.loadtest;

import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * JMX文件上传请求
 *
 * @author ZhengMingZhuo
 */
@Data
@ApiModel("JMX文件上传请求")
public class JmxFileUploadRequest {

    @ApiModelProperty(value = "JMX文件数据", required = true)
    private FileUploadData file;

    @ApiModelProperty(value = "压测引擎端点", required = true, example = "http://1.94.151.57:8008")
    private String endpoint;

    @ApiModelProperty("命名空间")
    private String namespace;

    @ApiModelProperty(value = "用户信息", hidden = true)
    private ChaosUser user;
}
