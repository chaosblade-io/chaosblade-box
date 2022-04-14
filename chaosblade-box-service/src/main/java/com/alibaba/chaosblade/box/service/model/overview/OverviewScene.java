package com.alibaba.chaosblade.box.service.model.overview;

import lombok.Data;

/**
 * @author sunpeng
 *
 *
 */
@Data
public class OverviewScene {

    /**
     * 场景名称
     */
    private String name;

    /**
     * 场景小程序编码
     */
    private String appCode;

    /**
     * 场景排序
     */
    private Integer order;

    /**
     * 场景目标资源
     */
    private String sceneTarget;

    /**
     * 场景类型
     */
    private String sceneType;

    public OverviewScene(String name, String appCode, Integer order, String sceneTarget, String sceneType) {
        this.name = name;
        this.appCode = appCode;
        this.order = order;
        this.sceneTarget = sceneTarget;
        this.sceneType = sceneType;
    }

    public OverviewScene() {
    }
}
