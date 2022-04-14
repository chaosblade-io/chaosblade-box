package com.alibaba.chaosblade.box.common.infrastructure.domain.workspace;

import com.alibaba.chaosblade.box.common.common.domain.PageableRequest;
import com.alibaba.chaosblade.box.common.common.enums.ExperimentStateEnum;
import com.alibaba.chaosblade.box.common.common.enums.ResultEnum;
import lombok.Data;

import java.util.List;

/**
 * @author haibin
 *
 *
 */
@Data
public class WorkspaceExperimentPageableQueryRequest extends PageableRequest {

    private String workspaceId;

    private String searchKey;

    private ExperimentStateEnum state;

    private List<ExperimentStateEnum> states;

    private List<ResultEnum> results;

    private List<String> tagNames;

    private String filterUserId;

}