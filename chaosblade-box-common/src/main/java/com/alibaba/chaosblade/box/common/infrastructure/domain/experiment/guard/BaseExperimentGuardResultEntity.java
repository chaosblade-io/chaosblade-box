package com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.guard;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author haibin
 *
 *
 */
@Data
public class BaseExperimentGuardResultEntity {

    /**
     * 指标分类名称,比如CPU
     */
    private String name;

    private String guardId;

    private GuardRunState state;

    private String alias;

    /**
     * 图标数据
     */
    private List<ExperimentGuardMetricDataItem> data=new ArrayList<>();

    /**
     * 具体哪一项Metric,比如:Cpu下面的系统利用率
     */
    private String subName;

    /**
     * 图标数据查询不到的错误原因.
     */
    private String failureDetail;

    /**
     * 失败code
     */
    private String failureCode;

    private Long lastRecordPoint;

}
