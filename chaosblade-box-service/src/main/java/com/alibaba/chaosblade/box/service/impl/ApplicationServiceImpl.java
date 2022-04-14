package com.alibaba.chaosblade.box.service.impl;

import com.alibaba.chaosblade.box.common.app.sdk.scope.Host;
import com.alibaba.chaosblade.box.common.app.sdk.scope.Scope;
import com.alibaba.chaosblade.box.common.commands.CommandBus;
import com.alibaba.chaosblade.box.common.common.domain.BaseRequest;
import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.common.common.domain.task.BaseExperimentTask;
import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import com.alibaba.chaosblade.box.common.common.enums.AppType;
import com.alibaba.chaosblade.box.common.common.enums.CommonErrorCode;
import com.alibaba.chaosblade.box.common.common.enums.DeviceOsType;
import com.alibaba.chaosblade.box.common.common.enums.ScopeTypeEnum;
import com.alibaba.chaosblade.box.common.experiment.model.AppNameAndIdPair;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.request.ExperimentTaskStopRequest;
import com.alibaba.chaosblade.box.common.infrastructure.exception.ChaosException;
import com.alibaba.chaosblade.box.common.infrastructure.util.CollectionUtil;
import com.alibaba.chaosblade.box.common.infrastructure.util.ExecutorUtil;
import com.alibaba.chaosblade.box.dao.command.task.ExperimentTaskStopCommand;
import com.alibaba.chaosblade.box.dao.model.*;
import com.alibaba.chaosblade.box.dao.model.base.PageableResponse;
import com.alibaba.chaosblade.box.dao.query.ApplicationDeviceQuery;
import com.alibaba.chaosblade.box.dao.repository.*;
import com.alibaba.chaosblade.box.service.ApplicationService;
import com.alibaba.chaosblade.box.service.infrastructure.configuration.ApplicationConfiguration;
import com.alibaba.chaosblade.box.service.infrastructure.configuration.ApplicationConfigurator;
import com.alibaba.chaosblade.box.service.infrastructure.convertor.*;
import com.alibaba.chaosblade.box.service.infrastructure.finder.ExperimentApplicationFinder;
import com.alibaba.chaosblade.box.service.manager.tag.TagManager;
import com.alibaba.chaosblade.box.service.model.ExperimentApplication;
import com.alibaba.chaosblade.box.service.model.application.*;
import com.alibaba.chaosblade.box.service.model.device.CloudDevice;
import com.alibaba.chaosblade.box.service.model.experiment.ExperimentScope;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.alibaba.chaosblade.box.dao.model.TagDO.TAG_TYPE_APPLICATION_HOST;

/**
 * @author haibin
 *
 *
 */
@Slf4j
@Service("applicationService")
//@Component
public class ApplicationServiceImpl implements ApplicationService {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private UserApplicationSummaryConvertor userApplicationSummaryConverter;

    @Autowired
    private ApplicationBasicInfoConvertor applicationBasicInfoConverter;

    @Autowired
    private ApplicationBaseExperimentTaskConvertor applicationBaseExperimentTaskConverter;

    @Autowired
    private ApplicationRelationRepository applicationRelationRepository;

    @Autowired
    private ApplicationGroupRepository applicationGroupRepository;

    @Autowired
    private ApplicationDeviceRepository applicationDeviceRepository;

    @Autowired
    private ApplicationDeviceToExperimentScopeConvertor applicationDeviceToExperimentScopeConverter;

    @Autowired
    private ApplicationDeviceConvertor applicationDeviceConverter;

    @Autowired
    private ApplicationConfigurator applicationConfigurator;

    @Autowired
    private ApplicationDeviceTagRepository applicationDeviceTagRepository;

    @Autowired
    private TagManager tagManager;

    @Autowired
    private ExperimentApplicationFinder experimentApplicationFinder;

    @Autowired
    private CommandBus commandBus;

