package com.alibaba.chaosblade.box.common.infrastructure.domain.scene;

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
public class SceneAuthorizedUpdateRequest {

    String authorizedId;
    String functionId;
    String functionName;
    String functionCode;
    Integer phase;
    Integer enabled;
    Integer source;
    Boolean isDelete;

    List<String> functionIds;
    List<Integer> supportScopeTypes;
    List<String> authorizedIds;
    private boolean isPublic;

    public void setIsPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    public boolean getIsPublic() {
        return isPublic;
    }
}
