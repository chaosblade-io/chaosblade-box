package com.alibaba.chaosblade.box.dao.infrastructure.checker.runnable;


import com.alibaba.chaosblade.box.common.common.domain.ChaosError;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.request.ExperimentRunRequest;
import com.alibaba.chaosblade.box.dao.model.ExperimentDO;

/**
 * 判断一个演练是否能运行的接口
 *
 * @author haibin
 *
 * 
 */
public interface ExperimentRunnableChecker {

    /**
     * 演练是否能运行
     *
     * @param experimentDO         演练对象
     * @param experimentRunRequest experiment Run request
     * @return ChaosError 如果可以返回Null
     */
    ChaosError checkRunnable(ExperimentRunRequest experimentRunRequest, ExperimentDO experimentDO);

}
