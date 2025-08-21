package com.alibaba.chaosblade.box.service.model.loadtest;

import com.alibaba.chaosblade.box.dao.model.LoadTestDefinitionDO;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 压测定义视图对象
 *
 * @author ZhengMingZhuo
 */
@Data
@ApiModel("压测定义视图对象")
public class LoadTestDefinitionVO {

    @ApiModelProperty("压测定义ID")
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

    @ApiModelProperty("创建者")
    private String createdBy;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX", timezone = "Asia/Shanghai")
    private Date createdAt;

    @ApiModelProperty("更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX", timezone = "Asia/Shanghai")
    private Date updatedAt;

    /**
     * 从 DO 对象转换为 VO 对象
     *
     * @param loadTestDefinitionDO DO 对象
     * @return VO 对象
     */
    public static LoadTestDefinitionVO from(LoadTestDefinitionDO loadTestDefinitionDO) {
        if (loadTestDefinitionDO == null) {
            return null;
        }

        LoadTestDefinitionVO vo = new LoadTestDefinitionVO();
        vo.setId(loadTestDefinitionDO.getDefinitionId());
        vo.setName(loadTestDefinitionDO.getName());
        vo.setEngineType(loadTestDefinitionDO.getEngineType());
        vo.setEndpoint(loadTestDefinitionDO.getEndpoint());
        vo.setEntry(loadTestDefinitionDO.getEntry());
        vo.setContentRef(loadTestDefinitionDO.getContentRef());
        vo.setUrlCase(loadTestDefinitionDO.getUrlCase());
        vo.setCreatedBy(loadTestDefinitionDO.getCreatedBy());
        vo.setCreatedAt(loadTestDefinitionDO.getGmtCreate());
        vo.setUpdatedAt(loadTestDefinitionDO.getGmtModified());

        return vo;
    }
}
