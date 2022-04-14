package com.alibaba.chaosblade.box.dao.mapper.type;


import com.alibaba.chaosblade.box.common.common.domain.experiment.ActivityRunParam;

/**
 * @author haibin
 * 
 * 
 */
public class ActivityRunParamDefinitionTypeHandler extends BaseFastJsonTypeHandler<ActivityRunParam> {
    @Override
    public Class<ActivityRunParam> getObjectClass() {
        return ActivityRunParam.class;
    }
}
