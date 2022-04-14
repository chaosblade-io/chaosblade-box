package com.alibaba.chaosblade.box.dao.infrastructure.app.function;

import com.alibaba.chaosblade.box.dao.model.SceneFunctionDO;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author haibin.lhb
 *
 * 
 */
public abstract class BaseSceneSynchronizer implements SceneSynchronizer {

    @Autowired
    private List<SceneFunctionSyncInterceptor> sceneFunctionSyncInterceptorList;

    protected boolean ignoreSyncSceneFunction(SceneFunctionDO sceneFunctionDO) {
        return sceneFunctionSyncInterceptorList.stream().anyMatch(
            sceneFunctionSyncInterceptor -> sceneFunctionSyncInterceptor.skipSyncSceneFunction(sceneFunctionDO));
    }

}
