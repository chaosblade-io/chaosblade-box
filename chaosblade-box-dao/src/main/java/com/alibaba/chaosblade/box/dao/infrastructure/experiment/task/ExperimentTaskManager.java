package com.alibaba.chaosblade.box.dao.infrastructure.experiment.task;


import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.common.common.domain.task.BaseExperimentTask;
import com.alibaba.chaosblade.box.common.common.domain.task.ExperimentTaskStopOption;
import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import com.alibaba.chaosblade.box.common.experiment.clientobject.ExperimentStat;
import com.alibaba.chaosblade.box.common.experiment.clientobject.ExperimentTaskStat;
import com.alibaba.chaosblade.box.common.experiment.request.ExperimentTaskStatRequest;
import com.alibaba.chaosblade.box.common.experiment.request.UserExperimentTaskStatRequest;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.request.ExperimentTaskCreateRequest;
import com.alibaba.chaosblade.box.dao.model.ExperimentTaskDO;

/**
 * @author haibin
 *
 * 
 */
public interface ExperimentTaskManager {

    /**
     * 创建演练任务
     *
     * @param experimentTaskCreateRequest 演练请求
     * @return 返回演练任务Id
     */
    Response<ExperimentTaskDO> createTask(ExperimentTaskCreateRequest experimentTaskCreateRequest);

    /**
     * 停止演练任务
     *
     * @param experimentTaskId
     * @param experimentTaskStopOption
     * @return
     */
    Response<Void> stopTask(ChaosUser user, String experimentTaskId, ExperimentTaskStopOption experimentTaskStopOption);

    /**
     * 统计用户所有实验的运行数据
     *
     * @param userExperimentTaskStatRequest request for statistics
     * @return result for statistics
     */
    Response<ExperimentStat> getUserExperimentTaskStatistics(
        UserExperimentTaskStatRequest userExperimentTaskStatRequest);

    /**
     * 统计任务运行信息
     *
     * @param experimentTaskStatRequest
     * @return
     */
    Response<ExperimentTaskStat> doExperimentTaskStatistic(ExperimentTaskStatRequest experimentTaskStatRequest);

    /**
     * 查询演练任务基本信息
     *
     * @param experimentTaskId
     * @return
     */
    Response<BaseExperimentTask> getBaseExperimentTaskByTaskId(String experimentTaskId);

}
