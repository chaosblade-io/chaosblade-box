package com.alibaba.chaosblade.box.service.command.scope;

import com.alibaba.chaosblade.box.dao.model.DeviceDO;
import com.alibaba.chaosblade.box.service.model.scope.ScopeInfo;

/**
 * @author haibin.lhb
 *
 * 
 */
public interface ScopeInfoQueryInterceptor {

    public void doQuery(ScopeInfo scopeInfo, DeviceDO deviceDO);
}