    @Override
    public Response<PageableResponse<UserApplicationSummary>> getUserApplicationSummaries(
        GetUserApplicationSummariesRequest userApplicationSummariesRequest) {
        String userId = userApplicationSummariesRequest.getUserId();
        IPage<ApplicationDO> applicationDOIPage = applicationRepository.findApplicationsByUserId(userId,
            userApplicationSummariesRequest.getNamespace(),
            userApplicationSummariesRequest.isFilterDisabled(),
            userApplicationSummariesRequest.getPage(),
            userApplicationSummariesRequest.getSize());
        PageableResponse<UserApplicationSummary> pageableResponse = PageableResponse.of(
            applicationDOIPage.getCurrent(), applicationDOIPage.getSize(),
            ExecutorUtil.execute(() -> applicationDOIPage.getRecords().stream().map(
                (Function<ApplicationDO, Callable<UserApplicationSummary>>)applicationDO -> () -> userApplicationSummaryConverter
                    .convert(applicationDO)).collect(Collectors.toList()), 4),
            applicationDOIPage.getPages(),
            applicationDOIPage.getTotal());
        return Response.okWithData(pageableResponse);
    }

    @Override
    public Response<List<UserApplicationSummary>> searchApplicationsForSummary(
        UserApplicationSearchRequest userApplicationSearchRequest) {
        List<UserApplicationSummary> userApplicationSummaries = applicationRepository.fuzzySearchApplication(
            userApplicationSearchRequest.getUserId(), userApplicationSearchRequest.getNamespace(),
            userApplicationSearchRequest.getKey(), userApplicationSearchRequest.isFilterDisabled()).stream().map(
            applicationDO -> userApplicationSummaryConverter.convert(applicationDO)).collect(
            Collectors.toList());
        return Response.okWithData(userApplicationSummaries);
    }

    @Override
    public Response<ApplicationBasicInfo> getApplicationBasic(UserApplicationQueryRequest userApplicationQueryRequest)
        throws Throwable {
        ApplicationDO applicationDO = applicationRepository.findByAppIdOrAppName(userApplicationQueryRequest.getAppId(),
            userApplicationQueryRequest.getAppName(), userApplicationQueryRequest.getUserId(),
            userApplicationQueryRequest.getNamespace());
        if (applicationDO == null) {
            throw new ChaosException(CommonErrorCode.P_APPLICATION_NOT_FOUND);
        }
        return Response.okWithData(applicationBasicInfoConverter.convert(applicationDO));
    }

    @Override
    public Response<PageableResponse<BaseExperimentTask>> getApplicationExperimentTasks(
        UserApplicationQueryRequest userApplicationQueryRequest) {
        IPage<ApplicationRelationDO> relationDOIPage = applicationRelationRepository
            .pageableFindRelationsByAppIdAndRelationType(userApplicationQueryRequest.getAppId(),
                ApplicationRelationDO.RELATION_EXPERIMENT_TASK, userApplicationQueryRequest.getPage(),
                userApplicationQueryRequest.getSize());
        List<BaseExperimentTask> baseExperimentTasks = relationDOIPage.getRecords().stream().map(
            e -> applicationBaseExperimentTaskConverter.convert(e)).filter(Objects::nonNull).collect(
            Collectors.toList());
        PageableResponse<BaseExperimentTask> pageableResponse = PageableResponse.of(
            relationDOIPage.getCurrent(), relationDOIPage.getSize(), baseExperimentTasks,
            relationDOIPage.getPages(), relationDOIPage.getTotal());
        return Response.okWithData(pageableResponse);
    }

