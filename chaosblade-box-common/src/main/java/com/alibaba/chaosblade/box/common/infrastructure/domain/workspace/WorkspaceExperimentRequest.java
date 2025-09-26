package com.alibaba.chaosblade.box.common.infrastructure.domain.workspace;

import com.alibaba.chaosblade.box.common.common.domain.BaseRequest;
import java.util.List;
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * @Author yinyansheng
 *
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
