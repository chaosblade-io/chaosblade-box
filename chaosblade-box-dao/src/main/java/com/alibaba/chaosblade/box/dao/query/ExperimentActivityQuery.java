package com.alibaba.chaosblade.box.dao.query;

import com.alibaba.chaosblade.box.common.app.sdk.constants.PhaseType;
import java.util.List;
import lombok.Data;

/** @author haibin */
@Data
public class ExperimentActivityQuery {
  private String experimentId;

  private PhaseType phase;

  private String parentActivityId;

  private String activityId;

  private boolean excludePointcut;

  private boolean isDeleted = false;

  private List<String> flowIds;
}
