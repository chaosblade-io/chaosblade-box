package com.alibaba.chaosblade.box.common.infrastructure.domain.workspace;

import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/** @author sunju */
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WorkspaceInfo {
  String workspaceId;
  String name;
  String description;
  Integer experimentCount;
  Integer memberCount;
  Integer type;
  List<WorkspaceMember> administrators;
}
