package com.alibaba.chaosblade.box.common.experiment.task.flow.util.concurrent;

import java.util.Map;

/**
 * @author haibin
 *
 *
 */
public interface InvokeTracer {

    public void addTrace(String key, String value);

    public String getTrace(String key);

    public Map<String, String> getCopy();

    public void putContext(Map<String, String> context);

    public void removeTrace(String key);

    public void clear();

}
