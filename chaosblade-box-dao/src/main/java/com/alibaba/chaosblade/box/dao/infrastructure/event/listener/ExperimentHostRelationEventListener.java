package com.alibaba.chaosblade.box.dao.infrastructure.event.listener;

import com.alibaba.chaosblade.box.common.common.enums.StateEnum;
import com.alibaba.chaosblade.box.common.infrastructure.util.concurrent.ThreadPool;
import com.alibaba.chaosblade.box.dao.infrastructure.event.BaseChaosEvent;
import com.alibaba.chaosblade.box.dao.infrastructure.event.BaseChaosEventListener;
import com.alibaba.chaosblade.box.dao.infrastructure.event.ExperimentTaskFinishedEvent;
import com.alibaba.chaosblade.box.dao.repository.ExperimentHostRelationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author haibin.lhb
 *
 *
 */
@Component
public class ExperimentHostRelationEventListener extends BaseChaosEventListener {

    @Autowired
    private ExperimentHostRelationRepository experimentHostRelationRepository;

    @Autowired
    private ThreadPool threadPool;

    @Override
    public boolean support(BaseChaosEvent event) {
        return event instanceof ExperimentTaskFinishedEvent;
    }

    @Override
    public void onChaosEvent(BaseChaosEvent event) {
        threadPool.executor("host-relation-update").execute(new Runnable() {
            @Override
            public void run() {
                String experimentTaskId = ((ExperimentTaskFinishedEvent)event).getExperimentTaskDO().getTaskId();
                StateEnum stateEnum = StateEnum.FINISHED;
                experimentHostRelationRepository.updateTaskState(experimentTaskId, stateEnum);
            }
        });
    }
}
