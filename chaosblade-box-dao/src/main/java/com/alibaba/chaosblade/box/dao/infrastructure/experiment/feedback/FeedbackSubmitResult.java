package com.alibaba.chaosblade.box.dao.infrastructure.experiment.feedback;

import lombok.Data;

/**
 * @author haibin.lhb
 *
 *
 */
@Data
public class FeedbackSubmitResult {

    private String redirectUrl;

    private String feedbackId;

    private Integer source;
}
