package com.alibaba.chaosblade.box.dao.infrastructure.experiment.feedback;

import com.alibaba.chaosblade.box.common.common.domain.FeedBackConstant;
import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.common.common.domain.feedback.ExperimentTaskFeedback;
import com.alibaba.chaosblade.box.common.common.domain.feedback.ExtraFeedbackComponent;
import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import com.alibaba.chaosblade.box.common.common.enums.CommonErrorCode;
import com.alibaba.chaosblade.box.dao.infrastructure.checker.ExperimentChecker;
import com.alibaba.chaosblade.box.dao.infrastructure.tunnel.TransactionUtil;
import com.alibaba.chaosblade.box.dao.model.ExperimentTaskDO;
import com.alibaba.chaosblade.box.dao.model.ExperimentTaskFeedbackDO;
import com.alibaba.chaosblade.box.dao.repository.ExperimentTaskRepository;
import com.alibaba.chaosblade.box.dao.repository.FeedbackRepository;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;

import java.util.Date;
import java.util.List;

/**
 * @author haibin.lhb
 *
 * 
 */
@Component
@Slf4j
public class FeedbackProcessor implements InitializingBean {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private TransactionUtil transactionUtil;

    @Autowired
    private ExperimentChecker experimentChecker;

    @Autowired
    private ExperimentTaskRepository experimentTaskRepository;

    @Autowired
    private List<ExtraFeedbackComponentPostProcessor> extraFeedbackComponentPostProcessors;

    public Response<ExperimentTaskDO> checkTaskAllowFeedback(String experimentTaskId) {
        Response<ExperimentTaskDO> response = experimentChecker.assertExperimentTaskExist(experimentTaskId);
        if (!response.isSuccess()) {
            return Response.failedWith(response.getError());
        }
        return response;
    }

    public ExperimentTaskFeedbackDO createExperimentTaskFeedback(ExperimentTaskDO experimentTaskDO,
                                                                 ExtraFeedbackComponent extraFeedbackComponent) {
        ExperimentTaskFeedbackDO newExperimentFeedTaskDO = new ExperimentTaskFeedbackDO();
        newExperimentFeedTaskDO.setFeedbackId(IdWorker.getIdStr());
        newExperimentFeedTaskDO.setUserId(experimentTaskDO.getUserId());
        newExperimentFeedTaskDO.setExperimentTaskId(experimentTaskDO.getTaskId());
        newExperimentFeedTaskDO.setExperimentId(experimentTaskDO.getExperimentId());
        newExperimentFeedTaskDO.setExtraComponent(extraFeedbackComponent);
        //默认是符合预期，不影响业务
        newExperimentFeedTaskDO.setBusinessStatus(FeedBackConstant.BusinessImpactStatus.BUSINESS_IMPACT_STATUS_NO.getValue());
        newExperimentFeedTaskDO.setExpectationStatus(FeedBackConstant.ExpectationStatus.EXPECTATION_STATUS_YES.getValue());
        if (experimentTaskDO.isFinished()) {
            feedbackRepository.add(newExperimentFeedTaskDO);
            updateTaskFeedbackStatus(experimentTaskDO, FeedBackConstant.FeedbackStatus.FEEDBACK_STATUS_WAITING);
        }
        return newExperimentFeedTaskDO;
    }

    public ExperimentTaskFeedback getExperimentTaskFeedback(ExperimentTaskDO experimentTaskDO) {
        ExtraFeedbackComponent extraFeedbackComponent = provideExtraFeedbackComponent(experimentTaskDO);
        ExperimentTaskFeedbackDO experimentTaskFeedbackDO = feedbackRepository.findByExperimentTaskId(
            experimentTaskDO.getTaskId()).orElseGet(
            () -> createExperimentTaskFeedback(experimentTaskDO, extraFeedbackComponent));
        ExperimentTaskFeedback experimentTaskFeedback = new ExperimentTaskFeedback();
        experimentTaskFeedback.setBusinessStatus(experimentTaskFeedbackDO.getBusinessStatus());
        experimentTaskFeedback.setExpectationStatus(experimentTaskFeedbackDO.getExpectationStatus());
        experimentTaskFeedback.setFeedbackTime(experimentTaskFeedbackDO.getFeedbackTime());
        experimentTaskFeedback.setMemo(experimentTaskFeedbackDO.getMemo());
        experimentTaskFeedback.setFeedbackId(experimentTaskFeedbackDO.getFeedbackId());
        experimentTaskFeedback.setExtra(extraFeedbackComponent);
        return experimentTaskFeedback;
    }

