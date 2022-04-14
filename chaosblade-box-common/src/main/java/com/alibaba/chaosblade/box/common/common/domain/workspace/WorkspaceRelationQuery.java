package com.alibaba.chaosblade.box.common.common.domain.workspace;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

/**
 * @author sunju
 *
 */
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
