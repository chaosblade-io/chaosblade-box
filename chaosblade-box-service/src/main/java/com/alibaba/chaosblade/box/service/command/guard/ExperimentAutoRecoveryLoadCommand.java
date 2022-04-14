package com.alibaba.chaosblade.box.service.command.guard;

import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.guard.GuardRunState;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.request.ExperimentTaskStopRequest;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.guard.ExperimentGuardResultLoadRequest;
import com.alibaba.chaosblade.box.dao.model.ExperimentGuardInstanceDO;
import com.alibaba.chaosblade.box.dao.model.ExperimentTaskDO;
import com.alibaba.chaosblade.box.dao.command.task.ExperimentTaskStopCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 全局自动恢复节点执行命令
 *
 * @author haibin
 *
 *
 */
@Component
@Slf4j
public class ExperimentAutoRecoveryLoadCommand
    extends BaseExperimentGuardLoadCommand<Response> {

    @Override
    public Response internalExecute(
        ExperimentGuardResultLoadRequest experimentGuardResultLoadRequest) {
        ExperimentTaskDO experimentTaskDO = experimentGuardResultLoadRequest.getExperimentTaskDO();
        if (needRecovery(experimentTaskDO)) {
            ExperimentTaskStopRequest experimentTaskStopRequest = new ExperimentTaskStopRequest();
            experimentTaskStopRequest.setTaskId(experimentTaskDO.getTaskId());
            ChaosUser user = new ChaosUser();
            user.setUserId(experimentTaskDO.getUserId());
            experimentTaskStopRequest.setUser(user);
            experimentTaskStopRequest.setFromScheduler(true);
            commandBus.syncRun(ExperimentTaskStopCommand.class, experimentTaskStopRequest);
            ExperimentGuardInstanceDO experimentGuardInstanceDO = experimentGuardResultLoadRequest
                .getExperimentGuardInstanceDO();
            experimentGuardInstanceDO.setState(GuardRunState.TRIGGERED);
            experimentGuardInstanceDO.setTriggeredReason(ExperimentGuardRecoveryLoadCommand.TRIGGER_REASON_TEMPLATE + "自动恢复");
            experimentGuardInstanceRepository.update(experimentGuardInstanceDO);
        }
        return Response.ok();
    }

    private boolean needRecovery(ExperimentTaskDO experimentTaskDO) {
        return (experimentTaskDO.getDuration() > 0)
            && (System.currentTimeMillis() - experimentTaskDO.getGmtCreate().getTime()) >= (experimentTaskDO
            .getDuration() * 1000);
    }
}
