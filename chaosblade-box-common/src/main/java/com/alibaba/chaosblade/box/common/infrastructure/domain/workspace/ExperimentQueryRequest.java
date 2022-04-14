package com.alibaba.chaosblade.box.common.infrastructure.domain.workspace;

import com.alibaba.chaosblade.box.common.common.domain.BaseRequest;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * @author sunju
 *
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExperimentQueryRequest extends BaseRequest {
    String workspaceId;
    String searchKey;
    Integer page;
}
