package com.alibaba.chaosblade.box.dao.infrastructure.app.function;


import com.alibaba.chaosblade.box.dao.model.SceneFunctionDO;

/**
 * @author haibin.lhb
 *
 *
 */
public interface SceneFunctionSyncInterceptor {

    /**
     * 是否跳过当前场景同步
     *
     * @param sceneFunctionDO
     * @return
     */
    public boolean skipSyncSceneFunction(SceneFunctionDO sceneFunctionDO);
}
