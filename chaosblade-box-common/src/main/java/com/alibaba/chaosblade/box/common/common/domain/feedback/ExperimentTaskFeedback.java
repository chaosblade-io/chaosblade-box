package com.alibaba.chaosblade.box.common.common.domain.feedback;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 演练反馈内容
 *
 * @author haibin.lhb
 *
 * 
 */
@Data
public class ExperimentTaskFeedback implements Serializable {

    private String feedbackId;

    /**
     * 反馈额外信息
     */
    private ExtraFeedbackComponent extra;

    /**
     * 反馈时间
     */
    private Date feedbackTime;

    /**
     * 用户备注
     */
    private String memo;

    /**
     * 是否符合预期
     */
    private Integer expectationStatus;

    /**
     * 业务影响程度
     */
    private Integer businessStatus;

}
