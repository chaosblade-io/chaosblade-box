package com.alibaba.chaosblade.box.common.infrastructure.domain.scene;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * @author jiumu
 *
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class SceneFunctionFissionRequest extends SceneFunctionCreateRequest {

    String functionId;

}
