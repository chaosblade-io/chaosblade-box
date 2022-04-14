package com.alibaba.chaosblade.box.common.infrastructure.monitor.log;

/**
 * @author haibin
 *
 *
 */
public interface IRecord<T extends RecordObject> {

    /**
     * 记录
     *
     * @param recordObject
     */
    public void record(T recordObject);

}
