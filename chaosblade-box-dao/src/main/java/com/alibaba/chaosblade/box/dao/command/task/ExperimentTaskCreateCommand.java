package com.alibaba.chaosblade.box.dao.command.task;


import com.alibaba.chaosblade.box.common.commands.CommandBus;
import com.alibaba.chaosblade.box.common.commands.SpringBeanCommand;
import com.alibaba.chaosblade.box.common.common.domain.ChaosError;
import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.common.common.domain.experiment.ActivityRunParam;
import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import com.alibaba.chaosblade.box.common.common.enums.CommonErrorCode;
import com.alibaba.chaosblade.box.common.common.enums.ExperimentStateEnum;
import com.alibaba.chaosblade.box.common.common.enums.ExperimentTaskTypeEnum;
import com.alibaba.chaosblade.box.common.common.enums.StateEnum;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.request.ExperimentRunRequest;
import com.alibaba.chaosblade.box.dao.command.ExperimentExecutionCommand;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.interceptor.ExperimentTaskInterceptor;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.request.ExperimentTaskCreateRequest;
import com.alibaba.chaosblade.box.common.experiment.task.ExperimentTaskContext;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.ActivityTaskManager;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.ActivityTasksCreateRequest;
import com.alibaba.chaosblade.box.dao.model.ActivityTaskDO;
import com.alibaba.chaosblade.box.dao.model.ExperimentDO;
import com.alibaba.chaosblade.box.dao.model.ExperimentTaskDO;
import com.alibaba.chaosblade.box.dao.model.LoadTestStrategyDO;
import com.alibaba.chaosblade.box.dao.query.ExperimentTaskQuery;
import com.alibaba.chaosblade.box.dao.repository.ExperimentRepository;
import com.alibaba.chaosblade.box.dao.repository.ExperimentTaskRepository;
import com.alibaba.chaosblade.box.dao.repository.LoadTestStrategyRepository;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

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

    @Autowired
    private CommandBus commandBus;

    @Autowired
    private LoadTestStrategyRepository loadTestStrategyRepository;

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

            // 记录第一个活动任务的信息
            ActivityTaskDO firstActivityTask = activityTaskDOS.get(0);
            experimentTaskDO.setActivityTaskId(firstActivityTask.getTaskId());
            logger.info("演练任务创建完成，设置第一个活动任务: experimentTaskId={}, firstActivityTaskId={}, phase={}, activityName={}",
                    experimentTaskDO.getTaskId(),
                    firstActivityTask.getTaskId(),
                    firstActivityTask.getPhase(),
                    firstActivityTask.getActivityName());
            //然后绑定到演练上
            ExperimentDO updateExperimentDO = new ExperimentDO();
            updateExperimentDO.setExperimentId(experimentId);
            updateExperimentDO.setExperimentTaskId(experimentTaskDO.getTaskId());
            updateExperimentDO.setState(ExperimentStateEnum.RUNNING.getValue());
            updateExperimentDO.setResult(null);
            experimentRepository.update(updateExperimentDO);

            // 自动触发演练执行
            autoTriggerExperimentExecution(experimentTaskCreateRequest, experimentTaskDO);

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

    private void autoTriggerExperimentExecution(ExperimentTaskCreateRequest experimentTaskCreateRequest,
                                                ExperimentTaskDO experimentTaskDO) {
        try {
            String experimentId = experimentTaskCreateRequest.getExperimentDO().getExperimentId();

            // 检查是否有压测策略
            Optional<LoadTestStrategyDO> strategyOptional = loadTestStrategyRepository.findByExperimentId(experimentId);

            if (strategyOptional.isPresent() && strategyOptional.get().getEnable().equals(1)) {
                LoadTestStrategyDO strategy = strategyOptional.get();
                int startBeforeFaultSec = strategy.getStartBeforeFaultSec();

                if (startBeforeFaultSec > 0) {
                    logger.info("检测到压测策略，故障注入将由延迟调度器执行，跳过立即触发: experimentId={}, delaySeconds={}",
                            experimentId, startBeforeFaultSec);
                    return; // 有压测策略且需要延迟，不立即触发
                }
            }

            // 没有压测策略或不需要延迟，立即触发演练执行
            ExperimentRunRequest experimentRunRequest = new ExperimentRunRequest();
            experimentRunRequest.setExperimentId(experimentId);
            experimentRunRequest.setUser(experimentTaskCreateRequest.getUser());
            experimentRunRequest.setSync(false); // 异步执行
            experimentRunRequest.setNamespace(experimentTaskCreateRequest.getNamespace());

            logger.info("自动触发演练执行（无压测策略或无延迟）: experimentId={}, experimentTaskId={}",
                    experimentId, experimentTaskDO.getTaskId());

            // 异步执行演练，避免阻塞演练任务创建
            new Thread(() -> {
                try {
                    // 稍微延迟一下，确保演练任务创建完全完成
                    Thread.sleep(1000);
                    commandBus.syncRun(ExperimentExecutionCommand.class, experimentRunRequest);
                } catch (Exception e) {
                    logger.error("自动触发演练执行失败: experimentId={}", experimentId, e);
                }
            }).start();

        } catch (Exception e) {
            logger.error("自动触发演练执行失败", e);
        }
    }
}
