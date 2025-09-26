package com.alibaba.chaosblade.box.service.model.expertise;

import java.util.List;
import java.util.Set;
import lombok.Data;

/** @author haibin */
@Data
public class ExpertiseFlowView {

  private List<ExpertiseFlowActivity> activities;

  private Set<Integer> scopeType;
}
