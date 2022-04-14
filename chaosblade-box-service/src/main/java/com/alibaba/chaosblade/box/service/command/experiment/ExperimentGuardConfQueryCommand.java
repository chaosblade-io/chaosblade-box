package com.alibaba.chaosblade.box.service.command.experiment;

import com.alibaba.chaosblade.box.common.commands.SpringBeanCommand;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.guard.ExperimentGuard;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.guard.ExperimentGuardConfiguration;
import com.alibaba.chaosblade.box.dao.model.ExperimentGuardDO;
import com.alibaba.chaosblade.box.dao.repository.ExperimentGuardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author haibin
 *
 *
 */
@Component
public class ExperimentGuardConfQueryCommand
    extends SpringBeanCommand<String, ExperimentGuardConfiguration> {

    @Autowired
    private ExperimentGuardRepository experimentGuardRepository;

    @Override
    public ExperimentGuardConfiguration execute(String experimentId) {
        List<ExperimentGuardDO> experimentGuardDOS = experimentGuardRepository.findByExperimentId(experimentId);
        ExperimentGuardConfiguration experimentGuardConfiguration = new ExperimentGuardConfiguration();
        experimentGuardConfiguration.setGuards(experimentGuardDOS.stream().map(
            new Function<ExperimentGuardDO, ExperimentGuard>() {
                @Override
                public ExperimentGuard apply(ExperimentGuardDO experimentGuardDO) {
                    ExperimentGuard experimentGuard = new ExperimentGuard();
                    experimentGuard.setAppCode(experimentGuardDO.getAppCode());
                    experimentGuard.setGuardId(experimentGuardDO.getGuardId());
                    experimentGuard.setArguments(experimentGuardDO.getArgument().getArguments());
                    experimentGuard.setFields(experimentGuardDO.getArgument().getFields());
                    experimentGuard.setTolerance(experimentGuardDO.getArgument().getTolerance());
                    experimentGuard.setName(experimentGuardDO.getName());
                    experimentGuard.setActionType(experimentGuardDO.getActionType());
                    return experimentGuard;
                }
            }).collect(Collectors.toList()));
        return experimentGuardConfiguration;
    }
}
