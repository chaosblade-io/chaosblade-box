package com.alibaba.chaosblade.box.dao.infrastructure.manager.impl;

import com.alibaba.chaosblade.box.common.app.sdk.scope.Host;
import com.alibaba.chaosblade.box.common.common.domain.activity.MiniAppTask;
import com.alibaba.chaosblade.box.common.common.domain.activity.MiniAppTaskSummary;
import com.alibaba.chaosblade.box.common.common.enums.ResultEnum;
import com.alibaba.chaosblade.box.common.common.enums.StateEnum;
import com.alibaba.chaosblade.box.common.common.util.MiniAppUtils;
import com.alibaba.chaosblade.box.common.infrastructure.ChaosRequestContextHolder;
import com.alibaba.chaosblade.box.dao.infrastructure.app.chaosblade.ChaosBladeResultCodeRepo;
import com.alibaba.chaosblade.box.dao.infrastructure.app.chaosblade.ChaosBladeSolutionUtil;
import com.alibaba.chaosblade.box.dao.infrastructure.manager.MiniAppTaskManager;
import com.alibaba.chaosblade.box.dao.model.ActivityTaskDO;
import com.alibaba.chaosblade.box.dao.model.ChaosBladeExpUidDO;
import com.alibaba.chaosblade.box.dao.model.ExperimentMiniAppTaskDO;
import com.alibaba.chaosblade.box.dao.query.ActivityTargetExecutionResultQuery;
import com.alibaba.chaosblade.box.dao.repository.ActivityTargetExecutionResultRepository;
import com.alibaba.chaosblade.box.dao.repository.ChaosBladeExpUidRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author haibin
 *
 *
 */
@Component
@Slf4j
public class MiniAppTaskManagerImpl implements MiniAppTaskManager {

    private static final String ORIGIN_REQUEST = "origin_request";
    private static final String ORIGIN_RESPONSE = "origin_response";
    @Autowired
    private ActivityTargetExecutionResultRepository activityTargetExecutionResultRepository;

    @Autowired
    private ChaosBladeExpUidRepository chaosBladeExpUidRepository;

    @Override
    public ExperimentMiniAppTaskDO findOrCreateMiniAppTaskIfNotExist(ActivityTaskDO activityTaskDO,
                                                                     Host host) {
        ActivityTargetExecutionResultQuery activityTargetExecutionResultQuery
            = new ActivityTargetExecutionResultQuery();
        activityTargetExecutionResultQuery.setActivityTaskId(activityTaskDO.getTaskId());
        String hostIp = null;
        if (host != null) {
            hostIp = host.getIp();
        }
        activityTargetExecutionResultQuery.setHostIp(hostIp);
        activityTargetExecutionResultQuery.setAppCode(activityTaskDO.getAppCode());
        ExperimentMiniAppTaskDO experimentMiniAppTaskDO = activityTargetExecutionResultRepository.findOne(
            activityTargetExecutionResultQuery);
        if (experimentMiniAppTaskDO == null) {
            return createMiniAppTask(activityTaskDO, host);
        }
        return experimentMiniAppTaskDO;
    }

    private ExperimentMiniAppTaskDO createMiniAppTask(ActivityTaskDO activityTaskDO,
        Host host) {
        ExperimentMiniAppTaskDO experimentMiniAppTaskDO = initExperimentMiniAppTaskDO(activityTaskDO, host);
        activityTargetExecutionResultRepository.add(experimentMiniAppTaskDO);
        return experimentMiniAppTaskDO;
    }

    /**
     * 放弃小程序执行
     *
     * @param experimentMiniAppTaskDO
     * @param reason
     * @return
     */
    private void rejectMiniAppTask(ExperimentMiniAppTaskDO experimentMiniAppTaskDO, String reason) {
        experimentMiniAppTaskDO.setResult(ResultEnum.REJECTED.getValue());
        experimentMiniAppTaskDO.setState(StateEnum.FINISHED.getValue());
        experimentMiniAppTaskDO.setGmtEnd(new Date());
        experimentMiniAppTaskDO.setErrorMessage(reason);
        activityTargetExecutionResultRepository.update(experimentMiniAppTaskDO);
    }

    @Override
    public long rejectMiniAppTasksByActivityTaskId(String activityTaskId, String rejectReason) {
        return activityTargetExecutionResultRepository.findByActivityTaskId(activityTaskId).stream().filter(
                miniAppExecutionRecordDO -> !miniAppExecutionRecordDO.isFinished()).peek(
                miniAppExecutionRecordDO -> rejectMiniAppTask(miniAppExecutionRecordDO, rejectReason))
            .count();
    }

