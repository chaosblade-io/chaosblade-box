package com.alibaba.chaosblade.box.service;


import com.alibaba.chaosblade.box.common.common.domain.PageQueryResponse;
import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.common.common.domain.experiment.BaseExperimentRequest;
import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import com.alibaba.chaosblade.box.common.experiment.clientobject.ExperimentStat;
import com.alibaba.chaosblade.box.common.experiment.log.ExperimentOperationLog;
import com.alibaba.chaosblade.box.common.experiment.request.ExperimentAppRiskMessageRequest;
import com.alibaba.chaosblade.box.common.experiment.request.ExperimentPageableQueryRequest;
import com.alibaba.chaosblade.box.common.experiment.request.UserExperimentStatRequest;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.ExperimentBasicInfo;
import com.alibaba.chaosblade.box.service.model.experiment.UserExperimentPageableQueryRequest;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.response.*;
import com.alibaba.chaosblade.box.dao.model.base.PageableResponse;

import java.util.List;

/**
 * @author haibin
 *
 *
 */
public interface ExperimentReadService {

    /**
     * 检查演练是否可以运行
     *
     * @param user
     * @param experimentId
     * @return
     */
    Response checkExperimentRunnable(ChaosUser user, String experimentId);

    /**
     * 查询演练
     *
     * @param request 演练ID和namespace
     * @return experiment entity
     */
    Response<ExperimentInfo> getExperiment(BaseExperimentRequest request);

    /**
     * 统计用户演练数据，按照演练计划维度
     *
     * @param userExperimentStatRequest
     * @return
     */
    Response<ExperimentStat> getUserExperimentStatistics(UserExperimentStatRequest userExperimentStatRequest);

    /**
     * 分页查询用户演练列表
     *
     * @param userExperimentPageableQueryRequest
     * @return
     */
    Response<PageQueryResponse<UserExperiment>> searchExperiments(
        UserExperimentPageableQueryRequest userExperimentPageableQueryRequest);

    /**
     * 查询演练基本信息
     *
     * @param baseExperimentRequest
     * @return
     */
    Response<ExperimentBasicInfo> getExperimentBasicInfo(BaseExperimentRequest baseExperimentRequest);

    /**
     * 查询演练的操作日志
     *
     * @param baseExperimentRequest
     * @return
     */
    Response<PageableResponse<ExperimentOperationLog>> listExperimentOperationLogs(
        ExperimentPageableQueryRequest baseExperimentRequest);


    /**
     * 查询演练缩略信息
     * @param experimentId
     * @return
     */
    Response<ExperimentSimpleInfo> getExperimentSimpleInfoForChaos(String experimentId);

    /**
     * 获取演练中包含的小程序的风险信息
     * @param request
     * @return
     */
    Response<List<ExperimentAppRisk>> getExperimentAppRisk(ExperimentAppRiskMessageRequest request);




}
