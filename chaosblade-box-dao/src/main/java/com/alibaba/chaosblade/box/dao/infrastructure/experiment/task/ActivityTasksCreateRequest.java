package com.alibaba.chaosblade.box.dao.infrastructure.experiment.task;

import com.alibaba.chaosblade.box.common.common.domain.experiment.ActivityRunParam;
import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import com.alibaba.chaosblade.box.common.common.enums.ExperimentRunModeEnum;
import com.alibaba.chaosblade.box.dao.model.ExperimentDO;
import com.alibaba.chaosblade.box.dao.model.ExperimentTaskDO;
import lombok.Data;

import java.util.List;

/**
 * @author haibin
 *
 * 
 */
@Data
public class ActivityTasksCreateRequest {

    ChaosUser chaosUser;

    ExperimentDO experimentDO;

    ExperimentTaskDO experimentTaskDO;

    List<ActivityRunParam> params;

    ExperimentRunModeEnum experimentRunModeEnum;
}
