package com.alibaba.chaosblade.box.dao.command.task;


import com.alibaba.chaosblade.box.common.commands.SpringBeanCommand;
import com.alibaba.chaosblade.box.common.common.domain.ChaosError;
import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.common.common.domain.experiment.ActivityRunParam;
import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import com.alibaba.chaosblade.box.common.common.enums.CommonErrorCode;
import com.alibaba.chaosblade.box.common.common.enums.ExperimentStateEnum;
import com.alibaba.chaosblade.box.common.common.enums.ExperimentTaskTypeEnum;
import com.alibaba.chaosblade.box.common.common.enums.StateEnum;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.interceptor.ExperimentTaskInterceptor;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.request.ExperimentTaskCreateRequest;
import com.alibaba.chaosblade.box.common.experiment.task.ExperimentTaskContext;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.ActivityTaskManager;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.ActivityTasksCreateRequest;
import com.alibaba.chaosblade.box.dao.model.ActivityTaskDO;
import com.alibaba.chaosblade.box.dao.model.ExperimentDO;
import com.alibaba.chaosblade.box.dao.model.ExperimentTaskDO;
import com.alibaba.chaosblade.box.dao.query.ExperimentTaskQuery;
import com.alibaba.chaosblade.box.dao.repository.ExperimentRepository;
import com.alibaba.chaosblade.box.dao.repository.ExperimentTaskRepository;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author haibin
 *
 *
 */
@Component
public class ExperimentTaskCreateCommand
    extends SpringBeanCommand<ExperimentTaskCreateRequest, Response<ExperimentTaskDO>> {

    private static Logger logger = LoggerFactory.getLogger(ExperimentTaskCreateCommand.class);

    @Autowired
    private ExperimentTaskRepository experimentTaskRepository;

    @Autowired
    ExperimentRepository experimentRepository;

    @Autowired
    private ActivityTaskManager activityTaskManager;

    @Autowired
    private List<ExperimentTaskInterceptor> experimentTaskInterceptors;

    @Override
    public Response<ExperimentTaskDO> execute(ExperimentTaskCreateRequest experimentTaskCreateRequest) {
        ExperimentDO experimentDO = experimentTaskCreateRequest.getExperimentDO();
        String experimentTaskId = null;
        try {
            ChaosUser user = experimentTaskCreateRequest.getUser();
            String experimentId = experimentDO.getExperimentId();
            ExperimentTaskDO experimentTaskDO = new ExperimentTaskDO();
            experimentTaskDO.setState(StateEnum.RUNNING.getValue());
            experimentTaskDO.setTaskType(ExperimentTaskTypeEnum.AUTO.getValue());
            experimentTaskDO.setExperimentId(experimentId);
            experimentTaskDO.setTaskId(IdWorker.getIdStr());
            experimentTaskDO.setNamespace(experimentTaskCreateRequest.getNamespace());
            experimentTaskDO.setOuterId(experimentTaskCreateRequest.getOuterId());
            experimentTaskDO.setExperimentTaskContext(buildExperimentTaskContext(experimentTaskCreateRequest));
            experimentTaskDO.setDuration(experimentDO.getDuration());
            experimentTaskDO.setName(experimentDO.getName());
            experimentTaskDO.setUserId(user.getUserId());
            if (experimentTaskCreateRequest.getExperimentRunParam() != null) {
                experimentTaskDO.setOuterUserId(experimentTaskCreateRequest.getExperimentRunParam().getEmpId());
            }
            experimentTaskDO.setOuterUserId(user.getCurrentUserId());
            beforeSaveExperimentTaskDO(experimentTaskCreateRequest, experimentTaskDO);
            experimentTaskRepository.add(experimentTaskDO);
            experimentTaskId = experimentTaskDO.getTaskId();
            //然后创建子任务
            List<ActivityRunParam> activityRunParams =
                experimentTaskCreateRequest.getExperimentRunParam() != null ? experimentTaskCreateRequest
                    .getExperimentRunParam().getActivities() : null;
            //创建每个活动的任务
            ActivityTasksCreateRequest activityTasksCreateRequest = new ActivityTasksCreateRequest();
            activityTasksCreateRequest.setChaosUser(experimentTaskCreateRequest.getUser());
            activityTasksCreateRequest.setExperimentDO(experimentDO);
            activityTasksCreateRequest.setExperimentTaskDO(experimentTaskDO);
            activityTasksCreateRequest.setParams(activityRunParams);
            activityTasksCreateRequest.setExperimentRunModeEnum(experimentDO.getRunMode());
            List<ActivityTaskDO> activityTaskDOS = activityTaskManager
                .createActivityTasks(activityTasksCreateRequest);
            experimentTaskDO.setActivityTaskId(activityTaskDOS.get(0).getTaskId());
            //然后绑定到演练上
            ExperimentDO updateExperimentDO = new ExperimentDO();
            updateExperimentDO.setExperimentId(experimentId);
            updateExperimentDO.setExperimentTaskId(experimentTaskDO.getTaskId());
            updateExperimentDO.setState(ExperimentStateEnum.RUNNING.getValue());
            updateExperimentDO.setResult(null);
            experimentRepository.update(updateExperimentDO);
            return Response.okWithData(experimentTaskDO);
        } catch (Exception ex) {
            logger.error("create experiment task failed", ex);
            if (experimentTaskId != null) {
                ExperimentTaskQuery experimentTaskQuery = new ExperimentTaskQuery();
                experimentTaskQuery.setTaskId(experimentTaskId);
                experimentTaskRepository.delete(experimentTaskQuery);
            }
            return Response.failedWith(throwableChaosErrorWrappers
                .wrapper(ex, new ChaosError(CommonErrorCode.B_CREATE_EXPERIMENT_TASK_FAILED)));
        }
    }

    private ExperimentTaskContext buildExperimentTaskContext(ExperimentTaskCreateRequest experimentTaskCreateRequest) {
        ExperimentTaskContext experimentTaskContext = new ExperimentTaskContext();
        experimentTaskContext.setSchedulerConfig(experimentTaskCreateRequest.getExperimentDO().getSchedulerConfig());
        return experimentTaskContext;
    }

    private void beforeSaveExperimentTaskDO(ExperimentTaskCreateRequest experimentTaskCreateRequest,
        ExperimentTaskDO experimentTaskDO) {
        for (ExperimentTaskInterceptor experimentTaskInterceptor : experimentTaskInterceptors) {
            experimentTaskInterceptor.beforeSaveExperimentTaskDO(experimentTaskCreateRequest, experimentTaskDO);
        }
    }
}
