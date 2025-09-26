package com.alibaba.chaosblade.box.common.common.domain.experiment;

import com.alibaba.chaosblade.box.common.app.sdk.constants.PhaseType;
import com.alibaba.chaosblade.box.common.common.domain.activity.ActivityTask;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

/** @author haibin */
@Data
@AllArgsConstructor
public class PhaseInfo {

  /** 阶段名,PREPARE,ATTACK,CHECK,RECOVER */
  private PhaseType phase;

  /** 活动任务的信息 */
  private List<ActivityTask> activities;
}
