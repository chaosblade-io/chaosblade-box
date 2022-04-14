package com.alibaba.chaosblade.box.dao.infrastructure.experiment.task;

import com.alibaba.chaosblade.box.common.app.sdk.constants.PhaseType;
import com.alibaba.chaosblade.box.dao.model.ExperimentActivityDO;

import java.util.List;
import java.util.Optional;

/**
 * @author haibin
 *
 *
 */
public interface ActivityQueryManager {

    /**
     * 查询然后组合活动
     *
     * @param experimentId 演练ID
     * @return
     */
    public List<ExperimentActivityDO> findActivitiesByExperimentId(String experimentId);

    /**
     * 根据activityID查询活动
     *
     * @param activityId
     * @return
     */
    public Optional<ExperimentActivityDO> findActivity(String activityId);

    /**
     * 获取指定阶段下面的所有活动
     *
     * @param experimentId 演练ID
     * @param phaseType    演练阶段名
     * @return
     */
    public List<ExperimentActivityDO> findActivitiesByPhase(String experimentId, PhaseType phaseType);
}
