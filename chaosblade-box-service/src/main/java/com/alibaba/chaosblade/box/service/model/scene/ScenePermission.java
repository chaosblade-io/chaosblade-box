package com.alibaba.chaosblade.box.service.model.scene;

import java.util.List;
import lombok.Data;

/** @author sunpeng */
@Data
public class ScenePermission {

  /** 是否有权限，如无权限页面置灰 */
  private boolean permission;

  /** 无权限原因 */
  private List<String> message;
}
