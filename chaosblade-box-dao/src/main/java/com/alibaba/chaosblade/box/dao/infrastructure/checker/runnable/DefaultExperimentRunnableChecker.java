package com.alibaba.chaosblade.box.dao.infrastructure.checker.runnable;

import com.alibaba.chaosblade.box.common.common.domain.ChaosError;
import com.alibaba.chaosblade.box.common.common.enums.CommonErrorCode;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.request.ExperimentRunRequest;
import com.alibaba.chaosblade.box.dao.model.ExperimentDO;
import com.alibaba.chaosblade.box.dao.model.ExperimentTaskDO;
import com.alibaba.chaosblade.box.dao.repository.ExperimentTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author haibin
 *
 *
 */
@Component
@Order(1000)
public class DefaultExperimentRunnableChecker implements ExperimentRunnableChecker {

    @Autowired
    private ExperimentTaskRepository experimentTaskRepository;

    @Override
    public ChaosError checkRunnable(ExperimentRunRequest experimentRunRequest, ExperimentDO experimentDO) {
        String experimentTaskId = experimentDO.getExperimentTaskId();
        //没有任务在运行
        if (experimentDO.isNotReady()) {
            return ChaosError.withCode(CommonErrorCode.B_EXPERIMENT_DRAFT_STATE);
        }
        if (experimentTaskId != null) {
            Optional<ExperimentTaskDO> experimentTaskDOOptional = experimentTaskRepository.findById(experimentTaskId);
            if (experimentTaskDOOptional.isPresent()) {
                ExperimentTaskDO experimentTaskDO = experimentTaskDOOptional.get();
                if (!experimentTaskDO.isFinished()) {
                    return new ChaosError(CommonErrorCode.B_EXPERIMENT_ALREADY_RUNNING);
                }
            }
        }
        return null;
    }
}
