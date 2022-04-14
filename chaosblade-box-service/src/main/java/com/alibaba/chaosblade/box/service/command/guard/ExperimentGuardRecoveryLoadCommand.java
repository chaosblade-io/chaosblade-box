package com.alibaba.chaosblade.box.service.command.guard;

import com.alibaba.chaosblade.box.common.app.sdk.scope.Host;
import com.alibaba.chaosblade.box.common.app.sdk.scope.Scope;
import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import com.alibaba.chaosblade.box.common.experiment.task.flow.util.Pair;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.SceneArgumentDefinition;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.flow.RecoveryStrategyFieldArgumentDefinition;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.flow.RecoveryStrategyToleranceArgumentDefinition;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.flow.RecoveryStrategyToleranceArgumentDefinitionRepo;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.guard.ExperimentGuardArgument;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.guard.GuardRunState;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.request.ExperimentTaskStopRequest;
import com.alibaba.chaosblade.box.common.infrastructure.metric.ChaosMetricEntity;
import com.alibaba.chaosblade.box.common.infrastructure.util.GuardMatcher;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.guard.ExperimentGuardResultLoadRequest;
import com.alibaba.chaosblade.box.dao.model.ExperimentGuardInstanceDO;
import com.alibaba.chaosblade.box.dao.model.ExperimentTaskDO;
import com.alibaba.chaosblade.box.dao.command.task.ExperimentTaskStopCommand;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 自动保护策略数据加载
 *
 * @author haibin
 *
 *
 */
