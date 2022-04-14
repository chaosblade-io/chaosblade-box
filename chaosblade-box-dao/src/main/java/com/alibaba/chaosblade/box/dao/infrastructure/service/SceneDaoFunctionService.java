package com.alibaba.chaosblade.box.dao.infrastructure.service;

import com.alibaba.chaosblade.box.common.infrastructure.exception.ChaosException;
import com.alibaba.chaosblade.box.dao.model.SceneFunctionDO;

import java.util.List;

public interface SceneDaoFunctionService {
    void rebuildChaosBladeFunctions(List<SceneFunctionDO> sceneFunctionDOS, boolean force) throws ChaosException;

    SceneFunctionDO querySceneFunctionByCode(String code);
}
