package com.alibaba.chaosblade.box.common.experiment.task.flow.step;

/**
 * @author haibin
 *
 *
 */
public interface OnceInvoke<Re,Rs> {

    public Rs invoke(Re re);
}
