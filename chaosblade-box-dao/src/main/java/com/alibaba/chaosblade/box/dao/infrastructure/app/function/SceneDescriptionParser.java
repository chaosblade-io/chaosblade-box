package com.alibaba.chaosblade.box.dao.infrastructure.app.function;

import com.alibaba.chaosblade.box.common.infrastructure.util.CollectionUtil;
import com.alibaba.chaosblade.box.dao.model.SceneFunctionDO;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author sunpeng
 *
 *
 */
@Component
public class SceneDescriptionParser {

    @Autowired
    private SceneFunctionDescriptionMessageLoadFactory sceneFunctionDescriptionMessageLoadFactory;

    public void parseSceneDescription(List<SceneFunctionDO> functions) {
        if(CollectionUtil.isNullOrEmpty(functions)) {
            return;
        }
        functions.forEach(sceneFunctionDO -> {
            SceneFunctionDescription description =
                    sceneFunctionDescriptionMessageLoadFactory.getSceneAppCodeDescription(sceneFunctionDO.getCode());
            if(null != description) {
                sceneFunctionDO.setDescription(description.getDescription());
                return;
            }
            description = sceneFunctionDescriptionMessageLoadFactory.getSceneKeyCodeDescription(getKeyCode(sceneFunctionDO.getCode()));
            if(null != description) {
                sceneFunctionDO.setDescription(description.getDescription());
            }
        });
    }

    private String getKeyCode(String appCode) {
        if(Strings.isNullOrEmpty(appCode)) {
            return appCode;
        }
        String[] strArray = appCode.split("-");
        return strArray[strArray.length - 1];
    }



}
