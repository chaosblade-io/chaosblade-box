package com.alibaba.chaosblade.box.dao.infrastructure.app.function;

import com.alibaba.chaosblade.box.common.infrastructure.exception.ChaosException;
import com.alibaba.chaosblade.box.dao.model.SceneDO;

/**
 * @author sunju
 *
 */
public interface SceneSynchronizer {

    void syncSceneFunctions(SceneDO scene) throws ChaosException;

}
