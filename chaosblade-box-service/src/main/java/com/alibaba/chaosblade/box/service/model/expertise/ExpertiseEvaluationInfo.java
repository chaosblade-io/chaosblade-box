package com.alibaba.chaosblade.box.service.model.expertise;

import lombok.Data;

import java.util.List;

/**
 * 演练评测信息
 *
 * @author haibin
 *
 *
 */
@Data
public class ExpertiseEvaluationInfo {

    /**
     * 条目信息
     */
    private List<ExpertiseEvaluationItem> items;

}
