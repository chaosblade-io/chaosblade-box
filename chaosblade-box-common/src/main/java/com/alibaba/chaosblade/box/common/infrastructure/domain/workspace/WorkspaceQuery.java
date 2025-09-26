package com.alibaba.chaosblade.box.common.infrastructure.domain.workspace;

import java.util.List;
import lombok.*;
import lombok.experimental.FieldDefaults;

/** @author sunju */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WorkspaceQuery {

  List<String> workspaceIds;
  String userId;
  Integer type;
  //    Integer userRole;
  String name;
  List<String> userIds;
}
