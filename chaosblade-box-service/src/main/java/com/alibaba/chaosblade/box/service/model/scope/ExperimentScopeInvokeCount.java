package com.alibaba.chaosblade.box.service.model.scope;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/** @author haibin */
@Data
public class ExperimentScopeInvokeCount implements Serializable {

  private Date time;

  private Integer total;
}
