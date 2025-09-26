package com.alibaba.chaosblade.box.dao.query;

import java.io.Serializable;
import java.util.List;
import lombok.Data;

/** @author haibin */
@Data
public class ActivityTaskQuery implements Serializable {

  private String taskId;

  private String experimentTaskId;

  private String activityId;

  private List<Integer> states;

  private List<Integer> excludeStates;
}
