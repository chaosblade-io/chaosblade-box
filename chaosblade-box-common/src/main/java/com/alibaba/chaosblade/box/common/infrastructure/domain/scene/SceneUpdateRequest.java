package com.alibaba.chaosblade.box.common.infrastructure.domain.scene;

import com.alibaba.chaosblade.box.common.common.domain.BaseRequest;
import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * @author sunju
 *
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class SceneUpdateRequest extends BaseRequest {

    Integer state;
    String sceneId;
    String code;
    String name;
    String description;
    String version;
    ChaosUser user;

}
