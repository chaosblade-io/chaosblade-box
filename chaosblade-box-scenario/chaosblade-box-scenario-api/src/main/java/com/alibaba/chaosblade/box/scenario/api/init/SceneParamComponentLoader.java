package com.alibaba.chaosblade.box.scenario.api.init;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.chaosblade.box.common.utils.JsonUtils;
import com.alibaba.chaosblade.box.dao.model.SceneDO;
import com.alibaba.chaosblade.box.dao.model.SceneParamDO;
import com.alibaba.chaosblade.box.dao.repository.SceneParamRepository;
import com.alibaba.chaosblade.box.dao.repository.SceneRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author yefei
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SceneParamComponentLoader implements InitializingBean {

    @Autowired
    private SceneRepository sceneRepository;

    @Autowired
    private SceneParamRepository sceneParamRepository;

    @Override
    public void afterPropertiesSet() {
        InputStream stream = ResourceUtil.getStream("scene_param_component.json");

        List<Scene> scenes = JsonUtils.readValue(new TypeReference<List<Scene>>() {
        }, IoUtil.readBytes(stream));

        if (CollUtil.isEmpty(scenes)) {
            return;
        }

        for (Scene scene : scenes) {
            List<SceneDO> sceneDOS = sceneRepository.selectByCode(scene.getSceneCode());

            sceneDOS.forEach(sceneDO -> {
                List<SceneParam> sceneParams = scene.getSceneParams();
                if (CollUtil.isNotEmpty(sceneParams)) {
                    Map<String, SceneParam> map = sceneParams.stream().collect(Collectors.toMap(SceneParam::getName, u -> u));
                    List<SceneParamDO> sceneParamDOS = sceneParamRepository
                            .selectList(SceneParamDO.builder().sceneId(sceneDO.getId()).build());

                    sceneParamDOS.forEach(sceneParamDO -> {
                        SceneParam sceneParam = map.get(sceneParamDO.getParamName());
                        if (sceneParam != null && StrUtil.isEmpty(sceneParamDO.getComponent())) {
                            sceneParamRepository.updateByPrimaryKey(sceneParamDO.getId(),
                                    SceneParamDO.builder()
                                            .component(sceneParam.getComponent())
                                            .build());
                        }
                    });
                }
            });
        }
    }
}
