package com.alibaba.chaosblade.box.dao.mapper.type;

import com.alibaba.chaosblade.box.common.infrastructure.domain.Hosts;

/**
 * @author haibin
 * 
 *
 */
public class HostsTypeHandler extends BaseFastJsonTypeHandler<Hosts> {
    @Override
    public Class<Hosts> getObjectClass() {
        return Hosts.class;
    }
}
