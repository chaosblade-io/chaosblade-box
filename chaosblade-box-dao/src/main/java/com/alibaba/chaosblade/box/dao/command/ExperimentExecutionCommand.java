package com.alibaba.chaosblade.box.dao.command;

import com.alibaba.chaosblade.box.common.commands.CommandBus;
import com.alibaba.chaosblade.box.common.commands.SpringBeanCommand;
import com.alibaba.chaosblade.box.common.common.domain.ChaosError;
import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.common.common.domain.experiment.ExperimentRunResult;
import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import com.alibaba.chaosblade.box.dao.infrastructure.checker.ExperimentChecker;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.request.ExperimentTaskCreateRequest;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.ExperimentTaskManager;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.ExperimentTaskRunnableSettings;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.activity.ExperimentExecuteContext;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.request.ExperimentRunRequest;
import com.alibaba.chaosblade.box.common.infrastructure.util.ChaosTraceUtil;
import com.alibaba.chaosblade.box.dao.infrastructure.event.ExperimentTaskStartedEvent;
import com.alibaba.chaosblade.box.dao.infrastructure.event.ChaosEventDispatcher;
import com.alibaba.chaosblade.box.dao.model.ActivityTaskDO;
import com.alibaba.chaosblade.box.dao.model.ExperimentDO;
import com.alibaba.chaosblade.box.dao.model.ExperimentTaskDO;
import com.alibaba.chaosblade.box.dao.repository.ActivityTaskRepository;
import com.alibaba.chaosblade.box.dao.command.task.ActivityTaskExecutionCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author haibin
 *
 * 
 */

@Component
public class ExperimentExecutionCommand extends SpringBeanCommand<ExperimentRunRequest, Response<ExperimentRunResult>> {

    @Autowired
    private ExperimentTaskManager experimentTaskManager;

    @Resource
    private ChaosEventDispatcher chaosEventDispatcher;

    @Autowired
    private CommandBus commandBus;

    @Autowired
    private ActivityTaskRepository activityTaskRepository;

    @Resource
    private ExperimentChecker experimentChecker;

    @Override
    public Response<ExperimentRunResult> execute(ExperimentRunRequest experimentRunRequest) {
        String experimentId = experimentRunRequest.getExperimentId();
        ChaosUser user = experimentRunRequest.getUser();
        Response<ExperimentDO> response = experimentChecker.assertExperimentExist(experimentId);
        if (!response.isSuccess()) {
            return Response.failedWith(response.getError());
        }
        ChaosError ChaosError = experimentChecker.checkExperimentRunnable(experimentRunRequest, response.getResult());
        if (ChaosError != null) {
            return Response.failedWith(ChaosError);
        }
        ExperimentDO experimentDO = response.getResult();
        ExperimentTaskCreateRequest experimentTaskCreateRequest
                = ExperimentTaskCreateRequest.buildExperimentTaskCreateRequest(experimentRunRequest,
                experimentDO);
        //创建演练任务
        Response<ExperimentTaskDO> createResponse = experimentTaskManager.createTask(experimentTaskCreateRequest);
        if (!createResponse.isSuccess()) {
            return Response.failedWith(createResponse.getError());
        }
        String experimentTaskId = createResponse.getResult().getTaskId();
        ActivityTaskDO activityTaskDO = activityTaskRepository.findById(createResponse.getResult().getActivityTaskId())
                .orElse(null);
        runTask(experimentRunRequest, user, activityTaskDO);
        ExperimentRunResult experimentRunResult = new ExperimentRunResult();
        experimentRunResult.setTaskId(experimentTaskId);
        sendNotification(experimentRunRequest, experimentId, user, createResponse, experimentTaskId);
        return Response.okWithData(experimentRunResult);
    }

    private void sendNotification(ExperimentRunRequest experimentRunRequest, String experimentId, ChaosUser user,
                                  Response<ExperimentTaskDO> createResponse, String experimentTaskId) {
        chaosEventDispatcher.fireEvent(new ExperimentTaskStartedEvent(user, createResponse.getResult()));
    }

    private void runTask(ExperimentRunRequest experimentRunRequest, ChaosUser user, ActivityTaskDO activityTaskDO) {
        ExperimentExecuteContext experimentExecuteContext = new ExperimentExecuteContext(
                ChaosTraceUtil.generateTraceId(), user,
                activityTaskDO,
                new ExperimentTaskRunnableSettings()
        );
        if (experimentRunRequest.isSync()) {
            commandBus.syncRun(new ActivityTaskExecutionCommand(experimentExecuteContext));
        } else {
            commandBus.asyncRun(new ActivityTaskExecutionCommand(experimentExecuteContext));
        }
    }

}
