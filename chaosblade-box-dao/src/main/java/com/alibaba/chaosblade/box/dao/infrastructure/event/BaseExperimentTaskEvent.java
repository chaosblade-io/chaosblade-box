package com.alibaba.chaosblade.box.dao.infrastructure.event;

import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import lombok.Getter;

/**
 * @author haibin
 *
 *
 */

@Getter
public class BaseExperimentTaskEvent extends BaseChaosEvent {

    private ChaosUser user;

    private String experimentTaskId;

    public BaseExperimentTaskEvent(ChaosUser user, String experimentTaskId) {
        this.user = user;
        this.experimentTaskId = experimentTaskId;
    }

}
