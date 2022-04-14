package com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.guard;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author haibin
 *
 *
 */
@Data
public class ExperimentGuardConfiguration {

    private List<ExperimentGuard> guards = new ArrayList<>();

}
