package com.alibaba.chaosblade.box.service;

import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.common.common.domain.feedback.ExperimentTaskFeedback;
import com.alibaba.chaosblade.box.common.common.domain.feedback.ExperimentTaskFeedbackSubmitRequest;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.feedback.FeedbackSubmitResult;

/**
 * @author haibin.lhb
 *
 *
 */
public interface FeedbackService {

    /**
     * 查询演练反馈
     *
     * @param experimentTaskId 演练任务ID
     * @return
     */
    public Response<ExperimentTaskFeedback> getExperimentTaskFeedback(String experimentTaskId);

    /**
     * 提交返回,
     *
     * @param experimentTaskFeedbackSubmitRequest
     * @return 返回反馈ID
     */
    public Response<FeedbackSubmitResult> submitExperimentTaskFeedback(
        ExperimentTaskFeedbackSubmitRequest experimentTaskFeedbackSubmitRequest);
}
