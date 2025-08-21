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
import com.alibaba.chaosblade.box.dao.model.LoadTestStrategyDO;
import com.alibaba.chaosblade.box.dao.repository.ActivityTaskRepository;
import com.alibaba.chaosblade.box.dao.repository.ExperimentTaskRepository;
import com.alibaba.chaosblade.box.dao.repository.LoadTestStrategyRepository;
import com.alibaba.chaosblade.box.dao.command.task.ActivityTaskExecutionCommand;
// 已移除 Quartz 调度器相关导入
import com.alibaba.chaosblade.box.common.commands.CommandBus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author haibin
 *
 *
 */
@Slf4j
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

    @Autowired
    private LoadTestStrategyRepository loadTestStrategyRepository;

    // 已移除 SchedulerJobService，使用简单定时器

    // 用于延迟执行故障注入的定时器
    private static final ScheduledExecutorService delayedExecutor = Executors.newScheduledThreadPool(5);

    @Autowired
    private ExperimentTaskRepository experimentTaskRepository;

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

        // 检查是否已有运行中的演练任务
        ExperimentTaskDO existingTask = findRunningExperimentTask(experimentId);

        if (existingTask != null) {
            // 如果已有运行中的任务，直接执行故障注入
            log.info("发现已有运行中的演练任务，直接执行故障注入: experimentId={}, taskId={}",
                    experimentId, existingTask.getTaskId());

            ActivityTaskDO activityTaskDO = activityTaskRepository.findById(existingTask.getActivityTaskId())
                    .orElse(null);

            if (activityTaskDO != null) {
                log.info("获取到活动任务: activityTaskId={}, phase={}, activityName={}, state={}, result={}",
                        activityTaskDO.getTaskId(),
                        activityTaskDO.getPhase(),
                        activityTaskDO.getActivityName(),
                        activityTaskDO.getState(),
                        activityTaskDO.getResult());
            } else {
                log.warn("未找到活动任务: activityTaskId={}", existingTask.getActivityTaskId());
            }

            runTask(experimentRunRequest, user, activityTaskDO, experimentDO.getExperimentId());

            ExperimentRunResult experimentRunResult = new ExperimentRunResult();
            experimentRunResult.setTaskId(existingTask.getTaskId());
            return Response.okWithData(experimentRunResult);
        } else {
            // 没有运行中的任务，创建新的演练任务
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
            runTask(experimentRunRequest, user, activityTaskDO, experimentDO.getExperimentId());
            ExperimentRunResult experimentRunResult = new ExperimentRunResult();
            experimentRunResult.setTaskId(experimentTaskId);
            sendNotification(experimentRunRequest, experimentId, user, createResponse, experimentTaskId);
            return Response.okWithData(experimentRunResult);
        }
    }

    private void sendNotification(ExperimentRunRequest experimentRunRequest, String experimentId, ChaosUser user,
                                  Response<ExperimentTaskDO> createResponse, String experimentTaskId) {
        chaosEventDispatcher.fireEvent(new ExperimentTaskStartedEvent(user, createResponse.getResult()));
    }

    private void runTask(ExperimentRunRequest experimentRunRequest, ChaosUser user, ActivityTaskDO activityTaskDO, String experimentId) {
        log.info("开始执行runTask: experimentId={}, activityTaskId={}, userId={}",
                experimentId, activityTaskDO != null ? activityTaskDO.getTaskId() : "null", user.getUserId());

        // 检查是否有压测策略需要延迟执行
        Optional<LoadTestStrategyDO> strategyOptional = loadTestStrategyRepository.findByExperimentId(experimentId);

        if (strategyOptional.isPresent() && strategyOptional.get().getEnable().equals(1)) {
            LoadTestStrategyDO strategy = strategyOptional.get();
            int startBeforeFaultSec = strategy.getStartBeforeFaultSec();

            if (startBeforeFaultSec > 0) {
                log.info("检测到压测策略，故障注入将延迟 {} 秒执行，演练ID: {}", startBeforeFaultSec, experimentId);
                scheduleDelayedExperimentTaskWithTimer(experimentRunRequest, user, activityTaskDO, startBeforeFaultSec);
                return;
            }
        }

        // 没有压测策略或不需要延迟，立即执行
        executeTask(experimentRunRequest, user, activityTaskDO);
    }

    private void executeTask(ExperimentRunRequest experimentRunRequest, ChaosUser user, ActivityTaskDO activityTaskDO) {
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

    /**
     * 使用简单定时器延迟执行故障注入任务
     */
    private void scheduleDelayedExperimentTaskWithTimer(ExperimentRunRequest experimentRunRequest, ChaosUser user,
                                                        ActivityTaskDO activityTaskDO, int delaySec) {
        log.info("使用定时器创建延迟任务: activityTaskId={}, delaySec={}, 预计执行时间={}",
                activityTaskDO.getTaskId(), delaySec, new Date(System.currentTimeMillis() + delaySec * 1000L));

        // 使用定时器延迟执行
        delayedExecutor.schedule(() -> {
            try {
                log.info("定时器触发，开始执行延迟的故障注入: activityTaskId={}", activityTaskDO.getTaskId());

                // 重新查询活动任务，确保状态是最新的
                Optional<ActivityTaskDO> activityTaskOptional = activityTaskRepository.findById(activityTaskDO.getTaskId());
                if (!activityTaskOptional.isPresent()) {
                    log.warn("活动任务不存在: activityTaskId={}", activityTaskDO.getTaskId());
                    return;
                }

                ActivityTaskDO currentActivityTask = activityTaskOptional.get();
                log.info("延迟执行获取到活动任务: activityTaskId={}, phase={}, activityName={}, state={}, result={}",
                        currentActivityTask.getTaskId(),
                        currentActivityTask.getPhase(),
                        currentActivityTask.getActivityName(),
                        currentActivityTask.getState(),
                        currentActivityTask.getResult());

                // 创建演练执行上下文
                ExperimentExecuteContext experimentExecuteContext = new ExperimentExecuteContext(
                        ChaosTraceUtil.generateTraceId(),
                        user,
                        currentActivityTask,
                        new ExperimentTaskRunnableSettings()
                );

                // 执行活动任务
                log.info("开始执行故障注入: activityTaskId={}", currentActivityTask.getTaskId());

                if (experimentRunRequest.isSync()) {
                    commandBus.syncRun(new ActivityTaskExecutionCommand(experimentExecuteContext));
                } else {
                    commandBus.asyncRun(new ActivityTaskExecutionCommand(experimentExecuteContext));
                }

                log.info("延迟故障注入任务执行完成: activityTaskId={}", currentActivityTask.getTaskId());

            } catch (Exception e) {
                log.error("延迟故障注入任务执行失败: activityTaskId={}", activityTaskDO.getTaskId(), e);
            }
        }, delaySec, TimeUnit.SECONDS);

        log.info("定时器延迟任务创建成功: activityTaskId={}, 将在{}秒后执行", activityTaskDO.getTaskId(), delaySec);
    }

    // 旧的 Quartz 调度器方法已删除，现在使用简单定时器

    private ExperimentTaskDO findRunningExperimentTask(String experimentId) {
        try {
            // 查找最新的未完成演练任务
            return experimentTaskRepository.findLatestUnfinishedExperimentTask(experimentId);
        } catch (Exception e) {
            log.warn("查找运行中的演练任务失败: experimentId={}", experimentId, e);
            return null;
        }
    }

}
