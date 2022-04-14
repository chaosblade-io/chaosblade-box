package com.alibaba.chaosblade.box.common.infrastructure.domain.workspace;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * @Author yinyansheng
 * @create 2020/10/19
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WorkspaceExperiment {
    String experimentId;
    String experimentName;
}