    @Override
    public List<MiniAppTaskSummary> findMiniAppTasksByActivityTaskId(String activityTaskId) {
        return activityTargetExecutionResultRepository.findByActivityTaskIdReturnSummary(activityTaskId)
            .stream()
            .map(
                this::convertMiniAppTaskSummary).collect(Collectors.toList());
    }

    @Override
    public MiniAppTask findMiniAppTask(String miniAppTaskId) {
        return activityTargetExecutionResultRepository.findById(miniAppTaskId).map(
            this::convert).orElse(null);
    }

    @Override
    public void resetMiniAppTasksByActivityTask(String activityTaskId) {
        activityTargetExecutionResultRepository.updateMiniAppTasksByActivityTaskId(activityTaskId);
    }

    private MiniAppTaskSummary convertMiniAppTaskSummary(ExperimentMiniAppTaskDO experimentMiniAppTaskDO) {
        MiniAppTaskSummary miniAppTaskSummary = new MiniAppTaskSummary();
        miniAppTaskSummary.setEndTime(experimentMiniAppTaskDO.getGmtEnd());
        miniAppTaskSummary.setHostIp(experimentMiniAppTaskDO.getHostIp());
        miniAppTaskSummary.setTaskId(experimentMiniAppTaskDO.getTaskId());
        miniAppTaskSummary.setState(experimentMiniAppTaskDO.getStateEnum());
        miniAppTaskSummary.setResult(experimentMiniAppTaskDO.getResultEnum());
        miniAppTaskSummary.setDeviceName(experimentMiniAppTaskDO.getDeviceName());
        Date startTime = experimentMiniAppTaskDO.getStartTime() == null ? experimentMiniAppTaskDO
            .getGmtCreate()
            : experimentMiniAppTaskDO.getStartTime();
        miniAppTaskSummary.setStartTime(startTime);
        miniAppTaskSummary.setAppConfigurationId(experimentMiniAppTaskDO.getAppConfigurationId());
        return miniAppTaskSummary;
    }

    private MiniAppTask convert(ExperimentMiniAppTaskDO experimentMiniAppTaskDO) {
        MiniAppTask miniAppTask = new MiniAppTask();
        miniAppTask.setData(experimentMiniAppTaskDO.getData());
        miniAppTask.setErrorMessage(experimentMiniAppTaskDO.getErrorMessage());
        miniAppTask.setEndTime(experimentMiniAppTaskDO.getGmtEnd());
        miniAppTask.setHostIp(experimentMiniAppTaskDO.getHostIp());
        miniAppTask.setTaskId(experimentMiniAppTaskDO.getTaskId());
        miniAppTask.setState(experimentMiniAppTaskDO.getStateEnum());
        miniAppTask.setResult(experimentMiniAppTaskDO.getResultEnum());
        miniAppTask.setExpId(experimentMiniAppTaskDO.getChaosBladeExpUid());
        miniAppTask.setDeviceName(experimentMiniAppTaskDO.getDeviceName());

        Date startTime = experimentMiniAppTaskDO.getStartTime() == null ? experimentMiniAppTaskDO
            .getGmtCreate()
            : experimentMiniAppTaskDO.getStartTime();
        miniAppTask.setStartTime(startTime);
        if (MiniAppUtils.isFromChaosBlade(experimentMiniAppTaskDO.getAppCode())) {
            ChaosBladeExpUidDO chaosBladeExpUidDO = chaosBladeExpUidRepository.findByActivityTargetTaskId(
                experimentMiniAppTaskDO.getTaskId());
            if (chaosBladeExpUidDO != null) {
                miniAppTask.setExpId(chaosBladeExpUidDO.getExpUid());
            }
        }
        ChaosBladeResultCodeRepo.getByErrorCode(experimentMiniAppTaskDO.getErrorCode()).ifPresent(
            codeMetaData -> miniAppTask.setSolution(codeMetaData.getSolution()));
        ChaosBladeResultCodeRepo.getByCode(experimentMiniAppTaskDO.getAgentCode()).ifPresent(
            codeMetaData -> miniAppTask.setSolution(ChaosBladeSolutionUtil
                .parseSolution(codeMetaData.getSolution(), experimentMiniAppTaskDO.getErrorMessage())));
        ChaosRequestContextHolder.getLoginUser().ifPresent(chaosUser -> {
            miniAppTask.getExtraInfo().put(ORIGIN_REQUEST, experimentMiniAppTaskDO.getOriginRequest());
            miniAppTask.getExtraInfo().put(ORIGIN_RESPONSE, experimentMiniAppTaskDO.getOriginResponse());
        });
        return miniAppTask;
    }

}
