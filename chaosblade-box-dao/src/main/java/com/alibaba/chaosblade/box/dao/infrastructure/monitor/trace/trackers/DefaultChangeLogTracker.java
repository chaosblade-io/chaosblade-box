package com.alibaba.chaosblade.box.dao.infrastructure.monitor.trace.trackers;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author haibin
 *
 *
 */
@Order
@Component
public class DefaultChangeLogTracker extends BaseChangeLogTracker {
    @Override
    public boolean support(String targetType) {
        return true;
    }

}
