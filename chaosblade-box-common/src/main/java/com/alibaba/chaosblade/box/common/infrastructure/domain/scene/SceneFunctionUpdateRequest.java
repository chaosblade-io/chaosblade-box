package com.alibaba.chaosblade.box.common.infrastructure.domain.scene;

import com.alibaba.chaosblade.box.common.common.domain.BaseRequest;
import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

/**
 * @author sunju
 *
 *
 * Modified by jiumu in 2019-07-18
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class SceneFunctionUpdateRequest extends BaseRequest {

    String sceneId;
    String functionId;
    ChaosUser user;
    String code;
    String name;
    String description;
    Boolean agentRequired;
    Integer enabled;
    Integer phaseFlag;
    String alias;
    String version;
    List<SceneFunctionSystemVersion> systemVersions;
    List<String> categoryIds;
    List<Integer> supportScopeTypes;
    List<SceneFunctionDependency> dependencies;

}
