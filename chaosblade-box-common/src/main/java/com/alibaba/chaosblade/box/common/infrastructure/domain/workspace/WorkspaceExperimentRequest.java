package com.alibaba.chaosblade.box.common.infrastructure.domain.workspace;

import com.alibaba.chaosblade.box.common.common.domain.BaseRequest;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

/**
 * @Author yinyansheng
 * @create 2020/10/19
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkspaceExperimentRequest extends BaseRequest {
    String workspaceId;
    List<WorkspaceExperiment> workspaceExperimentList;
}
