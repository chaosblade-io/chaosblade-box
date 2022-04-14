package com.alibaba.chaosblade.box.dao.infrastructure.event;

import com.alibaba.chaosblade.box.dao.model.ActivityTaskDO;
import lombok.Getter;

/**
 * @author haibin
 *
 *
 */
@Getter
public class ActivityTaskFinishedEvent extends BaseChaosEvent {

    private ActivityTaskDO activityTaskDO;

    public ActivityTaskFinishedEvent(ActivityTaskDO activityTaskDO) {
        this.activityTaskDO = activityTaskDO;
    }

}
