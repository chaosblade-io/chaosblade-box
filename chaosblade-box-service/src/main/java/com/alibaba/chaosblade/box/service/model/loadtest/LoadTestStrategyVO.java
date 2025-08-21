package com.alibaba.chaosblade.box.service.model.loadtest;

import com.alibaba.chaosblade.box.dao.model.LoadTestStrategyDO;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 压测策略视图对象
 *
 * @author ZhengMingZhuo
 */
@Data
@ApiModel("压测策略视图对象")
public class LoadTestStrategyVO {

    @ApiModelProperty("策略ID")
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

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX", timezone = "Asia/Shanghai")
    private Date createdAt;

    @ApiModelProperty("更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX", timezone = "Asia/Shanghai")
    private Date updatedAt;

    /**
     * 从DO对象转换为VO对象
     *
     * @param strategyDO DO对象
     * @return VO对象
     */
    public static LoadTestStrategyVO from(LoadTestStrategyDO strategyDO) {
        if (strategyDO == null) {
            return null;
        }

        LoadTestStrategyVO vo = new LoadTestStrategyVO();
        vo.setId(strategyDO.getStrategyId());
        vo.setEnable(strategyDO.getEnable() != null && strategyDO.getEnable() == 1);
        vo.setDefinitionId(strategyDO.getDefinitionId());
        vo.setExperimentId(strategyDO.getExperimentId());
        vo.setStartBeforeFaultSec(strategyDO.getStartBeforeFaultSec());
        vo.setTrafficDurationSec(strategyDO.getTrafficDurationSec());
        vo.setAbortOnLoadFailure(strategyDO.getAbortOnLoadFailure() != null && strategyDO.getAbortOnLoadFailure() == 1);
        vo.setCreatedAt(strategyDO.getGmtCreate());
        vo.setUpdatedAt(strategyDO.getGmtModified());

        return vo;
    }
}
