package com.alibaba.chaosblade.box.dao.infrastructure.event;

import com.alibaba.chaosblade.box.dao.model.ExpertiseDO;
import lombok.Getter;

/**
 * @author haibin
 *
 *
 */

@Getter
public class ExpertiseUpdateEvent extends BaseExpertiseEvent {

    public ExpertiseUpdateEvent(ExpertiseDO expertiseDO) {
        super(expertiseDO);
    }
}
