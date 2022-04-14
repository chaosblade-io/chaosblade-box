package com.alibaba.chaosblade.box.dao.infrastructure.experiment.guard;

import com.alibaba.chaosblade.box.common.app.sdk.scope.Host;
import com.alibaba.chaosblade.box.dao.model.ExperimentGuardInstanceDO;
import com.alibaba.chaosblade.box.dao.model.ExperimentTaskDO;
import lombok.Data;

import java.util.List;

/**
 * @author haibin
 *
 * 
 */
@Data
public class ExperimentGuardResultLoadRequest {

    private ExperimentGuardInstanceDO experimentGuardInstanceDO;

    private List<Host> hosts;

    private ExperimentTaskDO experimentTaskDO;
}
