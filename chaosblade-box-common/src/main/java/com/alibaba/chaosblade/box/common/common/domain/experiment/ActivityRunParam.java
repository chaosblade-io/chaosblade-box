package com.alibaba.chaosblade.box.common.common.domain.experiment;

import com.alibaba.chaosblade.box.common.app.sdk.scope.Host;
import com.alibaba.chaosblade.box.common.common.domain.definition.ExperimentNodeArgumentsDefinition;
import lombok.Data;

import java.util.List;

/**
 * @author haibin
 *
 *
 */
@Data
public class ActivityRunParam {

    private String activityId;

    ExperimentNodeArgumentsDefinition arguments;

    List<Host> scope;

    /**
     * 当前节点失败后是否立刻终止演练,只在非手动推进才生效,true 为开启，false为关闭,
     */
    boolean interruptedIfFailed = false;

    /**
     * 失败容忍度,当下面的机器或者子任务运行失败占比超过指定值,当前节点才会认定为失败,值在[0-100]
     */
    int failedTolerance = 0;

    Boolean needUserCheck = null;
}
