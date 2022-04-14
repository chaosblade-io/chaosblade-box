package com.alibaba.chaosblade.box.dao.mapper.type;

import com.alibaba.chaosblade.box.dao.model.ExpertiseRunTimeInfo;

/**
 * @author haibin
 *
 *
 */
public class ExpertiseRunTimeInfoTypeHandler extends BaseFastJsonTypeHandler<ExpertiseRunTimeInfo> {
    @Override
    public Class<ExpertiseRunTimeInfo> getObjectClass() {
        return ExpertiseRunTimeInfo.class;
    }
}
