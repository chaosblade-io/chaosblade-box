package com.alibaba.chaosblade.box.dao.infrastructure.event;

import com.alibaba.chaosblade.box.dao.model.ExpertiseDO;
import lombok.Getter;

/**
 * @author haibin
 *
 *
 */
@Getter
public class ExpertiseCreateEvent extends BaseExpertiseEvent {

    public ExpertiseCreateEvent(ExpertiseDO expertiseDO) {
        super(expertiseDO);
    }
}
