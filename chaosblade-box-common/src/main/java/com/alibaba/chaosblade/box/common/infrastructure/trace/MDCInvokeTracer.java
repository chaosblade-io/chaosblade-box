package com.alibaba.chaosblade.box.common.infrastructure.trace;

import com.alibaba.chaosblade.box.common.experiment.task.flow.util.concurrent.InvokeTracer;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author haibin
 *
 *
 */
@Component
public class MDCInvokeTracer implements InvokeTracer {

    @Override
    public void addTrace(String key, String value) {
        try {
            MDC.put(key, value);
        } catch (Exception ignore) {

        }
    }

    @Override
    public String getTrace(String key) {
        try {
            return MDC.get(key);
        } catch (Exception ignore) {
        }
        return null;
    }

    @Override
    public Map<String, String> getCopy() {
        try {
            return MDC.getCopyOfContextMap();
        } catch (Exception ignore) {
            return new HashMap<>();
        }
    }

    @Override
    public void putContext(Map<String, String> context) {
        try {
            if (context == null) { return; }
            MDC.setContextMap(context);
        } catch (Exception ignore) {

        }
    }

    @Override
    public void removeTrace(String key) {
        try {
            MDC.remove(key);
        } catch (Exception ignore) {
        }
    }

    @Override
    public void clear() {
        MDC.clear();
    }
}
