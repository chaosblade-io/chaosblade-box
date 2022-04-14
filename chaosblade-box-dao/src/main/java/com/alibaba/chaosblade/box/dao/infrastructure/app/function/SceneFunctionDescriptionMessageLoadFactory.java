package com.alibaba.chaosblade.box.dao.infrastructure.app.function;

import com.alibaba.chaosblade.box.common.infrastructure.util.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author sunpeng
 *
 *
 */
@Slf4j
@Component
public class SceneFunctionDescriptionMessageLoadFactory implements InitializingBean {

    private static final String LOAD_PATH = "config/scene/description/scene_description.json";

    /**
     * map key为小程序的appCode
     */
    private final Map<String, SceneFunctionDescription> sceneAppCodeDescription = new HashMap<>();

    /**
     * map key为小程序类别
     */
    private final Map<String, SceneFunctionDescription> sceneKeyCodeDescription = new HashMap<>();


    public SceneFunctionDescription getSceneAppCodeDescription(String code) {
        return sceneAppCodeDescription.getOrDefault(code,null);
    }

    public SceneFunctionDescription getSceneKeyCodeDescription(String code) {
        return sceneKeyCodeDescription.getOrDefault(code,null);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            InputStream inputstream = Thread.currentThread().getContextClassLoader().getResourceAsStream(
                    LOAD_PATH);
            Type type = new TypeReference<List<SceneFunctionDescription>>() {}.getType();
            List<SceneFunctionDescription> sceneFunctionDescriptions = JSON.parseObject(inputstream, type);
            if(!CollectionUtil.isNullOrEmpty(sceneFunctionDescriptions)){
                sceneFunctionDescriptions.forEach(sceneFunctionDescription -> {
                    sceneAppCodeDescription.put(sceneFunctionDescription.getAppCode(),sceneFunctionDescription);
                    if(!Strings.isNullOrEmpty(sceneFunctionDescription.getKeyCode())) {
                        sceneKeyCodeDescription.put(sceneFunctionDescription.getKeyCode(),sceneFunctionDescription);
                    }
                });
            }
        } catch (Exception e) {
            log.error("[SceneFunctionDescriptionMessageLoad] afterPropertiesSet fail ",e);
        }
    }
}
