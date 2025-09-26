package com.alibaba.chaosblade.box.common.experiment.request;

import com.alibaba.chaosblade.box.common.common.domain.PageableRequest;
import java.util.List;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/** Author: sunju */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderQueryRequest extends PageableRequest {

  String experimentId;
  String experimentTaskId;
  List<String> experimentTaskIds;
  String packType;
  Boolean consumable;
  Boolean expired;
}