    @Override
    public Response<Integer> stopExperimentsByApplication(UserApplicationQueryRequest userApplicationQueryRequest) {
        ApplicationDO applicationDO = applicationRepository.findByAppIdOrAppName(userApplicationQueryRequest.getAppId(),
            userApplicationQueryRequest.getAppName(), userApplicationQueryRequest.getUserId(),
            userApplicationQueryRequest.getNamespace());
        if (applicationDO == null) {
            throw new ChaosException(CommonErrorCode.P_APPLICATION_NOT_FOUND);
        }
        List<ApplicationRelationDO> applicationRelationDOS = applicationRelationRepository.findRunningTaskByAppId(
            applicationDO.getId());
        applicationRelationDOS.forEach(applicationRelationDO -> {
            ExperimentTaskStopRequest experimentTaskStopRequest = new ExperimentTaskStopRequest();
            BaseRequest.copy(userApplicationQueryRequest, experimentTaskStopRequest);
            experimentTaskStopRequest.setTaskId(applicationRelationDO.getOuterId());
            commandBus.asyncRun(ExperimentTaskStopCommand.class, experimentTaskStopRequest);
        });
        return Response.okWithData(applicationRelationDOS.size());
    }

    @Override
    public Response<PageableResponse<ExperimentScope>> searchApplicationHosts(
        ApplicationScopeSearchRequest applicationScopeSearchRequest) {
        ApplicationDeviceQuery applicationDeviceQuery
            = buildQuery(applicationScopeSearchRequest);
        IPage<ApplicationDeviceDO> applicationDeviceDOIPage = applicationDeviceRepository
            .searchLiveDevice(applicationDeviceQuery, applicationScopeSearchRequest.getPage(),
                applicationScopeSearchRequest.getSize());
        PageableResponse<ExperimentScope> pageableResponse = PageableResponse.of(
            applicationDeviceDOIPage.getCurrent(), applicationDeviceDOIPage.getSize(),
            applicationDeviceDOIPage.getRecords().stream()
                .map(e -> applicationDeviceToExperimentScopeConverter.convert(e)).filter(Objects::nonNull).collect(
                    Collectors.toList()), applicationDeviceDOIPage.getPages(), applicationDeviceDOIPage.getTotal());
        return Response.okWithData(pageableResponse);
    }

    private ApplicationDeviceQuery buildQuery(
        ApplicationScopeSearchRequest applicationScopeSearchRequest) {
        ApplicationDeviceQuery applicationDeviceQuery = new ApplicationDeviceQuery();
        applicationDeviceQuery.setAppId(applicationScopeSearchRequest.getAppId());
        applicationDeviceQuery.setGroupName(applicationScopeSearchRequest.getGroupName());
        applicationDeviceQuery.setPartName(applicationScopeSearchRequest.getPartName());
        return applicationDeviceQuery;
    }

    @Override
    public Response<PageableResponse<CloudDevice>> getScopesByApplication(ChaosUser user,
                                                                          PageableQueryApplicationHostRequest pageableQueryApplicationHostRequest) {

        ApplicationDO applicationDO = applicationRepository.findById(
            pageableQueryApplicationHostRequest.getAppId()).orElse(null);
        if (applicationDO == null) {
            return Response.failedWith(CommonErrorCode.P_APPLICATION_NOT_FOUND);
        }
        IPage<ApplicationDeviceDO>
            applicationDeviceDOIPage = findApplicationDeviceDOIPage(pageableQueryApplicationHostRequest,
            applicationDO);

        List<CloudDevice> cloudDevices = applicationDeviceDOIPage.getRecords().stream().map(
                applicationDeviceDO -> applicationDeviceConverter.convert(applicationDeviceDO)).filter(Objects::nonNull)
            .collect(Collectors.toList());

        //RAM校验权限列表
        PageableResponse<CloudDevice> pageableResponse = PageableResponse.of(
            applicationDeviceDOIPage.getCurrent(),
            applicationDeviceDOIPage.getSize(), cloudDevices, applicationDeviceDOIPage.getPages(),
            applicationDeviceDOIPage.getTotal());
        return Response.okWithData(pageableResponse);
    }

