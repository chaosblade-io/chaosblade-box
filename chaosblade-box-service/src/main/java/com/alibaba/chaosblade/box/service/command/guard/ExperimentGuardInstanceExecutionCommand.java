package com.alibaba.chaosblade.box.service.command.guard;

import com.alibaba.chaosblade.box.common.commands.SpringBeanCommand;
import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.guard.ExperimentGuardHostsProvider;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.guard.ExperimentGuardInstanceExecutionRequest;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.guard.ExperimentGuardResultLoadRequest;
import com.alibaba.chaosblade.box.dao.model.ExperimentGuardDO;
import com.alibaba.chaosblade.box.dao.model.ExperimentGuardInstanceDO;
import com.alibaba.chaosblade.box.dao.model.ExperimentTaskDO;
import com.alibaba.chaosblade.box.dao.repository.ExperimentGuardInstanceRepository;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.core.LockConfiguration;
import net.javacrumbs.shedlock.core.LockingTaskExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;

/**
 * 全局节点的真实执行入口
 *
 * @author haibin
 *
 *
 */
@Component
@Slf4j
public class ExperimentGuardInstanceExecutionCommand
    extends SpringBeanCommand<ExperimentGuardInstanceExecutionRequest, Response> {

    @Autowired
    private ExperimentGuardHostsProvider experimentGuardHostsProvider;

    @Autowired
    private ExperimentGuardInstanceRepository experimentGuardInstanceRepository;

    @Autowired
    private LockingTaskExecutor lockingTaskExecutor;

    @Override
    public Response execute(ExperimentGuardInstanceExecutionRequest experimentGuardInstanceExecutionRequest) {
        ExperimentTaskDO experimentTaskDO = experimentGuardInstanceExecutionRequest.getExperimentTaskDO();
        List<ExperimentGuardInstanceDO> experimentGuardInstances = experimentGuardInstanceExecutionRequest
            .getExperimentGuardInstances();
        ExperimentGuardResultLoadRequest experimentGuardResultLoadRequest = new ExperimentGuardResultLoadRequest();
        //获取本次全局节点涉及的机器范围
        experimentGuardResultLoadRequest.setHosts(
            experimentGuardHostsProvider.provide(experimentGuardInstanceExecutionRequest));
        experimentGuardResultLoadRequest.setExperimentTaskDO(experimentTaskDO);
        //根据不同的策略来获取数据
        for (ExperimentGuardInstanceDO experimentGuardInstanceDO : experimentGuardInstances) {
            lockingTaskExecutor.executeWithLock(new Runnable() {
                @Override
                public void run() {
                    experimentGuardResultLoadRequest.setExperimentGuardInstanceDO(experimentGuardInstanceDO);
                    if (experimentTaskDO.isRunning()) {
                        if (ExperimentGuardDO.ACTION_TYPE_RECOVERY.equals(
                            experimentGuardInstanceDO.getActionType())) {
                            if (experimentTaskDO.isRunning()) {
                                commandBus.syncRun(ExperimentGuardRecoveryLoadCommand.class,
                                    experimentGuardResultLoadRequest);
                            }
                        }
                        if (ExperimentGuardDO.ACTION_TYPE_TIMEOUT_RECOVERY.equals(
                            experimentGuardInstanceDO.getActionType())) {
                            commandBus.syncRun(ExperimentAutoRecoveryLoadCommand.class,
                                experimentGuardResultLoadRequest);
                        }
                    }
                    //全局监控节点
                    if (ExperimentGuardDO.ACTION_TYPE_MONITOR.equals(experimentGuardInstanceDO.getActionType())) {
                        commandBus.syncRun(ExperimentGuardMetricLoadCommand.class,
                            experimentGuardResultLoadRequest);
                    }
                }
            }, buildLockConfiguration(experimentGuardInstanceDO));
        }
        return Response.ok();
    }

    private LockConfiguration buildLockConfiguration(ExperimentGuardInstanceDO experimentGuardInstanceDO) {
        return new LockConfiguration("guard-instance-" + experimentGuardInstanceDO.getInstanceId(),
            Duration.ofMinutes(2), Duration.ofSeconds(10));
    }

}
