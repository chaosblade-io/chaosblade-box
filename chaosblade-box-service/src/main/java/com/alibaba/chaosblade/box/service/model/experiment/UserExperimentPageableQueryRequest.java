package com.alibaba.chaosblade.box.service.model.experiment;

import com.alibaba.chaosblade.box.common.common.enums.ExperimentStateEnum;
import com.alibaba.chaosblade.box.common.common.enums.ResultEnum;
import com.alibaba.chaosblade.box.common.common.domain.PageableRequest;
import lombok.Data;

import java.util.List;

/**
 * @author haibin
 *
 *
 */
@Data
public class UserExperimentPageableQueryRequest extends PageableRequest {

    private String searchKey;

    private ExperimentStateEnum state;

    private List<ExperimentStateEnum> states;

    private List<ResultEnum> results;

    private List<String> tagNames;

    private boolean scheduler;

}