    private IPage<ApplicationDeviceDO> findApplicationDeviceDOIPage(
            PageableQueryApplicationHostRequest pageableQueryApplicationHostRequest, ApplicationDO applicationDO) {
        IPage<ApplicationDeviceDO> applicationDeviceDOIPage = applicationDeviceRepository
                .findLiveDeviceByAppIdAndGroupAndKeyAndTags(
                        applicationDO, pageableQueryApplicationHostRequest.getGroups(),
                        pageableQueryApplicationHostRequest.getKey(),
                        pageableQueryApplicationHostRequest.getOsType(),
                        pageableQueryApplicationHostRequest.getTags(),
                        pageableQueryApplicationHostRequest.getKubNamespaces(),
                        pageableQueryApplicationHostRequest.getClusterIds(),
                        pageableQueryApplicationHostRequest.getPage(),
                        pageableQueryApplicationHostRequest.getSize());
        return applicationDeviceDOIPage;
    }

    @Override
    public Response<List<AppNameAndIdPair>> getUserApplicationNames(ApplicationSearchRequest searchRequest) {
        //应用类型与机器类型不一致，
        fixAppType(searchRequest);
        List<AppNameAndIdPair> result = getAppNameAndIdPairs(searchRequest);
        return Response.okWithData(result);
    }

    private List<AppNameAndIdPair> getAppNameAndIdPairs(ApplicationSearchRequest searchRequest) {
        List<AppNameAndIdPair> result = applicationRepository
            .findApplicationByUserIdAndScopeTypeReturnNames(
                searchRequest.getUserId(),
                searchRequest.getNamespace(),
                searchRequest.getAppType(),
                searchRequest.getKey(),
                searchRequest.isFilterDisabled());
        return result.stream().peek(this::setOsType).filter(appNameAndIdPair -> {
            if (null != searchRequest.getOsType()) {
                return searchRequest.getOsType().equals(appNameAndIdPair.getOsType());
            }
            return true;
        }).collect(Collectors.toList());
    }

    private void setOsType(AppNameAndIdPair appNameAndIdPair) {
        List<ApplicationDeviceDO> applicationDeviceDOS = applicationDeviceRepository.findLiveByAppIdLimitOne(
            Long.valueOf(appNameAndIdPair.getAppId()));
        if (!CollectionUtil.isNullOrEmpty(applicationDeviceDOS) && null != applicationDeviceDOS.get(0)) {
            appNameAndIdPair.setScopeType(applicationDeviceDOS.get(0).getDeviceType());
            Integer osType = applicationDeviceDOS.get(0).getOsType();
            appNameAndIdPair.setOsType(null == osType ? DeviceOsType.linux.getType() : osType);
        }
    }

    private void fixAppType(ApplicationSearchRequest searchRequest) {
        if (!Strings.isNullOrEmpty(searchRequest.getAppType()) &&
            (String.valueOf(Scope.SCOPE_TYPE_CONTAINER).equals(searchRequest.getAppType())
                || String.valueOf(Scope.SCOPE_TYPE_K8S).equals(searchRequest.getAppType()))) {
            //应用类型 0代表主机 1代表容器环境
            searchRequest.setAppType("1");
        }
    }

    @Override
    public Response<List<String>> getGroupsByApp(UserApplicationQueryRequest userApplicationQueryRequest) {
        return Response.okWithData(
            applicationGroupRepository.findApplicationGroupsByAppId(userApplicationQueryRequest.getAppId()));
    }

    @Override
    public Response<List<Host>> getScopeByAppNameAndGroupsAndIp(
        ApplicationGroupDeviceQuery applicationGroupDeviceQuery) {
        List<ApplicationDeviceDO> applicationDeviceDOs = applicationDeviceRepository.
            findLiveDeviceByAppNameAndGroupAndIp(
                applicationGroupDeviceQuery.getAppName(),
                applicationGroupDeviceQuery.getGroupNames(),
                applicationGroupDeviceQuery.getIps()
            );
        return Response.okWithData(applicationDeviceDOs.stream()
            .map(applicationDeviceDO -> applicationDeviceConverter.convert(applicationDeviceDO))
            .filter(Objects::nonNull)
            .collect(Collectors.toList()));

    }

