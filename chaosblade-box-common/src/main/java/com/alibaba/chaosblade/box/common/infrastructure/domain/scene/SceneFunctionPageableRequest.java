package com.alibaba.chaosblade.box.common.infrastructure.domain.scene;

import com.alibaba.chaosblade.box.common.common.domain.PageableRequest;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * @author sunju
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SceneFunctionPageableRequest extends PageableRequest {

    Integer phase;

}
