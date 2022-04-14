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
public class SceneAuthorizedQueryRequest {

    String functionId;
    String searchKey;
    String grantFrom;
    String grantTo;
    Integer enabled;
    Integer source;
    Boolean isPublic;
    Boolean isDelete = false;
    Integer permission;
    Integer phase;
    Integer supportScopeType;
    List<String> functionIds;
    Integer k8sResourceType;

}
