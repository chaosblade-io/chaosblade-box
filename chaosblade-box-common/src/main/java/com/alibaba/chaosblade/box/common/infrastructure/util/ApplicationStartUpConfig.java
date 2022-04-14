package com.alibaba.chaosblade.box.common.infrastructure.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author haibin
 *
 *
 */
@Component
@Slf4j
public class ApplicationStartUpConfig {

    @Value("#{'${chaos.function.sync.type}'.split(',')}")
    private List<String> functionSyncTypes;

    public static enum SyncFunctionType {
        ChaosBlade,
        UserApp,
        ALL,
        None,
        LITMUS_CHAOS
    }

    public boolean isSync(SyncFunctionType syncFunctionType) {
        if (functionSyncTypes.contains(SyncFunctionType.ALL.name())) { return true; }
        boolean isSync = functionSyncTypes.contains(syncFunctionType.name());
        if (isSync) {
            log.info("config[chaos.function.sync.type:{}],sync function for type:{}", functionSyncTypes,
                syncFunctionType.name());
        } else {
            log.info("config[chaos.function.sync.type:{}],skip sync function for type:{}", functionSyncTypes,
                syncFunctionType.name());
        }
        return isSync;
    }

}
