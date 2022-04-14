package com.alibaba.chaosblade.box.service;


import com.alibaba.chaosblade.box.common.app.sdk.scope.Scope;
import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import com.alibaba.chaosblade.box.common.infrastructure.domain.scope.ScopeQuery;
import com.alibaba.chaosblade.box.dao.model.base.PageableQueryWrapper;
import com.alibaba.chaosblade.box.dao.model.base.PageableResponse;

import java.util.List;

/**
 * Service definition for host,device and etc.
 *
 * @author sunju
 *
 */
public interface ScopeService {

    /**
     * Query all alive devices.
     *
     * @param query query condition
     * @param <T> device type for internal and cloud
     * @return all alive devices
     *
     */
    <T extends Scope> List<T> queryAliveScopes(ScopeQuery query);

    <T extends Scope, Q extends ScopeQuery> PageableResponse<T> queryAliveScopesByPage(ChaosUser user, PageableQueryWrapper<Q> query, String nameSpace);
}
