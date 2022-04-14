package com.alibaba.chaosblade.box.dao.infrastructure.event;

import com.alibaba.chaosblade.box.dao.model.ExpertiseDO;
import lombok.Getter;

/**
 * @author haibin
 *
 *
 */
@Getter
public class BaseExpertiseEvent extends BaseChaosEvent {

    private ExpertiseDO expertiseDO;

    public BaseExpertiseEvent(ExpertiseDO expertiseDO) {
        this.expertiseDO = expertiseDO;
    }
}
