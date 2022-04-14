package com.alibaba.chaosblade.box.dao.infrastructure.experiment.feedback.processor;

import com.alibaba.chaosblade.box.common.common.domain.feedback.ExtraFeedbackComponent;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.feedback.ExtraFeedbackComponentPostProcessor;
import com.alibaba.chaosblade.box.dao.model.ExperimentTaskDO;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author haibin.lhb
 *
 *
 */
@Component
@Order(Ordered.LOWEST_PRECEDENCE)
public class MockExtraFeedbackComponentPostProcessor implements ExtraFeedbackComponentPostProcessor {
    @Override
    public ExtraFeedbackComponent acquireExtraFeedbackComponent(ExperimentTaskDO experimentTaskDO) {
        return null;
    }

    @Override
    public boolean submitExtraFeedbackComponent(ExperimentTaskDO experimentTaskDO,
        ExtraFeedbackComponent extraFeedbackComponent)
        throws Exception {
        return true;
    }
}
