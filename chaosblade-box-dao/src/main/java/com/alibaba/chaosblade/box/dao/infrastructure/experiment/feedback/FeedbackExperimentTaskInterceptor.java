package com.alibaba.chaosblade.box.dao.infrastructure.experiment.feedback;

import com.alibaba.chaosblade.box.common.experiment.task.ExperimentTaskContext;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.interceptor.ExperimentTaskInterceptor;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.request.ExperimentTaskCreateRequest;
import com.alibaba.chaosblade.box.dao.model.ExperimentDO;
import com.alibaba.chaosblade.box.dao.model.ExperimentTaskDO;
import org.springframework.stereotype.Component;

/**
 * 在演练任务创建之前将强弱依赖的标签打上
 *
 * @author haibin.lhb
 *
 *
 */
@Component
public class FeedbackExperimentTaskInterceptor implements ExperimentTaskInterceptor {

    @Override
    public void beforeSaveExperimentTaskDO(ExperimentTaskCreateRequest experimentTaskCreateRequest,
                                           ExperimentTaskDO experimentTaskDO) {
        ExperimentDO experimentDO = experimentTaskCreateRequest.getExperimentDO();
        ExperimentTaskContext experimentTaskContext = experimentTaskDO.getExperimentTaskContext();
        experimentTaskContext.setOuterId(experimentDO.getOuterId());
        experimentTaskContext.setSource(experimentDO.getSource());
    }
}