    @Override
    public Response<List<ApplicationConfiguration>> listApplicationConfigurations(
        UserApplicationQueryRequest userApplicationQueryRequest) {
        return Response.okWithData(applicationConfigurator.provide(userApplicationQueryRequest.getAppId()));
    }

    @Override
    public Response updateApplicationConfiguration(
        ApplicationConfigurationUpdateRequest applicationConfigurationUpdateRequest) {
        boolean success = applicationConfigurator.updateOrCreateIfNotExistConfiguration(
            applicationConfigurationUpdateRequest);
        if (success) {
            return Response.ok();
        } else {
            return Response.failedWith(CommonErrorCode.B_UPDATE_APPLICATION_CONFIG_FAILED);
        }
    }

    @Override
    public Response<PageableResponse<ApplicationDeviceDO>> pageableSearchApplicationHostsByAppIdOrAppNamePart
        (ApplicationScopeSearchRequest applicationScopeSearchRequest) {
        ApplicationDeviceQuery applicationDeviceQuery = new ApplicationDeviceQuery();
        applicationDeviceQuery.setAppId(applicationScopeSearchRequest.getAppId());
        applicationDeviceQuery.setAppName(applicationScopeSearchRequest.getAppName());
        applicationDeviceQuery.setIp(applicationScopeSearchRequest.getIp());
        applicationDeviceQuery.setNamespace(applicationScopeSearchRequest.getNamespace());
        applicationDeviceQuery.setUserId(applicationScopeSearchRequest.getUserId());
        IPage<ApplicationDeviceDO> applicationDeviceDOIPage = applicationDeviceRepository
            .searchDeviceByAppIdOrAppNamePart(applicationDeviceQuery, applicationScopeSearchRequest.getPage(),
                applicationScopeSearchRequest.getSize());
        PageableResponse<ApplicationDeviceDO> pageableResponse = PageableResponse.of(
            applicationDeviceDOIPage.getCurrent(), applicationDeviceDOIPage.getSize(),
            applicationDeviceDOIPage.getRecords(), applicationDeviceDOIPage.getPages(),
            applicationDeviceDOIPage.getTotal());
        return Response.okWithData(pageableResponse);
    }

    @Override
    public Response<PageableResponse<ApplicationDO>> pageableSearchUserApplication(
        GetUserApplicationSummariesRequest getUserApplicationSummariesRequest) {
        String userId = getUserApplicationSummariesRequest.getUserId();
        IPage<ApplicationDO> applicationDOIPage = applicationRepository.findApplicationsByUserId(userId,
            getUserApplicationSummariesRequest.getNamespace(),
            getUserApplicationSummariesRequest.isFilterDisabled(),
            getUserApplicationSummariesRequest.getPage(),
            getUserApplicationSummariesRequest.getSize());
        PageableResponse<ApplicationDO> pageableResponse = PageableResponse.of(
            applicationDOIPage.getCurrent(), applicationDOIPage.getSize(),
            applicationDOIPage.getRecords(), applicationDOIPage.getPages(), applicationDOIPage.getTotal());
        return Response.okWithData(pageableResponse);
    }

    private List<CloudDevice> mergeByIp(List<CloudDevice> cloudDevices, ApplicationDO applicationDO) {
        //仅对主机类型做处理
        if (AppType.HOST.getType() == applicationDO.getAppType()) {
            List<CloudDevice> result = new ArrayList<>();
            Map<String, List<CloudDevice>> groupList = cloudDevices.stream().collect(
                Collectors.groupingBy(CloudDevice::getPrivateIp));
            groupList.forEach((ip, deviceList) -> {
                result.add(deviceList.get(0));
            });
            return result;
        }
        return cloudDevices;
    }

