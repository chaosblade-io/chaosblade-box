package com.alibaba.chaosblade.box.service;

import com.alibaba.chaosblade.box.common.app.sdk.scope.Host;
import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.common.common.domain.task.BaseExperimentTask;
import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import com.alibaba.chaosblade.box.common.experiment.model.AppNameAndIdPair;
import com.alibaba.chaosblade.box.dao.model.ApplicationDO;
import com.alibaba.chaosblade.box.dao.model.ApplicationDeviceDO;
import com.alibaba.chaosblade.box.dao.model.base.PageableResponse;
import com.alibaba.chaosblade.box.service.infrastructure.configuration.ApplicationConfiguration;
import com.alibaba.chaosblade.box.service.model.ExperimentApplication;
import com.alibaba.chaosblade.box.service.model.application.*;
import com.alibaba.chaosblade.box.service.model.device.CloudDevice;
import com.alibaba.chaosblade.box.service.model.experiment.ExperimentScope;

import java.util.List;
import java.util.Set;

/**
 * @author haibin
 *
 *
 */
public interface ApplicationService {

    /**
     * 分页获取用户演练概要
     *
     * @param getUserApplicationSummariesRequest
     * @return
     */
    Response<PageableResponse<UserApplicationSummary>> getUserApplicationSummaries(
        GetUserApplicationSummariesRequest getUserApplicationSummariesRequest);

    /**
     * 搜索应用
     *
     * @param userApplicationSearchRequest
     * @return
     */
    Response<List<UserApplicationSummary>> searchApplicationsForSummary(
        UserApplicationSearchRequest userApplicationSearchRequest);

    /**
     * 查询演练基本信息
     *
     * @param userApplicationQueryRequest
     * @return
     */
    Response<ApplicationBasicInfo> getApplicationBasic(
        UserApplicationQueryRequest userApplicationQueryRequest) throws Throwable;

    /**
     * 获取应用演练任务
     *
     * @param userApplicationQueryRequest
     * @return
     */
    Response<PageableResponse<BaseExperimentTask>> getApplicationExperimentTasks(
        UserApplicationQueryRequest userApplicationQueryRequest);
    
    /**
     * 停止应用下面的所有演练任务
     * @param userApplicationQueryRequest
     * @return
     */
    public Response<Integer> stopExperimentsByApplication(
        UserApplicationQueryRequest userApplicationQueryRequest);

    /**
     * 搜索演练机器
     *
     * @param applicationScopeSearchRequest
     * @return
     */
    public Response<PageableResponse<ExperimentScope>> searchApplicationHosts(
        ApplicationScopeSearchRequest applicationScopeSearchRequest);

    /**
     * 查询演练机器，演练创建时候使用
     *
     * @param pageableQueryApplicationHostRequest
     * @return
     */
    Response<PageableResponse<CloudDevice>> getScopesByApplication(ChaosUser user,
                                                                   PageableQueryApplicationHostRequest pageableQueryApplicationHostRequest);

    /**
     * 获取应用机器
     *
     * @param applicationSearchRequest
     * @return
     */
    Response<List<AppNameAndIdPair>> getUserApplicationNames(ApplicationSearchRequest applicationSearchRequest);

    /**
     * 分页获取应用机器
     *
     * @param applicationSearchRequest
     * @return
     */
    Response<PageableResponse<AppNameAndIdPair>> pageUserApplicationNames(ApplicationSearchRequest applicationSearchRequest);

    /**
     * 获取应用分组名
     *
     * @param userApplicationQueryRequest
     * @return
     */
    Response<List<String>> getGroupsByApp(UserApplicationQueryRequest userApplicationQueryRequest);

    /**
     * 根据应用分组以及IP查询机器信息
     *
     * @param applicationGroupDeviceQuery
     * @return
     */
    Response<List<Host>> getScopeByAppNameAndGroupsAndIp(
        ApplicationGroupDeviceQuery applicationGroupDeviceQuery);

    /**
     * 列举应用的配置
     *
     * @param userApplicationQueryRequest
     * @return
     */
    Response<List<ApplicationConfiguration>> listApplicationConfigurations(
        UserApplicationQueryRequest userApplicationQueryRequest);

    /**
     * 添加应用单挑配置
     *
     * @param applicationConfigurationUpdateRequest
     * @return
     */
    Response updateApplicationConfiguration(
        ApplicationConfigurationUpdateRequest applicationConfigurationUpdateRequest);

    /**
     * 搜索演练机器
     *
     * @param applicationScopeSearchRequest
     * @return
     */
    Response<PageableResponse<ApplicationDeviceDO>> pageableSearchApplicationHostsByAppIdOrAppNamePart(
            ApplicationScopeSearchRequest applicationScopeSearchRequest);

    /**
     * 查询用户应用列表 openapi
     * @param getUserApplicationSummariesRequest
     * @return
     */
    Response<PageableResponse<ApplicationDO>> pageableSearchUserApplication(
            GetUserApplicationSummariesRequest getUserApplicationSummariesRequest);

    /**
     * 更新应用下机器标签
     * @param applicationTagsUpdateRequest
     * @return
     */
    Response<Boolean> updateApplicationTag(ApplicationTagsUpdateRequest applicationTagsUpdateRequest);

    /**
     * 批量添加标签（仅添加）
     *
     * @param applicationTagsUpdateRequest
     * @return
     */
    Response<Boolean> batchAddApplicationTag(ApplicationTagsBatchUpdateRequest applicationTagsUpdateRequest);

    /**
     * 模糊搜索查询分组下所有标签
     *
     * @param applicationTagsSearchRequest
     * @return
     */
    Response<List<String>> searchDeviceTagsByAppIdAndGroupName(ApplicationTagsSearchRequest applicationTagsSearchRequest);

    /**
     * 模糊搜索查询分组下所有机器的namespace
     *
     * @param applicationTagsSearchRequest
     * @return
     */
    Response<Set<String>> searchClusterNamespacesByAppIdAndGroupName(ApplicationTagsSearchRequest applicationTagsSearchRequest);

    /**
     * 获取当前应用分组下存活机器数量
     * @param user
     * @param applicationHostsCountRequest
     * @return
     */
    Response<ApplicationHostsCount> countScopesByApplication(ChaosUser user,
                                                             ApplicationHostsCountRequest applicationHostsCountRequest);

    /**
     * 根据configurationId  查询机器
     * @param applicationGroupDeviceQuery
     * @return
     */
    Response<List<Host>> getScopeByConfigurationIds(ApplicationGroupDeviceQuery applicationGroupDeviceQuery);

    /**
     * 获取正在运行中的演练应用
     *
     * @param mainUserId
     * @param namespace
     * @return
     */
    Response<List<ExperimentApplication>> findApplicationsInExperimentForVision(String mainUserId, String namespace);

    /**
     * 根据app configurationId获取机器信息
     * @param appConfigurationId
     * @return
     */
    Response<Host> getScopeByAppConfigurationId(String appConfigurationId);

}
