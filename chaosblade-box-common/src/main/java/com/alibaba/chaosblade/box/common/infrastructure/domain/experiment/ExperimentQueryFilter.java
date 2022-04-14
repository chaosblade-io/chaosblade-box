package com.alibaba.chaosblade.box.common.infrastructure.domain.experiment;

import com.alibaba.chaosblade.box.common.common.enums.ExperimentStateEnum;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * @author sunju
 *
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExperimentQueryFilter {

    ExperimentStateEnum state;

}
