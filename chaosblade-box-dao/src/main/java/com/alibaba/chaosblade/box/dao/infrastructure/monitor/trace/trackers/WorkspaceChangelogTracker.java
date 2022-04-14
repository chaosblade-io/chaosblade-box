package com.alibaba.chaosblade.box.dao.infrastructure.monitor.trace.trackers;

import com.alibaba.chaosblade.box.common.infrastructure.constant.ChangelogTypes;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author sunju
 * 
 */
@Component
@Order(value = Ordered.HIGHEST_PRECEDENCE)
public class WorkspaceChangelogTracker extends BaseChangeLogTracker {

    @Override
    public boolean support(String targetType) {
        return ChangelogTypes.ChangeTargetType.WORKSPACE.getName().equals(targetType);
    }

}
