package com.alibaba.chaosblade.box.common.infrastructure.metric;

import com.alibaba.chaosblade.box.common.app.sdk.scope.Host;
import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author haibin
 *
 *
 */
@Data
public class MetricRequest implements Serializable {

    private String userId;

    private String namespace;

    private String key;

    private Long from;

    private Long to;

    private Host host;

    private String expId;

    private Map<String, Object> attributes = new HashMap<>();

    public void merge(Map<String, Object> contextData) {
        if (contextData != null) {
            attributes.putAll(contextData);
        }
    }

    public Object getAttribute(String key) {
        if (attributes == null) { return null; }
        return attributes.get(key);
    }

    public void putAttribute(String key, Object value) {
        if (key == null || value == null) { return; }
        if (attributes == null) {
            attributes = new HashMap<>();
        }
        attributes.put(key, value);
    }
}
