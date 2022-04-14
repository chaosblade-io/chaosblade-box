package com.alibaba.chaosblade.box.dao.infrastructure.monitor.trace.trackers;

import com.alibaba.chaosblade.box.common.infrastructure.constant.ChangelogTypes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author jiumu
 *
 */
@Slf4j
@Component
@Order(value = Ordered.HIGHEST_PRECEDENCE)
public class SceneChangelogTracker extends BaseChangeLogTracker {

    @Override
    public boolean support(String targetType) {
        return ChangelogTypes.ChangeTargetType.SCENE.getName().equals(targetType);
    }

}
