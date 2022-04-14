package com.alibaba.chaosblade.box.dao.infrastructure.domain.workspace;

import com.alibaba.chaosblade.box.common.infrastructure.domain.workspace.WorkspaceMember;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

/**
 * @author sunju
 *
 */
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WorkspaceBaseInfo {
    String workspaceId;
    String name;
    String description;
    List<WorkspaceMember> members;
}
