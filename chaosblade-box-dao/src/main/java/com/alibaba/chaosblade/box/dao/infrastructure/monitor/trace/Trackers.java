package com.alibaba.chaosblade.box.dao.infrastructure.monitor.trace;

import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import com.alibaba.chaosblade.box.common.infrastructure.constant.ChangelogTypes;
import com.alibaba.chaosblade.box.common.infrastructure.util.ClockUtil;
import com.alibaba.chaosblade.box.dao.model.ChangelogDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.AbortPolicy;
import java.util.concurrent.TimeUnit;

/**
 * @author sunju
 *
 */
@Slf4j
@Component
public class Trackers implements DisposableBean, InitializingBean {

    @Autowired
    private List<ChangelogTracker> changelogTrackers;

    private final ThreadPoolExecutor threadPool = new ThreadPoolExecutor(
        1,
        10,
        30L,
        TimeUnit.SECONDS,
        new LinkedBlockingQueue<>(200),
        new CustomizableThreadFactory("changelog-pool-thread-"),
        new AbortPolicy()
    );

    private void track(ChangelogDO changelogDO) {
        threadPool.submit(() -> {
            for (ChangelogTracker tracker : changelogTrackers) {
                try {
                    if (tracker.support(changelogDO.getTargetType())) {
                        tracker.track(changelogDO);
                        break;
                    }
                } catch (Exception e) {
                    log.error("Track failed.", e);
                }
            }
        });
    }

    public void track(ChangelogTypes.ChangeActionType changeActionType, ChangelogTypes.ChangeOperatorType operatorType,
                      String operatorId, ChangelogTypes.ChangeTargetType changeTargetType, String targetId,
                      ChangelogTypes.ChangePropertyType changePropertyType, String propertyId, String description) {
        ChangelogDOBuilder builder = ChangelogDOBuilder.aChangelogDO().withChangeType(changeActionType.getName())
            .withTypeDescription(changeActionType.getDesc())
            .withTargetDescription(changeTargetType.getDesc())
            .withOperatorType(operatorType.getName())
            .withOperatorDescription(operatorType.getDesc())
            .withOperatorId(operatorId)
            .withTargetType(changeTargetType.getName())
            .withTargetId(targetId)
            .withChangeDescription(description)
            .withGmtCreate(ClockUtil.now());
        if (changePropertyType != null) {
            builder.withPropertyType(changePropertyType.getName())
                .withPropertyChangeType(changePropertyType.getName())
                .withPropertyId(propertyId);
        }
        track(builder.build());
    }

    public void trackExperimentOperation(ChangelogTypes.ChangeActionType changeActionType,
                                         ChangelogTypes.ChangeOperatorType operatorType,
                                         ChaosUser chaosUser, String experimentId, String description) {
        track(changeActionType, operatorType, chaosUser.getCurrentUserId(), ChangelogTypes.ChangeTargetType.EXPERIMENT, experimentId,
            null, null, description);
    }

    public void trackExperimentTaskOperation(ChangelogTypes.ChangeActionType changeActionType,
                                             ChangelogTypes.ChangeOperatorType operatorType, ChaosUser chaosUser, String experimentId,
                                             String experimentTaskId) {
        track(changeActionType, operatorType, chaosUser.getCurrentUserId(), ChangelogTypes.ChangeTargetType.EXPERIMENT, experimentId,
            ChangelogTypes.ChangePropertyType.EXPERIMENT_TASK, experimentTaskId, null);
    }

    @Override
    public void destroy() throws Exception {
        threadPool.shutdown();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        AnnotationAwareOrderComparator.sort(changelogTrackers);
    }

}
