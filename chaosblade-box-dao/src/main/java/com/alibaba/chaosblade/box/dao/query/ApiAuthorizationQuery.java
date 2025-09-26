package com.alibaba.chaosblade.box.dao.query;

import java.util.List;
import lombok.Data;

/** @author haibin */
@Data
public class ApiAuthorizationQuery {

  private String accessToken;

  private String permission;

  private List<String> permissionTargets;
}
