package com.alibaba.chaosblade.box.dao.infrastructure.event;


import com.alibaba.chaosblade.box.dao.model.ExpertiseDO;

/**
 * @author haibin
 *
 * 
 */
public class ExpertiseDeletedEvent extends BaseExpertiseEvent {
    public ExpertiseDeletedEvent(ExpertiseDO expertiseDO) {
        super(expertiseDO);
    }
}
