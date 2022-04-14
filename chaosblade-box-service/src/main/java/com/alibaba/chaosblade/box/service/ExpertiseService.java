package com.alibaba.chaosblade.box.service;


import com.alibaba.chaosblade.box.common.common.domain.PageQueryResponse;
import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.common.common.domain.experiment.BaseExperimentRequest;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.request.ExperimentExpertiseQueryRequest;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.request.ExperimentFlowInitByExpertiseRequest;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.response.ExperimentFlowInfo;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.response.ExperimentInfo;
import com.alibaba.chaosblade.box.service.model.expertise.*;

/**
 * @author haibin
 *
 *
 */
public interface ExpertiseService {

    /**
     * 创建经验
     *
     * @param experimentExpertiseCreateRequest
     * @return 经验Id
     */
    public Response<String> createExpertise(ExperimentExpertiseCreateRequest experimentExpertiseCreateRequest);

    /**
     * 转换演练到经验库
     *
     * @param baseExperimentRequest
     * @return
     */
    public Response<String> convertExperimentToExpertise(BaseExperimentRequest baseExperimentRequest);

    /**
     * 禁用经验
     *
     * @param experimentExpertiseQueryRequest
     * @return
     */
    public Response disableExpertise(ExperimentExpertiseQueryRequest experimentExpertiseQueryRequest);

    /**
     * 启用经验
     *
     * @param experimentExpertiseQueryRequest
     * @return
     */
    public Response enableExpertise(ExperimentExpertiseQueryRequest experimentExpertiseQueryRequest);

    /**
     * 更新经验
     *
     * @param experimentExpertiseUpdateRequest
     * @return
     */
    public Response<String> updateExpertise(ExperimentExpertiseUpdateRequest experimentExpertiseUpdateRequest);

    /**
     * 删除经验
     *
     * @param experimentExpertiseQueryRequest
     * @return
     */
    public Response deleteExpertise(ExperimentExpertiseQueryRequest experimentExpertiseQueryRequest);

    /**
     * clone
     *
     * @param expertiseCloneRequest
     * @return
     */
    public Response<ExpertiseInfo> cloneExpertise(ExpertiseCloneRequest expertiseCloneRequest);

    /**
     * 查询经验详情
     *
     * @param experimentExpertiseQueryRequest
     * @return
     */
    public Response<ExpertiseInfo> queryExpertiseDetails(
        ExperimentExpertiseQueryRequest experimentExpertiseQueryRequest);

    /**
     * 用户查询经验列表
     *
     * @param expertiseSearchRequest
     * @return
     */
    Response<PageQueryResponse<ExpertiseView>> searchExpertise(
        ExpertiseSearchRequest expertiseSearchRequest);

    /**
     * 管理员查询经验列表
     *
     * @param expertiseSearchRequest
     * @return
     */
    Response<PageQueryResponse<AdminExpertiseView>> listExpertise(
        ExpertiseSearchRequest expertiseSearchRequest);

    /**
     * 根据模板初始化演练信息
     *
     * @param experimentExpertiseQueryRequest
     * @return
     */
    public Response<ExperimentFlowInfo> initFlowByExpertise(
        ExperimentFlowInitByExpertiseRequest experimentExpertiseQueryRequest);

    /**
     * 根据演练初始化完整的经验
     *
     * @param experimentExpertiseQueryRequest
     * @return
     */
    Response<ExperimentInfo> initExperimentByExpertise(
        ExperimentFlowInitByExpertiseRequest experimentExpertiseQueryRequest);
}
