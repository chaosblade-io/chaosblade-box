package com.alibaba.chaosblade.box.common.infrastructure.domain.scene;

import com.alibaba.chaosblade.box.common.common.domain.BaseRequest;
import com.alibaba.chaosblade.box.common.common.domain.definition.SceneFunctionParameterComponent;
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
public class SceneFunctionParameterUpdateRequest extends BaseRequest {

    ChaosUser user;
    String parameterId;
    String functionId;
    String name;
    String alias;
    String description;
    Integer sequence;
    SceneFunctionParameterComponent component;

}
