package com.alibaba.chaosblade.box.common.common.domain.workspace;

import java.util.List;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/** @author sunju */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WorkspaceRelationQuery {

  List<String> relationIds;
  List<String> workspaceIds;
  List<String> outerIds;
  String relationType;
  //    Integer userRole;
  //    Integer memberRole;
  String namespace;

  String experimentCreator;
  boolean excludeDeleted = true;
}
