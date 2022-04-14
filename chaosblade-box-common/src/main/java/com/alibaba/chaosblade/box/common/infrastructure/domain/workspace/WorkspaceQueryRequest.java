package com.alibaba.chaosblade.box.common.infrastructure.domain.workspace;

import com.alibaba.chaosblade.box.common.common.domain.BaseRequest;
import lombok.Data;

@Data
public class WorkspaceQueryRequest extends BaseRequest {
    String experimentId;

    String workspaceId;
}
