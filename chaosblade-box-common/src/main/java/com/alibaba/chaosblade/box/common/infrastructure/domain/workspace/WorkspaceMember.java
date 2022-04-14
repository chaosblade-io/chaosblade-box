package com.alibaba.chaosblade.box.common.infrastructure.domain.workspace;

import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * @author sunju
 * 
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkspaceMember {
    String relationId;
    String userId;
    String userName;
    Integer permission;
    Integer userRole;
    Integer memberRole;
}
