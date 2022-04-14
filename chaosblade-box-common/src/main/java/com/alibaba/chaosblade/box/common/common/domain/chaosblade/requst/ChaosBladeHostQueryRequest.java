package com.alibaba.chaosblade.box.common.common.domain.chaosblade.requst;

import com.alibaba.chaosblade.box.common.app.sdk.scope.Host;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author haibin
 *
 *
 */
@Getter
public class ChaosBladeHostQueryRequest {

    public ChaosBladeHostQueryRequest(Host host) {
        this.host = host;
    }

    private Host host;

    public Map<String, Object> extras = new HashMap<>();

    public Object getAttr(String key) {
        return extras.get(key);
    }

    public void addAttr(String key, Object object) {
        extras.put(key, object);
    }

}
