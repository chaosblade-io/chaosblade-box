package com.alibaba.chaosblade.box.common.app.sdk.scope;

import java.util.List;
import lombok.Data;

/** @author haibin */
@Data
public class ActivityScope {

  /** All scopes */
  private List<? extends Scope> scopes;

  /** the current scope index,the first index is 0 */
  private int index;
}