@Component
@Slf4j
public class ExperimentGuardRecoveryLoadCommand
    extends BaseExperimentGuardLoadCommand<Response> implements GuardExecutableInterface {

    private static Integer DEFAULT_DURATION_SECONDS = 30;

    public static String TRIGGER_REASON_TEMPLATE = "触发保护策略,动作:中断演练,指标:";

    @Override
    public Response internalExecute(
        ExperimentGuardResultLoadRequest experimentGuardResultLoadRequest) {
        //获取多个值，只要一个不满足就有问题
        ExperimentGuardInstanceDO experimentGuardInstanceDO = experimentGuardResultLoadRequest
            .getExperimentGuardInstanceDO();
        ExperimentGuardArgument argument = experimentGuardInstanceDO.getArgument();
        List<RecoveryStrategyFieldArgumentDefinition> fieldArgumentDefinitions = argument.getFields();
        List<RecoveryStrategyToleranceArgumentDefinition> tolerance = argument.getTolerance();
        Map<String, RecoveryStrategyToleranceArgumentDefinition> aliasToDefinitions = groupToleranceByName(tolerance);
        Map<String, List<RecoveryStrategyFieldArgumentDefinition>> metricToRecoveryStrategyFieldArgumentDefinition
            = groupByMetric(fieldArgumentDefinitions);
        String triggerMetric = null;
        //对每一个指标做判断，只要有一个指标触发了就终止演练
        for (Map.Entry<String, List<RecoveryStrategyFieldArgumentDefinition>> stringListEntry :
            metricToRecoveryStrategyFieldArgumentDefinition
                .entrySet()) {
            //某个指标的字段是否满足要求
            if (isTriggeredByField(experimentGuardResultLoadRequest.getExperimentTaskDO(), stringListEntry.getKey(),
                stringListEntry.getValue(), aliasToDefinitions,
                experimentGuardInstanceDO, experimentGuardResultLoadRequest.getHosts())) {
                triggerMetric = stringListEntry.getKey();
                break;
            }
        }
        if (triggerMetric != null) {
            experimentGuardInstanceDO.setState(GuardRunState.TRIGGERED);
            fireExperimentTaskStop(experimentGuardResultLoadRequest.getExperimentTaskDO());
            experimentGuardInstanceDO.setTriggeredReason(TRIGGER_REASON_TEMPLATE + triggerMetric);
            log.info("Fire experiment task stop,taskId:{},reason:{}",
                experimentGuardResultLoadRequest.getExperimentTaskDO().getTaskId(),
                experimentGuardInstanceDO.getTriggeredReason());
            experimentGuardInstanceRepository.update(experimentGuardInstanceDO);
        }
        return Response.ok();
    }

    private Map<String, List<RecoveryStrategyFieldArgumentDefinition>> groupByMetric(
        List<RecoveryStrategyFieldArgumentDefinition> fieldArgumentDefinitions) {
        return fieldArgumentDefinitions.stream().collect(
            Collectors.groupingBy(RecoveryStrategyFieldArgumentDefinition::getAlias));
    }

    private boolean isTriggeredByField(ExperimentTaskDO experimentTaskDO,
                                       String metric, List<RecoveryStrategyFieldArgumentDefinition> fieldArgumentDefinitions,
                                       Map<String, RecoveryStrategyToleranceArgumentDefinition> aliasToDefinitions,
                                       ExperimentGuardInstanceDO experimentGuardInstanceDO, List<Host> hosts) {
        //首先获取指标的值
        //合并用户配置的小程序参数值
        List<SceneArgumentDefinition> sceneArgumentDefinitions = new ArrayList<>(
            experimentGuardInstanceDO.getArgument().getArguments() != null ? experimentGuardInstanceDO.getArgument()
                .getArguments() : new ArrayList<>());
        //设置小程序的key
        SceneArgumentDefinition sceneArgumentDefinition = new SceneArgumentDefinition();
        sceneArgumentDefinition.setAlias("metric");
        sceneArgumentDefinition.setValue(metric);
        sceneArgumentDefinitions.add(sceneArgumentDefinition);
        //获取持续时间
        RecoveryStrategyToleranceArgumentDefinition recoveryStrategyToleranceArgumentDefinition = aliasToDefinitions
            .get(RecoveryStrategyToleranceArgumentDefinitionRepo.DURATION);
        if (recoveryStrategyToleranceArgumentDefinition == null || Strings.isNullOrEmpty(
            recoveryStrategyToleranceArgumentDefinition.getValue())) { return false; }
        Integer durationTimeInSeconds = DEFAULT_DURATION_SECONDS;
        durationTimeInSeconds = Integer.parseInt(recoveryStrategyToleranceArgumentDefinition.getValue());
        Pair<Date, Date> dateRange = calcTimeRange(durationTimeInSeconds);
        //获取得到所有值
        Map<Scope, List<ChaosMetricEntity>> metricValues = invokeMetricMiniApp(experimentTaskDO,
            experimentGuardInstanceDO.getAppCode(), experimentGuardInstanceDO,
            sceneArgumentDefinitions, dateRange.getLeft(), dateRange.getRight(), hosts);
        if (metricValues.isEmpty()) { return false; }
        int mactherCount = 0;
        for (Map.Entry<Scope, List<ChaosMetricEntity>> scopeListEntry : metricValues.entrySet()) {
            boolean isMatched = judgeMetricMatcher(scopeListEntry.getValue(), fieldArgumentDefinitions);
            if (isMatched) {
                mactherCount++;
            }
        }
        int percent = getPercent(aliasToDefinitions.get(RecoveryStrategyToleranceArgumentDefinitionRepo.PERCENT));
        return calPercent(mactherCount, hosts.size()) >= percent;
    }

    /**
     * 获取用户定义的百分比指标
     *
     * @param recoveryStrategyToleranceArgumentDefinition
     * @return
     */
    private int getPercent(RecoveryStrategyToleranceArgumentDefinition recoveryStrategyToleranceArgumentDefinition) {
        int percent = 100;
        if (recoveryStrategyToleranceArgumentDefinition != null) {
            percent = Integer.parseInt(recoveryStrategyToleranceArgumentDefinition.getValue());
            if (percent < 0 || percent > 100) {
                percent = 100;
            }
        }
        return percent;
    }

    /**
     * 判断当前指标是否满足要求
     *
     * @param values
     * @param fieldArgumentDefinitions
     * @return true表示命中恢复策略, false表示未命中
     */
    private boolean judgeMetricMatcher(List<ChaosMetricEntity> values,
        List<RecoveryStrategyFieldArgumentDefinition> fieldArgumentDefinitions) {
        if (values.isEmpty()) { return false; }
        return values.stream().allMatch(new Predicate<ChaosMetricEntity>() {
            @Override
            public boolean test(ChaosMetricEntity chaosMetricEntity) {
                Number value = chaosMetricEntity.getValue();
                try {
                    return GuardMatcher.match(value, fieldArgumentDefinitions);
                } catch (Exception ex) {
                    log.error("GuardMatcher parser failed", ex);
                    return false;
                }
            }
        });
    }

    /**
     * 按照恢复指标进行分组
     *
     * @param tolerance
     * @return
     */
    private Map<String, RecoveryStrategyToleranceArgumentDefinition> groupToleranceByName(
        List<RecoveryStrategyToleranceArgumentDefinition> tolerance) {
        return tolerance.stream().collect(Collectors.toMap(
            SceneArgumentDefinition::getAlias,
            recoveryStrategyToleranceArgumentDefinition -> recoveryStrategyToleranceArgumentDefinition));
    }

    /**
     * 停止演练任务
     *
     * @param experimentTaskDO
     */
    private void fireExperimentTaskStop(ExperimentTaskDO experimentTaskDO) {
        ChaosUser user = new ChaosUser(experimentTaskDO.getUserId());
        ExperimentTaskStopRequest experimentTaskStopRequest = new ExperimentTaskStopRequest();
        experimentTaskStopRequest.setTaskId(experimentTaskDO.getTaskId());
        experimentTaskStopRequest.setUser(user);
        commandBus.syncRun(ExperimentTaskStopCommand.class, experimentTaskStopRequest);
    }

    /**
     * 计算百分比
     *
     * @param count
     * @param denominator
     * @return
     */
    private float calPercent(int count, int
        denominator) {
        if (denominator == 0) { return 0f; }
        return ((float)count / denominator) * 100;
    }

}
