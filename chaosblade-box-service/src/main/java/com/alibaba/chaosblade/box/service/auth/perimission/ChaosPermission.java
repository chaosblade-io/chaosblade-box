package com.alibaba.chaosblade.box.service.auth.perimission;

/** @author haibin */
import static com.alibaba.chaosblade.box.common.infrastructure.constant.CommonConstant.VERTICAL_LINE;

import lombok.Data;

/** @author haibin */
@Data
public class ChaosPermission {

  public static String READ_OPERATION = "READ";

  public static String WRITE_OPERATION = "WRITE";

  private String permission;

  private String target;

  public String getName() {
    return permission + VERTICAL_LINE + target;
  }

  public ChaosPermission() {}

  public ChaosPermission(String permission, String target) {
    this.permission = permission;
    this.target = target;
  }
}
