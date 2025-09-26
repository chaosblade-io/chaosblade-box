package com.alibaba.chaosblade.box.common.common.domain.experiment;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/** @author haibin */
@Data
public class WorkspaceShortInfo implements Serializable {

  /** workspace */
  String workspaceId;

  String name;

  private String relationType;

  /** creator */
  private String userId;

  /** 描述 */
  private String description;

  /** create time */
  private Date createTime;
}
