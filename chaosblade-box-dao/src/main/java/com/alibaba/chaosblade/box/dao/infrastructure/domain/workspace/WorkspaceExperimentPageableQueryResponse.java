package com.alibaba.chaosblade.box.dao.infrastructure.domain.workspace;

import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.response.UserExperiment;
import com.alibaba.chaosblade.box.dao.model.base.PageableResponse;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class WorkspaceExperimentPageableQueryResponse {
    Integer permission;

    PageableResponse<UserExperiment> pageableResponse;
}
