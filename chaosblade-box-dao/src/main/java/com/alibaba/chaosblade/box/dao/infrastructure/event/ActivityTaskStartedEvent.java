package com.alibaba.chaosblade.box.dao.infrastructure.event;

import com.alibaba.chaosblade.box.dao.model.ActivityTaskDO;
import lombok.Getter;

/**
 * @author haibin
 *
 *
 */
@Getter
public class ActivityTaskStartedEvent extends BaseChaosEvent {

    private ActivityTaskDO activityTaskDO;

    public ActivityTaskStartedEvent(ActivityTaskDO activityTaskDO) {
        this.activityTaskDO = activityTaskDO;
    }
}
