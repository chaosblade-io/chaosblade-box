package com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.async;

import com.alibaba.chaosblade.box.dao.model.ChaosBladeExpUidDO;
import com.alibaba.chaosblade.box.dao.model.ExperimentMiniAppTaskDO;
import lombok.Data;

/**
 * @author sunpeng
 * 
 *
 */
@Data
public class AsyncCallBackContext {

    private ExperimentMiniAppTaskDO experimentMiniAppTaskDO;

    private ChaosBladeExpUidDO chaosBladeExpUidDO;

    private String uid;

    private String status;

    private String error;

    private String toolType;

    public AsyncCallBackContext() {}

    public AsyncCallBackContext(ExperimentMiniAppTaskDO experimentMiniAppTaskDO, ChaosBladeExpUidDO chaosBladeExpUidDO,
                                String uid, String status, String error, String toolType) {
        this.experimentMiniAppTaskDO = experimentMiniAppTaskDO;
        this.chaosBladeExpUidDO = chaosBladeExpUidDO;
        this.status = status;
        this.uid = uid;
        this.toolType = toolType;
        this.error = error;
    }

}