    @Override
    public Response<Boolean> updateApplicationTag(ApplicationTagsUpdateRequest applicationTagsUpdateRequest) {

        String userId = applicationTagsUpdateRequest.getUserId();
        List<String> configurationIds = applicationTagsUpdateRequest.getConfigurationIds();
        List<String> tags = applicationTagsUpdateRequest.getTags();
        if (CollectionUtil.isNullOrEmpty(configurationIds)) {
            return Response.okWithData(true);
        }
        //删除目标机器所有标签
        configurationIds.stream().distinct().forEach(configurationId -> {
            applicationDeviceTagRepository.deleteDeviceTag(
                configurationId,
                applicationTagsUpdateRequest.getAppId(),
                applicationTagsUpdateRequest.getGroupName(),
                applicationTagsUpdateRequest.getUserId());
        });
        appendTags(applicationTagsUpdateRequest, userId, configurationIds, tags);
        return Response.okWithData(true);
    }

    private void appendTags(ApplicationTagsUpdateRequest applicationTagsUpdateRequest, String userId,
        List<String> configurationIds, List<String> tags) {
        //添加标签
        if (!CollectionUtil.isNullOrEmpty(tags)) {
            for (String configurationId : configurationIds) {
                List<TagDO> tagDOS = tags.stream().map(
                        tagName -> tagManager.findOrCreateIfNotExistByTagName(userId, tagName,
                            TAG_TYPE_APPLICATION_HOST))
                    .collect(Collectors.toList());
                tagDOS.forEach(tagDO -> {
                    ApplicationDeviceTagDO applicationDeviceTagDO = new ApplicationDeviceTagDO();
                    applicationDeviceTagDO.setTagId(tagDO.getTagId());
                    applicationDeviceTagDO.setTagName(tagDO.getName());
                    applicationDeviceTagDO.setAppId(applicationTagsUpdateRequest.getAppId());
                    applicationDeviceTagDO.setGroupName(applicationTagsUpdateRequest.getGroupName());
                    applicationDeviceTagDO.setConfigurationId(configurationId);
                    applicationDeviceTagDO.setUserId(userId);
                    applicationDeviceTagRepository.add(applicationDeviceTagDO);
                });
            }
        }
    }

    @Override
    public Response<Boolean> batchAddApplicationTag(
        ApplicationTagsBatchUpdateRequest applicationTagsBatchUpdateRequest) {
        String userId = applicationTagsBatchUpdateRequest.getUserId();
        List<String> configurationIds = applicationTagsBatchUpdateRequest.getConfigurationIds();
        List<String> tags = applicationTagsBatchUpdateRequest.getTags();
        if (CollectionUtil.isNullOrEmpty(configurationIds)) {
            Response.okWithData(true);
        }
        appendTags(applicationTagsBatchUpdateRequest, userId, configurationIds, tags);
        return Response.okWithData(true);
    }

    private void appendTags(ApplicationTagsBatchUpdateRequest applicationTagsBatchUpdateRequest, String userId,
        List<String> configurationIds, List<String> tags) {
        //添加标签
        if (!CollectionUtil.isNullOrEmpty(tags)) {
            List<TagDO> tagDOS = tags.stream().map(
                    tagName -> tagManager.findOrCreateIfNotExistByTagName(userId, tagName, TAG_TYPE_APPLICATION_HOST))
                .collect(Collectors.toList());
            for (TagDO tagDO : tagDOS) {
                for (String configurationId : configurationIds) {
                    //查询groupName
                    Optional<ApplicationDeviceDO> applicationDeviceOptional =
                        applicationDeviceRepository.findByAppIdAndAppConfigurationId(
                            applicationTagsBatchUpdateRequest.getAppId(), configurationId);
                    ApplicationDeviceDO applicationDeviceDO = applicationDeviceOptional.get();

                    int count = applicationDeviceTagRepository.countByAppIdAndPrivateIpAndUserIdAndTagName(
                        applicationTagsBatchUpdateRequest.getAppId(),
                        applicationDeviceDO.getGroupName(),
                        configurationId,
                        userId,
                        tagDO.getName());
                    if (count > 0) {
                        continue;
                    }
                    ApplicationDeviceTagDO applicationDeviceTagDO = new ApplicationDeviceTagDO();
                    applicationDeviceTagDO.setTagId(tagDO.getTagId());
                    applicationDeviceTagDO.setTagName(tagDO.getName());
                    applicationDeviceTagDO.setAppId(applicationTagsBatchUpdateRequest.getAppId().toString());
                    applicationDeviceTagDO.setConfigurationId(configurationId);
                    applicationDeviceTagDO.setGroupName(applicationDeviceDO.getGroupName());
                    applicationDeviceTagDO.setUserId(userId);
                    applicationDeviceTagRepository.add(applicationDeviceTagDO);
                }
            }
        }
    }

