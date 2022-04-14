package com.alibaba.chaosblade.box.dao.infrastructure.monitor.trace.trackers;

import com.alibaba.chaosblade.box.common.infrastructure.constant.ChangelogTypes;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author haibin.lhb
 *
 *
 */
@Component
@Order(value = 400)
public class ExperimentChangeLogTracker extends BaseChangeLogTracker {
    @Override
    public boolean support(String targetType) {
        return ChangelogTypes.ChangeTargetType.EXPERIMENT.getName().equals(targetType);
    }

}
