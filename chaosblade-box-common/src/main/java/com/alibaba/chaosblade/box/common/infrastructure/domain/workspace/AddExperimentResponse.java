package com.alibaba.chaosblade.box.common.infrastructure.domain.workspace;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

/**
 * @Author yinyansheng
 * @create 2020/10/30
 */
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddExperimentResponse {
    Boolean success;
    List<WorkspaceExperiment> duplicateExperiments;
}
