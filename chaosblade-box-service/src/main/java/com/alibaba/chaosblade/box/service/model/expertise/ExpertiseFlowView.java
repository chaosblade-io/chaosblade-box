package com.alibaba.chaosblade.box.service.model.expertise;

import lombok.Data;

import java.util.List;
import java.util.Set;

/**
 * @author haibin
 *
 *
 */
@Data
public class ExpertiseFlowView {

    private List<ExpertiseFlowActivity> activities;

    private Set<Integer> scopeType;
}
