package com.alibaba.chaosblade.box.dao.infrastructure.app.function;

import com.alibaba.chaosblade.box.dao.model.SceneFunctionDO;
import org.springframework.stereotype.Component;

/**
 * @author haibin.lhb
 *
 * 
 */
@Component
public class DefaultSceneFunctionSyncInterceptor implements SceneFunctionSyncInterceptor {
    @Override
    public boolean skipSyncSceneFunction(SceneFunctionDO sceneFunctionDO) {
        return false;
    }
}
