package com.alibaba.chaosblade.box.common.common.domain.feedback;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/** @author haibin.lhb */
@Data
public class ExtraFeedbackComponent implements Serializable {

  /** 反馈的组件唯一code */
  private String code;
  /** 组件描述 */
  private String name;

  /** 反馈的组件选项 */
  private List<ExtraFeedbackOption> options = new ArrayList<>();

  /** 反馈之后回调的连接 */
  private String redirectUrl;
}
