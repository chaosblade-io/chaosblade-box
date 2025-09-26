package com.alibaba.chaosblade.box.dao.infrastructure.app;

import com.alibaba.fastjson.annotation.JSONField;
import java.io.Serializable;
import lombok.Data;

/**
 * 应用信息
 *
 * @author haibin
 */
@Data
public class AppInfo implements Serializable {

  @JSONField(name = "app_name")
  private String appName;

  @JSONField(name = "app_id")
  private Long appId;
}
