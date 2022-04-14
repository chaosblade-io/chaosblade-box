package com.alibaba.chaosblade.box.service.command.task;

import com.alibaba.chaosblade.box.cache.templates.distributed.RedisCacheTemplate;
import com.alibaba.chaosblade.box.common.commands.SpringBeanCommand;
import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.common.common.domain.task.BaseExperimentTaskRequest;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.guard.BaseExperimentGuardResultEntity;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.guard.ExperimentGuardMonitorMetricResultEntity;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.guard.ExperimentGuardRecoveryStrategyResultEntity;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.guard.ExperimentTaskGuardsResult;
import com.alibaba.chaosblade.box.dao.infrastructure.checker.ExperimentChecker;
import com.alibaba.chaosblade.box.dao.model.ExperimentGuardDO;
import com.alibaba.chaosblade.box.dao.model.ExperimentGuardInstanceDO;
import com.alibaba.chaosblade.box.dao.model.ExperimentTaskDO;
import com.alibaba.chaosblade.box.dao.repository.ExperimentGuardInstanceRepository;
import com.alibaba.chaosblade.box.service.command.guard.ExperimentGuardInstanceExecutionCommand;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.guard.ExperimentGuardInstanceExecutionRequest;
import com.alibaba.chaosblade.box.service.impl.ExperimentGuardInstanceServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 查询演练活动对应的全局节点状态
 *
 * @author haibin
 *
 *
 */
@Slf4j
@Component
public class ExperimentTaskGuardInfoQueryCommand
        extends SpringBeanCommand<BaseExperimentTaskRequest, Response<ExperimentTaskGuardsResult>> {

    @Autowired
    private ExperimentChecker experimentChecker;

    @Autowired
    private RedisCacheTemplate redisTemplate;

    @Autowired
    private ExperimentGuardInstanceRepository experimentGuardInstanceRepository;

    @Override
    public Response<ExperimentTaskGuardsResult> execute(BaseExperimentTaskRequest baseExperimentTaskRequest) {
        Response<ExperimentTaskDO> experimentTaskDOResponse = experimentChecker.assertExperimentTaskExist(
                baseExperimentTaskRequest.getTaskId());
        if (!experimentTaskDOResponse.isSuccess()) {
            return Response.failedWith(experimentTaskDOResponse.getError());
        }
        ExperimentTaskDO experimentTaskDO = experimentTaskDOResponse.getResult();
        ExperimentTaskGuardsResult experimentTaskGuardsResult = new ExperimentTaskGuardsResult();
        List<ExperimentGuardInstanceDO> experimentGuardInstanceDOS = experimentGuardInstanceRepository
                .findByExperimentTaskId(
                        experimentTaskDO.getTaskId());
        commandBus.asyncRun(ExperimentGuardInstanceExecutionCommand.class,
                new ExperimentGuardInstanceExecutionRequest(experimentTaskDO, experimentGuardInstanceDOS, false));
        transferMetrics(experimentTaskGuardsResult, experimentGuardInstanceDOS);
        return Response.okWithData(experimentTaskGuardsResult);
    }

    private void transferMetrics(ExperimentTaskGuardsResult experimentTaskGuardsResult,
                                 List<ExperimentGuardInstanceDO> experimentGuardInstanceDOS) {
        //这个是Metric类型的数据结果
        List<BaseExperimentGuardResultEntity> metrics = new ArrayList<>();
        experimentTaskGuardsResult.setMetrics(metrics);
        //这个是自我保护的结果
        List<ExperimentGuardRecoveryStrategyResultEntity> recoveryStrategyResultEntities = new ArrayList<>();
        experimentTaskGuardsResult.setStrategies(recoveryStrategyResultEntities);
        for (ExperimentGuardInstanceDO experimentGuardInstanceDO : experimentGuardInstanceDOS) {
            if (ExperimentGuardDO.ACTION_TYPE_MONITOR.equals(experimentGuardInstanceDO.getActionType())) {
                metrics.add(convertBaseExperimentGuardResultEntity(experimentGuardInstanceDO));
            }
            if (ExperimentGuardDO.ACTION_TYPE_RECOVERY.equals(experimentGuardInstanceDO.getActionType())) {
                recoveryStrategyResultEntities.add(
                        convertExperimentGuardRecoveryStrategyResultEntity(experimentGuardInstanceDO));
            }
            if (ExperimentGuardDO.ACTION_TYPE_TIMEOUT_RECOVERY.equals(experimentGuardInstanceDO.getActionType())) {
                recoveryStrategyResultEntities.add(
                        convertExperimentGuardRecoveryStrategyResultEntity(experimentGuardInstanceDO));
            }
        }
    }

    /**
     * 转换成恢复策略的结果
     *
     * @param experimentGuardInstanceDO
     * @return
     */
    private ExperimentGuardRecoveryStrategyResultEntity convertExperimentGuardRecoveryStrategyResultEntity(
            ExperimentGuardInstanceDO experimentGuardInstanceDO) {
        ExperimentGuardRecoveryStrategyResultEntity experimentGuardRecoveryStrategyResultEntity
                = new ExperimentGuardRecoveryStrategyResultEntity();
        experimentGuardRecoveryStrategyResultEntity.setGuardId(experimentGuardInstanceDO.getGuardId());
        experimentGuardRecoveryStrategyResultEntity.setName(experimentGuardInstanceDO.getName());
        experimentGuardRecoveryStrategyResultEntity.setState(experimentGuardInstanceDO.getState());
        experimentGuardRecoveryStrategyResultEntity.setFields(experimentGuardInstanceDO.getArgument().getFields());
        experimentGuardRecoveryStrategyResultEntity.setTolerance(
                experimentGuardInstanceDO.getArgument().getTolerance());
        return experimentGuardRecoveryStrategyResultEntity;
    }

    /**
     * 转换成监控策略的结果
     *
     * @param experimentGuardInstanceDO
     * @return
     */
    private BaseExperimentGuardResultEntity convertBaseExperimentGuardResultEntity(
            ExperimentGuardInstanceDO experimentGuardInstanceDO) {
        ExperimentGuardMonitorMetricResultEntity experimentGuardMonitorMetricResultEntity = experimentGuardInstanceDO
                .getValue();

        if(null == experimentGuardMonitorMetricResultEntity) {
            experimentGuardMonitorMetricResultEntity = getExperimentGuardMonitorMetricResultEntityFormRedis(experimentGuardInstanceDO);
        }
        if (experimentGuardMonitorMetricResultEntity != null) {
            return experimentGuardMonitorMetricResultEntity;
        }
        BaseExperimentGuardResultEntity baseExperimentGuardResultEntity = new BaseExperimentGuardResultEntity();
        baseExperimentGuardResultEntity.setState(experimentGuardInstanceDO.getState());
        baseExperimentGuardResultEntity.setGuardId(experimentGuardInstanceDO.getGuardId());
        baseExperimentGuardResultEntity.setName(experimentGuardInstanceDO.getName());
        return baseExperimentGuardResultEntity;
    }

    private ExperimentGuardMonitorMetricResultEntity getExperimentGuardMonitorMetricResultEntityFormRedis(ExperimentGuardInstanceDO experimentGuardInstanceDO) {
        Serializable serializable = redisTemplate.prefixGet(ExperimentGuardInstanceServiceImpl.PRE,experimentGuardInstanceDO.getInstanceId());
        if(null != serializable) {
            return (ExperimentGuardMonitorMetricResultEntity)serializable;
        }
        return new ExperimentGuardMonitorMetricResultEntity();
    }

}
