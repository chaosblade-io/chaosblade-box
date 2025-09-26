package com.alibaba.chaosblade.box.service.model.expertise;

import java.util.List;
import lombok.Data;

/**
 * 演练评测信息
 *
 * @author haibin
 */
@Data
public class ExpertiseEvaluationInfo {

  /** 条目信息 */
  private List<ExpertiseEvaluationItem> items;
}
