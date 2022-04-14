package com.alibaba.chaosblade.box.common.experiment.task.flow.step;

import com.alibaba.chaosblade.box.common.experiment.task.flow.util.NamedType;

/**
 * <p>
 * all nodes in experiment we call them "step",so step has two types,
 * one type is phase,like 'prepare,attack,check,recovery',it can't attack directly,
 * Another type is called activity,it represents the steps that can be performed,
 * There are many ways to implement activities，like PluginActivity,ComponentActivity and so on
 * </p>
 *
 * @author haibin
 *
 *
 */
public interface Step {

    enum StepType implements NamedType {

        /**
         * phase step
         */
        PHASE,
        /**
         * activity step
         */
        ACTIVITY
    }

    /**
     * unique id for step
     *
     * @return
     */
    public String getId();

    /**
     * stepType,phase or activity
     *
     * @return stepType {@link StepType}
     */
    public StepType getStepType();

}
