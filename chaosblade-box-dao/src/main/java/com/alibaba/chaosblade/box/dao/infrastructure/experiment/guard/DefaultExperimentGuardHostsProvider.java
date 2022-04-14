package com.alibaba.chaosblade.box.dao.infrastructure.experiment.guard;

import com.alibaba.chaosblade.box.common.app.sdk.constants.PhaseType;
import com.alibaba.chaosblade.box.common.app.sdk.scope.Host;
import com.alibaba.chaosblade.box.dao.model.ActivityTaskDO;
import com.alibaba.chaosblade.box.dao.model.ExperimentTaskDO;
import com.alibaba.chaosblade.box.dao.repository.ActivityTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author haibin
 *
 *
 */

public class DefaultExperimentGuardHostsProvider implements ExperimentGuardHostsProvider {

    @Autowired
    private ActivityTaskRepository activityTaskRepository;

    @Override
    public List<Host> provide(ExperimentGuardInstanceExecutionRequest experimentGuardInstanceExecutionRequest) {
        ExperimentTaskDO experimentTaskDO = experimentGuardInstanceExecutionRequest.getExperimentTaskDO();
        List<ActivityTaskDO> activityTaskDOS = activityTaskRepository.findByExperimentTaskIdAndPhase(
            experimentTaskDO.getTaskId(), PhaseType.ATTACK);
        return activityTaskDOS.stream().flatMap((Function<ActivityTaskDO, Stream<Host>>)activityTaskDO -> {
            try {
                return activityTaskDO.getRunParam().getScope().stream();
            } catch (Exception ex) {
                return Stream.empty();
            }
        }).distinct().collect(Collectors.toList());
    }
}
