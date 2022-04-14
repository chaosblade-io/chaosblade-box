package com.alibaba.chaosblade.box.service;

import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.common.common.domain.experiment.ExperimentRunResult;
import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.flow.ExperimentActivityInfo;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.request.*;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.response.ExperimentInit;
import com.alibaba.chaosblade.box.common.infrastructure.exception.ChaosException;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.request.ExperimentCreateRequest;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.request.ExperimentDefinitionRequest;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.request.ExperimentUpdateRequest;

import java.util.List;
import java.util.Map;

/**
 * @author haibin.lhb
 *
 *
 */
public interface ExperimentWriteService {

    /**
     * 开始执行某个演练
     *
     * @param user                 用户信息
     * @param experimentRunRequest 运行演练的请求
     * @return running result
     * @throws ChaosException some exception
     */
    Response<ExperimentRunResult> runExperiment(ChaosUser user, ExperimentRunRequest experimentRunRequest)
        throws ChaosException;

    /**
     * 创建演练
     *
     * @param experimentCreateRequest
     * @return
     * @throws ChaosException
     */
    Response<String> createExperiment(ExperimentCreateRequest experimentCreateRequest)
        throws ChaosException;

    /**
     * 更新演练定义
     *
     * @param experimentUpdateRequest request
     * @return return TRUE if update success, otherwise return FALSE
     * @throws ChaosException
     */
    Response<Boolean> updateExperiment(ExperimentUpdateRequest experimentUpdateRequest)
        throws ChaosException;

    /**
     * 更新演练基本信息
     *
     * @param user
     * @param experimentUpdateRequest
     * @return
     * @throws ChaosException
     */
    Response<Boolean> updateExperimentBasicInfo(ChaosUser user, ExperimentUpdateRequest experimentUpdateRequest)
        throws ChaosException;

    /**
     * 克隆演练
     *
     * @param experimentCloneRequest cloneRequest
     * @return 新的演练ID
     */
    Response<String> cloneExperiment(ExperimentCloneRequest experimentCloneRequest);

    /**
     * 删除一个演练
     *
     * @param experimentDeleteRequest 操作人员
     * @return
     */
    Response<Void> deleteExperiment(ExperimentDeleteRequest experimentDeleteRequest);

    /**
     * 更新流程定义
     *
     * @param flowDefinitionCreateRequest
     * @return
     */
    Response<Void> updateExperimentDefinition(ExperimentDefinitionRequest flowDefinitionCreateRequest);

    /**
     * 根据小程序code初始化流程
     *
     * @param initMiniFlowRequest
     * @return
     */
    Response<Map<String, List<ExperimentActivityInfo>>> initMiniFlowByAppCode(
        InitMiniFlowRequest initMiniFlowRequest);

    /**
     * openApi使用，更新演练内容
     * 根据入参，判断并更新演练基本信息、演练定义或者全部更新
     *
     * @param updateRequest
     * @return
     */
    Response<Boolean> updateExperimentForOpenApi(ExperimentUpdateRequest updateRequest);

    /**
     * 预校验目标机器环境
     * @param user
     * @param experimentRunRequest
     * @return
     * @throws ChaosException
     */
    Response<ExperimentRunResult> preCheckExperiment(ChaosUser user, ExperimentRunRequest experimentRunRequest)
            throws ChaosException;

    /**
     * 根据appCode初始化
     * @param initMiniFlowRequest
     * @return
     */
    Response<ExperimentInit> initExperimentByAppCode(
            InitMiniFlowRequest initMiniFlowRequest);

    /**
     * 更新演练机器
     * @param experimentHostUpdateRequest
     * @return
     * @throws ChaosException
     */
    Response<Boolean> updateExperimentHost(ExperimentHostUpdateRequest experimentHostUpdateRequest)
            throws ChaosException;


}
