package com.alibaba.chaosblade.box.scenario.api.init;

import lombok.Data;

import java.util.List;

/**
 * @author yefei
 */
@Data
public class Scene {

    private String sceneCode;

    private List<SceneParam> sceneParams;
}
