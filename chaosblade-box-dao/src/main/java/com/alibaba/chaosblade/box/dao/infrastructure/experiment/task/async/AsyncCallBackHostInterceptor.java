package com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.async;

import com.alibaba.chaosblade.box.common.app.sdk.scope.Host;

/**
 * @author sunpeng
 *
 *
 */
public interface AsyncCallBackHostInterceptor {

    /**
     * 补充host信息
     * @param host
     */
    void fillHostInfo(Host host);

}
