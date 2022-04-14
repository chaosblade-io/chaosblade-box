package com.alibaba.chaosblade.box.dao.infrastructure.app.function;

import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import com.alibaba.chaosblade.box.dao.model.SceneFunctionDO;

/**
 * @author haibin
 *
 *
 */
public interface UserSceneFunctionFilter {

    /**
     * 用户是否可以查询或者操作该该小程序
     *
     * @param chaosUser
     * @param sceneFunctionDO
     * @return
     */
    public boolean filter(ChaosUser chaosUser, SceneFunctionDO sceneFunctionDO);
}
