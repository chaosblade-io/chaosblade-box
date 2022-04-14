package com.alibaba.chaosblade.box.dao.infrastructure.experiment.task;

import com.alibaba.chaosblade.box.dao.infrastructure.event.ChaosEventDispatcher;
import com.alibaba.chaosblade.box.dao.infrastructure.manager.MiniAppTaskManager;
import com.alibaba.chaosblade.box.dao.model.ActivityTaskDO;
import com.alibaba.chaosblade.box.dao.repository.ActivityTaskRepository;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author haibin.lhb
 *
 *
 */

public class ActivityTaskDomainWrapper {

    private static Logger logger = LoggerFactory.getLogger(ActivityTaskDomainWrapper.class);

    public ActivityTaskDomainWrapper(ActivityTaskDO activityTaskDO) {
        this.activityTaskDO = activityTaskDO;
    }

    public void setActivityTaskDO(ActivityTaskDO activityTaskDO) {
        this.activityTaskDO = activityTaskDO;
    }

    @Getter
    private ActivityTaskDO activityTaskDO;

    @Autowired
    private ActivityTaskRepository activityTaskRepository;

    @Autowired
    private MiniAppTaskManager miniAppTaskManager;

    @Autowired
    private ChaosEventDispatcher chaosEventDispatcher;

}
