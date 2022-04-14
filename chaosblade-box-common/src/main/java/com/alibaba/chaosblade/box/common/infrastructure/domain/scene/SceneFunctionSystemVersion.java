package com.alibaba.chaosblade.box.common.infrastructure.domain.scene;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor(staticName = "of")
public class SceneFunctionSystemVersion {

    String system;
    String version;

}