    @Override
    public Response<List<String>> searchDeviceTagsByAppIdAndGroupName(
        ApplicationTagsSearchRequest applicationTagsSearchRequest) {

        List<ApplicationDeviceDO> applicationDeviceDOS = applicationDeviceRepository.findByAppIdAndGroups(
            String.valueOf(applicationTagsSearchRequest.getAppId()),
            applicationTagsSearchRequest.getGroupNames());

        List<String> configurationIds = applicationDeviceDOS.stream().map(ApplicationDeviceDO::getConfigurationId)
            .collect(Collectors.toList());

        return Response.okWithData(applicationDeviceTagRepository.listApplicationGroupTagsByConfigurationIds(
            configurationIds,
            applicationTagsSearchRequest.getUserId(),
            applicationTagsSearchRequest.getKey()));
    }

    @Override
    public Response<Set<String>> searchClusterNamespacesByAppIdAndGroupName(
        ApplicationTagsSearchRequest applicationTagsSearchRequest) {

        List<ApplicationDeviceDO> applicationDeviceDOS = applicationDeviceRepository.findByAppIdAndGroups(
            String.valueOf(applicationTagsSearchRequest.getAppId()),
            applicationTagsSearchRequest.getGroupNames());
        return Response.okWithData(
            applicationDeviceDOS.stream().collect(Collectors.groupingBy(ApplicationDeviceDO::getKubNamespace))
                .keySet());
    }

    @Override
    public Response<ApplicationHostsCount> countScopesByApplication(ChaosUser user,
                                                                    ApplicationHostsCountRequest applicationHostsCountRequest) {

        ApplicationHostsCount applicationHostsCount = new ApplicationHostsCount();
        if (null == applicationHostsCountRequest.getAppId() ||
            CollectionUtil.isNullOrEmpty(applicationHostsCountRequest.getGroupNames())) {
            applicationHostsCount.setTotal(0);
            return Response.okWithData(applicationHostsCount);
        }
        //机器总数
        applicationHostsCount.setTotal(applicationDeviceRepository.countDeviceByAppIdAndGroupNames(
            applicationHostsCountRequest.getAppId(), applicationHostsCountRequest.getGroupNames()));
        return Response.okWithData(applicationHostsCount);
    }

    @Override
    public Response<List<Host>> getScopeByConfigurationIds(ApplicationGroupDeviceQuery applicationGroupDeviceQuery) {
        List<ApplicationDeviceDO> applicationDeviceDOs = applicationDeviceRepository.findByConfigurationIds(
            applicationGroupDeviceQuery.getConfigurationIds());
        return Response.okWithData(applicationDeviceDOs.stream()
            .map(applicationDeviceDO -> applicationDeviceConverter.convert(applicationDeviceDO))
            .filter(Objects::nonNull)
            .collect(Collectors.toList()));
    }

    @Override
    public Response<List<ExperimentApplication>> findApplicationsInExperimentForVision(String mainUserId,
                                                                                       String namespace) {
        return Response.okWithData(experimentApplicationFinder.findApplicationsInExperiment(mainUserId, namespace));
    }

