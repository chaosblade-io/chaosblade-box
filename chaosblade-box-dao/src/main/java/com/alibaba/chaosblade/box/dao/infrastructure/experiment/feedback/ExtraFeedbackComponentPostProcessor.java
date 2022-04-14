package com.alibaba.chaosblade.box.dao.infrastructure.experiment.feedback;

import com.alibaba.chaosblade.box.common.common.domain.feedback.ExtraFeedbackComponent;
import com.alibaba.chaosblade.box.dao.model.ExperimentTaskDO;

/**
 * @author haibin.lhb
 *
 *
 */
public interface ExtraFeedbackComponentPostProcessor {

    /**
     * 获取第三方的反馈组件
     *
     * @param experimentTaskDO 演练任务
     * @return 组件描述
     */
    public ExtraFeedbackComponent acquireExtraFeedbackComponent(ExperimentTaskDO experimentTaskDO);

    /**
     * 提交新的反馈
     *
     * @param experimentTaskDO
     * @param extraFeedbackComponent
     * @return true or false,第三方反馈是否提交成功
     * @throws Exception
     */
    public boolean submitExtraFeedbackComponent(ExperimentTaskDO experimentTaskDO,
        ExtraFeedbackComponent extraFeedbackComponent)
        throws Throwable;
}
