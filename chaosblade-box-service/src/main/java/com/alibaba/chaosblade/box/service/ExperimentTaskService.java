package com.alibaba.chaosblade.box.service;


import com.alibaba.chaosblade.box.common.common.domain.PageQueryResponse;
import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.common.common.domain.task.BaseExperimentTask;
import com.alibaba.chaosblade.box.common.common.domain.task.BaseExperimentTaskRequest;
import com.alibaba.chaosblade.box.common.common.domain.task.ExperimentTask;
import com.alibaba.chaosblade.box.common.common.domain.task.ExperimentTaskStopOption;
import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import com.alibaba.chaosblade.box.common.experiment.clientobject.ExperimentTaskResult;
import com.alibaba.chaosblade.box.common.experiment.request.ExperimentPageableQueryRequest;
import com.alibaba.chaosblade.box.common.experiment.request.ExperimentTaskPageableQueryRequest;
import com.alibaba.chaosblade.box.common.experiment.request.ExperimentTaskRequest;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.ExperimentTaskControl;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.ExperimentTaskLog;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.ExperimentTaskPushRequest;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.ExperimentTaskSimple;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.guard.ExperimentTaskGuardsResult;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.response.ExperimentTaskSummary;
import com.alibaba.chaosblade.box.dao.model.ExperimentTaskDO;

import java.util.List;
import java.util.Map;

/**
 * @author haibin
 * 
 * 
 */
public interface ExperimentTaskService {

    /**
     * 获取演练的任务状态
     *
     * @param taskId
     * @return
     */
    Response<ExperimentTask> getExperimentTaskFullInfo(String taskId);

    Response pushExperimentTask(ExperimentTaskPushRequest experimentTaskPushRequest);

    /**
     * 查询演练任务详情
     *
     * @param taskId
     * @return
     */
    Response<ExperimentTaskSummary> getExperimentTaskSummary(String taskId);

    /**
     * 根据outerId来查询演练任务
     *
     * @param outerId
     * @return
     */
    Response<List<ExperimentTask>> getExperimentTasksByOuterId(String outerId);

    /**
     * 根据演练ID查询演练任务ID
     *
     * @param experimentId
     * @return
     */
    Response<List<ExperimentTask>> getExperimentTasksByExperimentId(String experimentId);

    /**
     * 获取演练活动的简单信息
     *
     * @param taskId
     * @return
     */
    Response<ExperimentTaskSimple> getExperimentTaskSimpleInfo(String taskId);

    /**
     * 根据演练ID获取演练任务简单信息(包含主机信息，类型信息，应用信息)
     * 不获取正在运行中的信息
     *
     * @param experimentTaskRequest
     * @return
     */
    Response<List<ExperimentTaskSimple>> getExperimentTasksSimpleByExperimentId(
        ExperimentTaskRequest experimentTaskRequest);

    /**
     * 根据演练ID统计演练任务结果
     *
     * @param experimentTaskRequest
     * @return
     */
    Response<ExperimentTaskResult> getExperimentTasksResultByExperimentId(ExperimentTaskRequest experimentTaskRequest);

    /**
     * 获取各个时间段内演练统计信息
     *
     * @param experimentTaskRequest
     * @return
     */
    Response<Map<String, Integer>> getExperimentTaskResultCount(ExperimentTaskRequest experimentTaskRequest);

    /**
     * 获取各个时间段内演练运行的次数
     *
     * @param experimentTaskRequest
     * @return
     */
    Response<Map<Long, Integer>> getExperimentTaskCount(ExperimentTaskRequest experimentTaskRequest);

    /**
     * 获取演练任务日志信息
     *
     * @param experimentTaskId
     * @return
     */
    Response<ExperimentTaskLog> getExperimentTaskLog(String experimentTaskId);

    /**
     * 获取控件列表
     *
     * @param experimentTaskRequest
     * @return
     */
    Response<ExperimentTaskControl> getControlList(ExperimentTaskRequest experimentTaskRequest);

    /**
     * 获取各个时间段内演练运行的费时
     *
     * @param experimentTaskRequest
     * @return
     */
    Response<Map<Long, Double>> getExperimentTaskCost(ExperimentTaskRequest experimentTaskRequest);

    /**
     * 停止演练任务
     *
     * @param experimentTaskId
     * @param experimentTaskStopOption
     * @return
     */
    Response<Void> stopTask(ChaosUser user, String experimentTaskId, ExperimentTaskStopOption experimentTaskStopOption);

    /**
     * 管理员停止任务
     *
     * @param user
     * @param experimentTaskId
     * @param experimentTaskStopOption
     * @return
     */
    Response<Void> adminStopTask(ChaosUser user, String experimentTaskId,
                                 ExperimentTaskStopOption experimentTaskStopOption);

    /**
     * 终止用户在一个namespace下面所有的演练任务
     *
     * @param user
     * @param baseExperimentTaskRequest
     * @return
     */
    public Response<Void> stopAllTasks(ChaosUser user, BaseExperimentTaskRequest baseExperimentTaskRequest);

    /**
     * 分页查询演练任务概要
     *
     * @param experimentPageableQueryRequest
     * @return
     */
    PageQueryResponse<BaseExperimentTask> pageableQueryExperimentTaskSummary(
        ExperimentPageableQueryRequest experimentPageableQueryRequest);

    /**
     * @param experimentId
     * @return
     */
    ExperimentTaskDO checkExperimentIsRunning(String experimentId);

    /**
     * 查询守护节点结果
     *
     * @param baseExperimentTaskRequest
     * @return
     */
    Response<ExperimentTaskGuardsResult> queryExperimentTaskGuardInfo(
        BaseExperimentTaskRequest baseExperimentTaskRequest);

    /**
     * 根据userId查询演练任务列表
     * @param experimentTaskPageableQueryRequest
     * @return
     */
    PageQueryResponse<BaseExperimentTask> pageableQueryExperimentTaskByUserId(
            ExperimentTaskPageableQueryRequest experimentTaskPageableQueryRequest);

}