    /**
     * 提交反馈
     *
     * @param chaosUser
     * @param experimentTaskDO
     * @param experimentTaskFeedback
     * @return
     */
    public Response<FeedbackSubmitResult> submitExperimentTaskFeedback(ChaosUser chaosUser, ExperimentTaskDO experimentTaskDO,
                                                                       ExperimentTaskFeedback experimentTaskFeedback) {
        boolean allowFeedback = experimentTaskDO.getFeedBackStatus() != null || experimentTaskDO.isFinished();
        if (!allowFeedback) {
            return Response.failedWith(CommonErrorCode.B_EXPERIMENT_TASK_FEEDBACK_DENIED);
        }
        ExperimentTaskFeedbackDO experimentTaskFeedbackDO
            = initExperimentTaskFeedbackDO(chaosUser, experimentTaskDO, experimentTaskFeedback);
        return transactionUtil.execute(new TransactionCallback<Response<FeedbackSubmitResult>>() {
            @Override
            public Response<FeedbackSubmitResult> doInTransaction(TransactionStatus transactionStatus) {
                if (experimentTaskFeedbackDO.getFeedbackId() == null) {
                    feedbackRepository.add(experimentTaskFeedbackDO);
                } else {
                    feedbackRepository.update(experimentTaskFeedbackDO);
                }
                updateTaskFeedbackStatus(experimentTaskDO, FeedBackConstant.FeedbackStatus.FEEDBACK_STATUS_FINISHED);
                try {
                    consumeExtraFeedbackComponent(experimentTaskDO, experimentTaskFeedback.getExtra());
                } catch (Throwable exception) {
                    log.error("consume extraFeedbackComponent failed", exception);
                    TransactionUtil.setRollback(transactionStatus);
                    return Response.failedWith(CommonErrorCode.B_FEEDBACK_SUBMIT_FAILED);
                }
                FeedbackSubmitResult feedbackSubmitResult = new FeedbackSubmitResult();
                if (experimentTaskFeedback.getExtra() != null) {
                    feedbackSubmitResult.setRedirectUrl(experimentTaskFeedback.getExtra().getRedirectUrl());
                }
                feedbackSubmitResult.setFeedbackId(experimentTaskFeedbackDO.getFeedbackId());
                feedbackSubmitResult.setSource(experimentTaskDO.getExperimentTaskContext().getSource());
                return Response.okWithData(feedbackSubmitResult);
            }
        });

    }

    private void updateTaskFeedbackStatus(ExperimentTaskDO experimentTaskDO, FeedBackConstant.FeedbackStatus feedbackStatus) {
        experimentTaskRepository.updateFeedbackStatus(experimentTaskDO.getTaskId(), feedbackStatus);
    }

    private ExperimentTaskFeedbackDO initExperimentTaskFeedbackDO(ChaosUser chaosUser, ExperimentTaskDO experimentTaskDO,
                                                                  ExperimentTaskFeedback experimentTaskFeedback) {
        ExperimentTaskFeedbackDO experimentTaskFeedbackDO = feedbackRepository.findByExperimentTaskId(
            experimentTaskDO.getTaskId()).orElse(null);
        if (experimentTaskFeedbackDO == null) {
            experimentTaskFeedbackDO = new ExperimentTaskFeedbackDO();
        }
        experimentTaskFeedbackDO.setExperimentId(experimentTaskDO.getExperimentId());
        experimentTaskFeedbackDO.setExperimentTaskId(experimentTaskDO.getTaskId());
        experimentTaskFeedbackDO.setExpectationStatus(experimentTaskFeedback.getExpectationStatus());
        experimentTaskFeedbackDO.setBusinessStatus(experimentTaskFeedback.getBusinessStatus());
        experimentTaskFeedbackDO.setFeedbackTime(new Date());
        experimentTaskFeedbackDO.setMemo(experimentTaskFeedback.getMemo());
//        experimentTaskFeedbackDO.setSubUserId(mkUser.getSubUserId());
        experimentTaskFeedbackDO.setUserId(chaosUser.getUserId());
        experimentTaskFeedbackDO.setExtraComponent(experimentTaskFeedback.getExtra());
        return experimentTaskFeedbackDO;
    }

    private ExtraFeedbackComponent provideExtraFeedbackComponent(ExperimentTaskDO experimentTaskDO) {
        ExtraFeedbackComponent extraFeedbackComponent;
        for (ExtraFeedbackComponentPostProcessor extraFeedbackComponentPostProcessor :
            extraFeedbackComponentPostProcessors) {
            try {
                extraFeedbackComponent = extraFeedbackComponentPostProcessor.acquireExtraFeedbackComponent(
                    experimentTaskDO);
                if (extraFeedbackComponent != null) {
                    return extraFeedbackComponent;
                }
            } catch (Exception exception) {
                log.error("provide extraFeedbackComponent failed,class:{}",
                    extraFeedbackComponentPostProcessor.getClass().getName(), exception);
                break;
            }
        }
        return null;
    }

    private void consumeExtraFeedbackComponent(ExperimentTaskDO experimentTaskDO,
        ExtraFeedbackComponent extraFeedbackComponent) throws Throwable {
        for (ExtraFeedbackComponentPostProcessor extraFeedbackComponentPostProcessor :
            extraFeedbackComponentPostProcessors) {
            if (extraFeedbackComponentPostProcessor.submitExtraFeedbackComponent(experimentTaskDO,
                extraFeedbackComponent)) {
                break;
            }
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        AnnotationAwareOrderComparator.sort(extraFeedbackComponentPostProcessors);
    }
}
