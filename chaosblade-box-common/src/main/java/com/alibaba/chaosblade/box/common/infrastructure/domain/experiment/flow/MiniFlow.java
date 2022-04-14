package com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.flow;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 演练微流程,一个微流程只能对应一个故障注入场景
 *
 * @author haibin
 *
 *
 */
@Data
public class MiniFlow {

    /**
     * 前端界面的ID
     */
    @JSONField(name = "id")
    private String fontId;

    /**
     * 微流程名字
     */
    private String name;

    /**
     * 微流程Id
     */
    private String flowId;

    /**
     * 微流程顺序
     */
    private Integer order;

    /**
     * 微流程准备环节
     */
    private List<ExperimentActivityInfo> prepare = new ArrayList<>();

    /**
     * 微流程注入环节
     */
    private List<ExperimentActivityInfo> attack = new ArrayList<>();

    /**
     * 微流程校验环节
     */
    private List<ExperimentActivityInfo> check = new ArrayList<>();

    /**
     * 微流程恢复环节
     */
    private List<ExperimentActivityInfo> recover = new ArrayList<>();

    /**
     * 组件是否必须
     */
    private Boolean required;

}
