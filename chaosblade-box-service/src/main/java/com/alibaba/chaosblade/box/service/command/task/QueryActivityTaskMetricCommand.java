package com.alibaba.chaosblade.box.service.command.task;

import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.common.common.domain.activity.BaseActivityTaskRequest;
import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import com.alibaba.chaosblade.box.common.common.util.MiniAppUtils;
import com.alibaba.chaosblade.box.common.infrastructure.metric.ChaosMetricEntity;
import com.alibaba.chaosblade.box.dao.command.task.ActivityTaskExecutionCommand;
import com.alibaba.chaosblade.box.dao.infrastructure.manager.MiniAppTaskManager;
import com.alibaba.chaosblade.box.common.commands.SpringBeanCommand;
import com.alibaba.chaosblade.box.common.experiment.task.flow.ActivityTaskExecutionAttributes;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.ExperimentTaskRunnableSettings;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.activity.ExperimentExecuteContext;
import com.alibaba.chaosblade.box.common.infrastructure.domain.app.MetricResultEntity;
import com.alibaba.chaosblade.box.common.infrastructure.util.ChaosTraceUtil;
import com.alibaba.chaosblade.box.dao.model.ActivityTaskDO;
import com.alibaba.chaosblade.box.dao.repository.ActivityTaskRepository;
import com.alibaba.chaosblade.box.service.model.param.RequestHit;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * @author haibin
 *
 *
 */
@Component
public class QueryActivityTaskMetricCommand
    extends SpringBeanCommand<BaseActivityTaskRequest, Response<List<MetricResultEntity>>> {

    @Autowired
    private ActivityTaskRepository activityTaskRepository;

    @Autowired
    private MiniAppTaskManager miniAppTaskManager;

    @Override
    public Response<List<MetricResultEntity>> execute(BaseActivityTaskRequest baseActivityTaskRequest) {
        ChaosUser user = baseActivityTaskRequest.getUser();
        String activityTaskId = baseActivityTaskRequest.getActivityTaskId();
        ActivityTaskDO activityTaskDO = activityTaskRepository.findById(activityTaskId).orElse(null);
        if (activityTaskDO == null) {
            return Response.okWithData(new ArrayList<>());
        }
        //如果不是metric类型的小程序，就直接返回空
        if (!MiniAppUtils.isMetricMiniApp(activityTaskDO.getExecutableAppCode())) {
            return Response.okWithData(new ArrayList<>());
        }
        ExperimentExecuteContext experimentExecuteContext = new ExperimentExecuteContext(ChaosTraceUtil.generateTraceId(),
            baseActivityTaskRequest.getUser(),
            activityTaskDO, new ExperimentTaskRunnableSettings());
        experimentExecuteContext.getContextData().add(ActivityTaskExecutionAttributes.ATTRIBUTE_KEY_ACTIVITY_TASK_DO,
            activityTaskDO);
        experimentExecuteContext.getContextData().setUser(user);
        experimentExecuteContext.getContextData().add(ActivityTaskExecutionAttributes.ATTRIBUTE_KEY_METRIC_RELOAD,
            ActivityTaskExecutionAttributes.ATTRIBUTE_VALUE_TRUE);
        experimentExecuteContext.getContextData().add(
            ActivityTaskExecutionAttributes.ATTRIBUTE_ACTIVITY_SUPPORT_REPERTITION_EXECUTION,
            ActivityTaskExecutionAttributes.ATTRIBUTE_VALUE_TRUE);
        experimentExecuteContext.getContextData().add(
            ActivityTaskExecutionAttributes.ATTRIBUTE_KEY_PUSH_EXPERIMENT_TASK,
            ActivityTaskExecutionAttributes.ATTRIBUTE_VALUE_FALSE);
        commandBus.syncRun(new ActivityTaskExecutionCommand(experimentExecuteContext));
        String appCode = activityTaskDO.getAppCode();
        List<MetricResultEntity> entities = new ArrayList<>();
        return Response.okWithData(entities);
    }

    private Stream<MetricResultEntity> getMetricStream(String data) {
        Map<String, List<ChaosMetricEntity>> result = JSON.parseObject(data,
            new TypeReference<Map<String, List<ChaosMetricEntity>>>() {});
        if (result == null) {return Stream.empty();}
        return result.getOrDefault("response", new ArrayList<>())
            .stream().map(new Function<ChaosMetricEntity, MetricResultEntity>() {
                @Override
                public MetricResultEntity apply(ChaosMetricEntity chaosMetricEntity) {
                    MetricResultEntity metricResultEntity = new MetricResultEntity();
                    metricResultEntity.setHost(chaosMetricEntity.getHost().getIp());
                    metricResultEntity.setName(chaosMetricEntity.getMetric());
                    metricResultEntity.setTimestamp(chaosMetricEntity.getTimestamp());
                    metricResultEntity.setValue(chaosMetricEntity.getValue());
                    metricResultEntity.setUnit(chaosMetricEntity.getUnit());
                    return metricResultEntity;
                }
            });
    }

    private Stream<MetricResultEntity> getHitCountStream(String data) {
        Map<String, List<RequestHit>> result = JSON.parseObject(data,
            new TypeReference<Map<String, List<RequestHit>>>() {});
        return result.getOrDefault("response", new ArrayList<>())
            .stream().map(new Function<RequestHit, MetricResultEntity>() {
                @Override
                public MetricResultEntity apply(RequestHit requestHit) {
                    MetricResultEntity metricResultEntity = new MetricResultEntity();
                    metricResultEntity.setHost(requestHit.getHost());
                    metricResultEntity.setName(requestHit.getName());
                    metricResultEntity.setValue(requestHit.getValue());
                    return metricResultEntity;
                }
            });
    }

}
