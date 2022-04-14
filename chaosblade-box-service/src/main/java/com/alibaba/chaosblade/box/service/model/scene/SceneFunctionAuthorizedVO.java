package com.alibaba.chaosblade.box.service.model.scene;

import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * @author sunju
 *
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SceneFunctionAuthorizedVO {

    String authorizedId;
    ChaosUser grantFrom;
    ChaosUser grantTo;
    String functionId;
    String permission;
    Boolean isPublic;

}