    @Override
    public Response<PageableResponse<AppNameAndIdPair>> pageUserApplicationNames(
        ApplicationSearchRequest applicationSearchRequest) {
        if (!Strings.isNullOrEmpty(applicationSearchRequest.getAppType()) &&
            (String.valueOf(Scope.SCOPE_TYPE_CONTAINER).equals(applicationSearchRequest.getAppType())
                || String.valueOf(Scope.SCOPE_TYPE_K8S).equals(applicationSearchRequest.getAppType()))) {
            //应用类型 0代表主机 1代表容器环境
            applicationSearchRequest.setAppType("1");
        }
        List<ApplicationDeviceDO> applicationDeviceDOS = applicationDeviceRepository.findUserDeviceGroupByAppId(
            applicationSearchRequest.getUserId(), applicationSearchRequest.getAppIds());
        Map<Long, ApplicationDeviceDO> appDeviceMap = applicationDeviceDOS.stream().filter(deviceDO -> {
            if (null != applicationSearchRequest.getOsType()) {
                if (null == deviceDO.getOsType()) {
                    deviceDO.setOsType(DeviceOsType.linux.getType());
                }
                return applicationSearchRequest.getOsType().equals(deviceDO.getOsType());
            }
            return true;
        }).collect(Collectors.toMap(ApplicationDeviceDO::getAppId, v -> v, (v1, v2) -> v2));
        if (appDeviceMap.size() < 1) {
            return Response.okWithData(PageableResponse.empty());
        }
        IPage<ApplicationDO> applicationDOIPage = applicationRepository.pageUserApplications(
            applicationSearchRequest.getUserId(),
            applicationSearchRequest.getNamespace(),
            applicationSearchRequest.getAppType(),
            applicationSearchRequest.getKey(),
            applicationSearchRequest.isFilterDisabled(),
            new ArrayList<>(appDeviceMap.keySet()),
            applicationSearchRequest.getPage(),
            applicationSearchRequest.getSize());

        List<AppNameAndIdPair> appNameAndIdPairs = applicationDOIPage.getRecords().stream().map(applicationDO -> {
            AppNameAndIdPair appNameAndIdPair = new AppNameAndIdPair(applicationDO.getAppName(),
                String.valueOf(applicationDO.getId()));
            appNameAndIdPair.setAppType(applicationDO.getAppType());
            appNameAndIdPair.setScopeType(ScopeTypeEnum.judgeScopeByAppType(applicationDO.getAppType()).getValue());
            ApplicationDeviceDO deviceDO = appDeviceMap.get(applicationDO.getId());
            if (deviceDO != null) {
                appNameAndIdPair.setOsType(null == deviceDO.getOsType() ? 0 : deviceDO.getOsType());
            }
            return appNameAndIdPair;
        }).collect(Collectors.toList());

        return Response.okWithData(PageableResponse.of(
            applicationDOIPage.getCurrent(),
            applicationDOIPage.getSize(), appNameAndIdPairs, applicationDOIPage.getPages(),
            applicationDOIPage.getTotal()));
    }

    protected ApplicationDeviceDO getApplicationDeviceLimitOne(Long appId) {
        List<ApplicationDeviceDO> applicationDeviceDOS =
            applicationDeviceRepository.findLiveByAppIdLimitOne(appId);
        if (!CollectionUtil.isNullOrEmpty(applicationDeviceDOS) && null != applicationDeviceDOS.get(0)) {
            return applicationDeviceDOS.get(0);
        }
        return null;
    }

    @Override
    public Response<Host> getScopeByAppConfigurationId(String appConfigurationId) {
        Optional<ApplicationDeviceDO> applicationDeviceDOOptional = applicationDeviceRepository.findById(
                appConfigurationId);
        if (!applicationDeviceDOOptional.isPresent()) {
            return Response.failedWith(CommonErrorCode.P_APP_DEVICE_NOT_FOUND);
        }
        return Response.okWithData(applicationDeviceConverter.convert(applicationDeviceDOOptional.get()));
    }

}
