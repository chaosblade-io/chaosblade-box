package com.alibaba.chaosblade.box.common.experiment.activity.execute;

/**
 * @author haibin.lhb
 *
 *
 */
public interface TaskQueueLifecycle {

    /**
     * 初始化
     */
    public void init();

    /**
     * 关闭queue
     */
    public void close();
}
