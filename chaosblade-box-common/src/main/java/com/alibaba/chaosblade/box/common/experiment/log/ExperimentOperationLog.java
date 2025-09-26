package com.alibaba.chaosblade.box.common.experiment.log;

import com.alibaba.fastjson.annotation.JSONField;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/** @author haibin.lhb */
@Data
public class ExperimentOperationLog implements Serializable {

  /** 操作时间 */
  private Date time;

  /** 操作人员 */
  @JSONField(name = "operator")
  private String operator;

  /** 操作类型 */
  @JSONField(name = "change_type")
  private String changeType;

  /** 描述 */
  @JSONField(name = "change_desc")
  private String description;

  @JSONField(name = "property_id")
  private String propertyId;
}
