package com.alibaba.chaosblade.box.common.infrastructure.domain.scene;

import com.alibaba.chaosblade.box.common.common.domain.BaseRequest;
import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

/**
 * @author haibin
 *
 *
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SceneQueryRequest extends BaseRequest {

    ChaosUser user;
    String sceneId;
    String functionId;
    String categoryId;
    String parameterId;
    List<String> codes;
    Integer permission;
    Integer phase;
    Integer source;
    Integer enabled = 2;
    Integer supportScopeType;
    Boolean isDelete = false;
    Boolean isPublic;
    String searchKey;
    Integer osType;

    private Integer k8sResourceType;

    private List<String> functionIds;
}
