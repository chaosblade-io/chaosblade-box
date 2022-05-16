package com.alibaba.chaosblade.box.dao.infrastructure.app.function;

import com.alibaba.chaosblade.box.common.common.domain.chaosblade.ChaosBladeMetaData;
import com.alibaba.chaosblade.box.common.infrastructure.util.CollectionUtil;
import com.alibaba.chaosblade.box.dao.infrastructure.app.function.SceneFunctionDescription;
import com.alibaba.chaosblade.box.dao.infrastructure.app.function.SceneFunctionDescriptionMessageLoadFactory;
import com.alibaba.chaosblade.box.dao.model.SceneFunctionDO;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author camix
 */
@Component
public class SceneFunctionNameParser {

    @Autowired
    private SceneFunctionDescriptionMessageLoadFactory sceneFunctionDescriptionMessageLoadFactory;

    public void parseSceneFunction(List<SceneFunctionDO> functions) {
        if(CollectionUtil.isNullOrEmpty(functions)) {
            return;
        }
        functions.forEach(sceneFunctionDO -> {
            String enName = sceneFunctionDO.getName();
            String functionName = ChaosBladeMetaData.getInstance().getFunctionNameParser(enName);
            sceneFunctionDO.setName(functionName);
        });
    }

    public void parseSceneFunctionOne(SceneFunctionDO sceneFunctionDO) {
        if(sceneFunctionDO == null) {
            return;
        }

        String enName = sceneFunctionDO.getName();
        String functionName = ChaosBladeMetaData.getInstance().getFunctionNameParser(enName);
        sceneFunctionDO.setName(functionName);

    }


}
