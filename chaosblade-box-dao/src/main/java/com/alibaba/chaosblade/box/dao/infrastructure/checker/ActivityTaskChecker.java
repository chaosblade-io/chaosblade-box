package com.alibaba.chaosblade.box.dao.infrastructure.checker;

import com.alibaba.chaosblade.box.common.app.sdk.constants.PhaseType;
import com.alibaba.chaosblade.box.common.common.domain.ChaosError;
import com.alibaba.chaosblade.box.common.common.domain.IErrorCode;
import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.common.common.enums.CommonErrorCode;
import com.alibaba.chaosblade.box.common.common.enums.UserCheckState;
import com.alibaba.chaosblade.box.dao.model.ActivityTaskDO;
import com.alibaba.chaosblade.box.dao.model.ExperimentTaskDO;
import com.alibaba.chaosblade.box.dao.repository.ActivityTaskRepository;
import com.alibaba.chaosblade.box.dao.repository.ExperimentTaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.Optional;

/**
 * @author haibin
 *
 *
 */
@Component
@Slf4j
public class ActivityTaskChecker {

    @Resource
    private ActivityTaskRepository activityTaskRepository;

    @Resource
    private ExperimentTaskRepository experimentTaskRepository;

    public Response<ActivityTaskDO> checkActivityTaskExist(String activityTaskId) {
        Optional<ActivityTaskDO> activityTaskDOOptional = activityTaskRepository.findById(activityTaskId);
        return activityTaskDOOptional.map(Response::okWithData)
            .orElseGet(() -> Response.failedWith(CommonErrorCode.P_ACTIVITY_TASK_NOT_FOUND));
    }

    /**
     * 判断一个演练是否能重试
     *
     * @param activityTaskDO
     * @return
     */
    public IErrorCode isActivityRetryable(ExperimentTaskDO experimentTaskDO, ActivityTaskDO activityTaskDO) {
        //运行成功，不允许重试
        if (activityTaskDO.isSuccess()) {
            return CommonErrorCode.P_ACTIVITY_TASK_STATUS_ILLEGAL;
        }
        //如果还没有完结，那么不允许
        if (!activityTaskDO.isFinished()) {
            return CommonErrorCode.B_ATTACK_ACTIVITY_NOT_FINISHED;
        }
        //恢复阶段的话，就算演练结束了,也可以可以重试
        if (!activityTaskDO.inPhase(PhaseType.RECOVER)) {
            if (experimentTaskDO.isFinished()) {
                return CommonErrorCode.B_EXPERIMENT_TASK_ALREADY_FINISHED;
            }
            if (!Objects.equals(experimentTaskDO.getActivityTaskId(), activityTaskDO.getTaskId())) {
                return CommonErrorCode.P_ACTIVITY_TASK_STATUS_ILLEGAL;
            }
        }
        return null;
    }

    public Response<Void> isActivityTaskCanUserCheck(ActivityTaskDO activityTaskDO) {
        String activityTaskId = activityTaskDO.getTaskId();
        if (!activityTaskDO.isFinished()) {
            log.warn("activity task {} not pushable,is not finished", activityTaskId);
            return Response.failedWith(new ChaosError(CommonErrorCode.B_EXPERIMENT_TASK_NOT_PUSHABLE));
        }
        if (!UserCheckState.USER_CHECK_STATE_WAITING.getValue().equals(activityTaskDO.getUserCheckState())) {
            log.warn("activity task {} not waiting check,check state:{}", activityTaskId,
                activityTaskDO.getUserCheckState());
            return Response.failedWith(new ChaosError(CommonErrorCode.B_EXPERIMENT_TASK_NOT_PUSHABLE));
        }
        Optional<ExperimentTaskDO> experimentTaskDOOptional = experimentTaskRepository.findById(
            activityTaskDO.getExperimentTaskId());
        if (experimentTaskDOOptional.get().isFinished()) {
            return Response.failedWith(new ChaosError(CommonErrorCode.B_EXPERIMENT_TASK_ALREADY_FINISHED));
        }
        return Response.ok();

    }

}
