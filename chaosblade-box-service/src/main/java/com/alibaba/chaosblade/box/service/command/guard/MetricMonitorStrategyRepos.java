package com.alibaba.chaosblade.box.service.command.guard;

import com.alibaba.chaosblade.box.dao.infrastructure.experiment.guard.ExperimentGuardResultLoadRequest;
import com.alibaba.chaosblade.box.dao.repository.ChaosBladeExpUidRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * @author haibin.lhb
 *
 * 
 */
@Component
public class MetricMonitorStrategyRepos {

    @Autowired
    private List<MonitorStrategy> monitorStrategyList;

    private static Integer METRIC_DATE_COLLECT_TIME_OFFSET = 60 * 2;

    @Autowired
    private ChaosBladeExpUidRepository chaosBladeExpUidRepository;

    public Optional<MonitorStrategy> select(ExperimentGuardResultLoadRequest experimentGuardResultLoadRequest) {
        return monitorStrategyList.stream().filter(new Predicate<MonitorStrategy>() {
            @Override
            public boolean test(MonitorStrategy monitorStrategy) {
                return monitorStrategy.support(experimentGuardResultLoadRequest);
            }
        }).findFirst();

    }

}
