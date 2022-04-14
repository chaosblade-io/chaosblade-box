package com.alibaba.chaosblade.box.service.impl;

import com.alibaba.chaosblade.box.common.commands.CommandBus;
import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.common.common.domain.activity.ActivityTask;
import com.alibaba.chaosblade.box.common.common.domain.task.ActivityTaskResultConfirmRequest;
import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.request.ActivityRetryRequest;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.ActivityTaskManager;
import com.alibaba.chaosblade.box.service.ActivityTaskService;
import com.alibaba.chaosblade.box.service.command.task.ActivityTaskResultConfirmCommand;
import com.alibaba.chaosblade.box.service.command.task.ActivityTaskRetryCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author haibin
 *
 *
 */
@Component
@Slf4j
public class ActivityTaskServiceImpl implements ActivityTaskService {

    @Autowired
    private ActivityTaskManager activityTaskManager;

    @Autowired
    private CommandBus commandBus;

    @Override
    public Response<ActivityTask> findActivityTaskByActivityTaskId(String activityTaskId) {
        return activityTaskManager.queryFullActivityTaskInfo(activityTaskId);
    }

    @Override
    public Response<String> retryActivity(ActivityRetryRequest activityRetryRequest) {
        return commandBus.syncRun(ActivityTaskRetryCommand.class, activityRetryRequest);
    }

    @Override
    public Response<Void> confirmActivityTaskResult(ChaosUser user,
                                                    ActivityTaskResultConfirmRequest activityTaskResultConfirmRequest) {
        return commandBus.syncRun(ActivityTaskResultConfirmCommand.class, activityTaskResultConfirmRequest);
    }
}
