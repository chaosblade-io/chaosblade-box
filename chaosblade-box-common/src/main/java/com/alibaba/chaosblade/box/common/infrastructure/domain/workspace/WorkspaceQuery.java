package com.alibaba.chaosblade.box.common.infrastructure.domain.workspace;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

/**
 * @author sunju
 *
 */
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
