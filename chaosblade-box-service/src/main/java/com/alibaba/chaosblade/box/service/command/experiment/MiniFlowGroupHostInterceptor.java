package com.alibaba.chaosblade.box.service.command.experiment;


import com.alibaba.chaosblade.box.common.app.sdk.scope.Host;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author sunpeng
 *
 *
 */
public interface MiniFlowGroupHostInterceptor {

    /**
     * 校验host是否存在
     * @param host
     */
    default void check(Host host) {}

    /**
     * 批量校验
     * @param list
     */
    default List<String> batchCheck(List<String> list, Set<String> appId,Set<String> groupName) {
        return new ArrayList<>();
    }

}
