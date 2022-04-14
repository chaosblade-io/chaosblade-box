package com.alibaba.chaosblade.box.service.model.expertise;

import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.response.ExperimentFlowInfo;
import com.alibaba.chaosblade.box.dao.model.ExpertiseRunTimeInfo;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * 经验库执行信息
 *
 * @author haibin
 *
 *
 */
@Data
public class ExpertiseExecutableInfo {

    /**
     * 运行环境信息
     */
    @JSONField(name = "run_time")
    private ExpertiseRunTimeInfo runTime;

    /**
     * 演练流程定义
     */
    private ExperimentFlowInfo flow;
}
