package com.alibaba.chaosblade.box.service.infrastructure.miniapp;


import com.alibaba.chaosblade.box.common.app.sdk.scope.Host;
import com.alibaba.chaosblade.box.common.common.annotation.ExtensionPoint;
import com.alibaba.chaosblade.box.service.model.param.ParamOptionsQueryRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * @author haibin.lhb
 *
 *
 */
@ExtensionPoint
public interface MiniAppParamOptionsProvider {

    default <T> List<T> loopInHosts(List<Host> hosts, Function<Host, List<T>> function) {
        List<T> result = new ArrayList<>();
        for (Host host : hosts) {
            result = function.apply(host);
            if (!result.isEmpty()) {
                break;
            }
        }
        return result;
    }

    /**
     * 提供参数下拉
     *
     * @param paramOptionsQueryRequest
     * @return
     */
    public List<MiniAppParamOption> provide(ParamOptionsQueryRequest paramOptionsQueryRequest);

    /**
     * 是否支持当前provider
     *
     * @param paramOptionsQueryRequest
     * @return
     */
    public boolean isSupported(ParamOptionsQueryRequest paramOptionsQueryRequest);

}
