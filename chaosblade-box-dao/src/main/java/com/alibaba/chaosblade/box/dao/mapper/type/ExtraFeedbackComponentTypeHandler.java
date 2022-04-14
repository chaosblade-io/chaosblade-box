package com.alibaba.chaosblade.box.dao.mapper.type;


import com.alibaba.chaosblade.box.common.common.domain.feedback.ExtraFeedbackComponent;

/**
 * @author haibin.lhb
 *
 *
 */
public class ExtraFeedbackComponentTypeHandler extends BaseFastJsonTypeHandler<ExtraFeedbackComponent> {
    @Override
    public Class<ExtraFeedbackComponent> getObjectClass() {
        return ExtraFeedbackComponent.class;
    }
}
