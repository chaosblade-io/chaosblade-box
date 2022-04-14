package com.alibaba.chaosblade.box.dao.infrastructure.experiment.task;

import com.alibaba.chaosblade.box.common.common.domain.task.BaseExperimentTask;
import com.alibaba.chaosblade.box.common.common.domain.task.ExperimentTaskExtInfo;
import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import com.alibaba.chaosblade.box.common.experiment.clientobject.ExperimentTaskStat;
import com.alibaba.chaosblade.box.dao.model.ActivityTaskDO;
import com.alibaba.chaosblade.box.dao.model.ExperimentDO;
import com.alibaba.chaosblade.box.dao.model.ExperimentTaskDO;
import com.alibaba.chaosblade.box.dao.repository.ActivityTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author haibin
 *
 *
 */
@Component
public class ExperimentTaskUtil {

    @Autowired
    private ActivityTaskRepository activityTaskRepository;
    
    public long calculateProgressPercent( List<ActivityTaskDO> activityTaskDOS) {
        if (activityTaskDOS == null||activityTaskDOS.isEmpty()) { return 0; }
        int activitySize=activityTaskDOS.size();
        Set<String> finishedOrRunningActivityIds = activityTaskDOS.stream().map(
            activityTaskDO -> {
                if (activityTaskDO.isReady()) { return null; }
                return activityTaskDO.getActivityId();
            }).filter(Objects::nonNull).collect(Collectors.toSet());
        return Math.round(((double)finishedOrRunningActivityIds.size() / (double)activitySize) * 100);
    }
    
    
    public void fillBaseInfo(BaseExperimentTask baseExperimentTask, ExperimentTaskDO experimentTaskDO,
                             ExperimentDO experimentDO) {
        baseExperimentTask.setExperimentName(experimentTaskDO.getName());
        if (baseExperimentTask.getExperimentName() == null && experimentDO != null) {
            baseExperimentTask.setExperimentName(experimentDO.getName());
        }
        baseExperimentTask.setExperimentId(experimentTaskDO.getExperimentId());
        baseExperimentTask.setTaskId(experimentTaskDO.getTaskId());
        baseExperimentTask.setState(experimentTaskDO.getStateEnum());
        baseExperimentTask.setResult(experimentTaskDO.getResultEnum());
        baseExperimentTask.setStartTime(experimentTaskDO.getGmtCreate());
        baseExperimentTask.setEndTime(experimentTaskDO.getGmtEnd());
        baseExperimentTask.setMessage(experimentTaskDO.getErrorMessage());
        baseExperimentTask.setFeedbackStatus(experimentTaskDO.getFeedBackStatus());
        baseExperimentTask.setCreator(new ChaosUser(experimentTaskDO.getUserId()));
        baseExperimentTask.setNamespace(experimentTaskDO.getNamespace());
        ExperimentTaskExtInfo experimentTaskExtInfo = new ExperimentTaskExtInfo();
        baseExperimentTask.setExtInfo(experimentTaskExtInfo);
        if (experimentTaskDO.getExperimentTaskContext() != null) {
            experimentTaskExtInfo.setSchedulerConfig(experimentTaskDO.getExperimentTaskContext().getSchedulerConfig());
            baseExperimentTask.setSource(experimentTaskDO.getExperimentTaskContext().getSource());
        }
        if (!experimentTaskDO.isFinished()) {
            activityTaskRepository.findById(experimentTaskDO.getActivityTaskId()).ifPresent(
                activityTaskDO -> baseExperimentTask.setCurrentPhase(activityTaskDO.getPhase()));
        }
    }

    /**
     * 演练任务统计
     *
     * @param experimentTaskDOS
     * @return
     */
    public ExperimentTaskStat doStatistic(List<ExperimentTaskDO> experimentTaskDOS) {
        ExperimentTaskStat experimentTaskStat = new ExperimentTaskStat();
        Integer runningCount = 0;
        Integer successCount = 0;
        Integer failureCount = 0;
        Integer exceptionCount = 0;

        //因为较早的数据没有记录endTime字段,所以这些就不参与到数据统计里面去了
        Integer countWithEndTime = 0;
        Long totalCost = 0L;
        for (ExperimentTaskDO experimentTaskDO : experimentTaskDOS) {
            switch (experimentTaskDO.getStateEnum()) {
                case FINISHED:
                    switch (experimentTaskDO.getResultEnum()) {
                        case SUCCESS:
                            successCount++;
                            break;
                        case ERROR:
                            exceptionCount++;
                            break;
                        default:
                            failureCount++;
                    }
                    break;
                default:
                    runningCount++;
            }
            if (experimentTaskDO.getGmtEnd() != null) {
                countWithEndTime++;
                totalCost += (experimentTaskDO.getGmtEnd().getTime() - experimentTaskDO.getGmtCreate().getTime());
            }
        }
        experimentTaskStat.setTotalCount(experimentTaskDOS.size());
        experimentTaskStat.setRunningCount(runningCount);
        experimentTaskStat.setFailureCount(failureCount);
        experimentTaskStat.setExceptionCount(exceptionCount);
        experimentTaskStat.setSuccessCount(successCount);
        if (countWithEndTime != 0) {
            experimentTaskStat.setAverageCostInSecond(TimeUnit.MILLISECONDS.toSeconds(totalCost / countWithEndTime));
        }
        return experimentTaskStat;
    }
}
