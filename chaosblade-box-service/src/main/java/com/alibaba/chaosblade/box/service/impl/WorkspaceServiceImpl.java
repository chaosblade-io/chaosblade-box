package com.alibaba.chaosblade.box.service.impl;

import com.alibaba.chaosblade.box.common.commands.CommandBus;
import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.common.common.domain.experiment.WorkspaceShortInfo;
import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import com.alibaba.chaosblade.box.common.common.domain.workspace.WorkspaceRelationQuery;
import com.alibaba.chaosblade.box.common.common.enums.ExperimentStateEnum;
import com.alibaba.chaosblade.box.common.experiment.clientobject.ExperimentStat;
import com.alibaba.chaosblade.box.common.infrastructure.constant.ChangelogTypes;
import com.alibaba.chaosblade.box.common.infrastructure.constant.PermissionTypes;
import com.alibaba.chaosblade.box.common.infrastructure.constant.WorkspaceRelationTypes;
import com.alibaba.chaosblade.box.common.infrastructure.constant.WorkspaceTypes;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.response.UserExperiment;
import com.alibaba.chaosblade.box.common.infrastructure.domain.workspace.*;
import com.alibaba.chaosblade.box.common.infrastructure.util.ChangeLogExecutor;
import com.alibaba.chaosblade.box.common.infrastructure.util.CollectionUtil;
import com.alibaba.chaosblade.box.dao.infrastructure.domain.workspace.WorkspaceBaseInfo;
import com.alibaba.chaosblade.box.dao.infrastructure.domain.workspace.WorkspaceExperimentPageableQueryResponse;
import com.alibaba.chaosblade.box.dao.infrastructure.monitor.trace.Trackers;
import com.alibaba.chaosblade.box.dao.model.ExperimentDO;
import com.alibaba.chaosblade.box.dao.model.ExperimentTaskDO;
import com.alibaba.chaosblade.box.dao.model.WorkspaceDO;
import com.alibaba.chaosblade.box.dao.model.WorkspaceRelationDO;
import com.alibaba.chaosblade.box.dao.model.base.PageableResponse;
import com.alibaba.chaosblade.box.dao.query.ExperimentPageQuery;
import com.alibaba.chaosblade.box.dao.repository.*;
import com.alibaba.chaosblade.box.service.WorkspaceService;
import com.alibaba.chaosblade.box.service.auth.perimission.PermissionResult;
import com.alibaba.chaosblade.box.service.command.experiment.WorkspaceExperimentStatCommand;
import com.alibaba.chaosblade.box.service.infrastructure.convertor.ExperimentDOTOUserExperimentConverter;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Component
public class WorkspaceServiceImpl implements WorkspaceService {
    private final static String MY_WORKSPACE_NAME = "我的空间";

    @Resource
    private Trackers trackers;

    @Autowired
    private CommandBus commandBus;

    @Resource
    private WorkspaceRelationRepository workspaceRelationRepository;

//    @Resource
//    private AdministratorManager administratorManager;

    @Autowired
    private ExperimentRepository experimentRepository;

    @Autowired
    private ExperimentTaskRepository experimentTaskRepository;

    @Autowired
    private ExperimentTagRepository experimentTagRepository;

    @Autowired
    private ExperimentDOTOUserExperimentConverter experimentDOTOUserExperimentConverter;

    @Autowired
    private WorkspaceRepository workspaceRepository;

    @Override
    public Response<ExperimentStat> getGeneralWorkSpaceStatInfo(ChaosUser user, WorkspaceQueryRequest request) {
        return Response.okWithData(commandBus.syncRun(WorkspaceExperimentStatCommand.class, request));
    }

    @Override
    public WorkspaceExperimentPageableQueryResponse pageableGeneralExperiments(
            ChaosUser user, WorkspaceExperimentPageableQueryRequest request, Integer permission) {
        WorkspaceRelationQuery workspaceRelationQuery = new WorkspaceRelationQuery();
        workspaceRelationQuery.setWorkspaceIds(Lists.newArrayList(request.getWorkspaceId()));
        workspaceRelationQuery.setRelationType(WorkspaceRelationTypes.EXPERIMENT);
        workspaceRelationQuery.setNamespace(request.getNamespace());
        workspaceRelationQuery.setExperimentCreator(request.getFilterUserId());
        List<WorkspaceRelationDO> relationDOList = workspaceRelationRepository.find(workspaceRelationQuery);

        List<String> experimentIds = relationDOList.stream().map(WorkspaceRelationDO::getOuterId).collect(
                Collectors.toList());

        int page = request.getPage();
        int size = request.getSize();
        if (CollectionUtils.isEmpty(experimentIds)) {
            PageableResponse<UserExperiment> pageableResponse = PageableResponse.of(page, size, Collections.emptyList(), 0L, 0L);

            return WorkspaceExperimentPageableQueryResponse.builder()
                    .permission(permission)
                    .pageableResponse(pageableResponse)
                    .build();
        }

        ExperimentPageQuery experimentPageQuery = new ExperimentPageQuery();
        experimentPageQuery.setExperimentIds(experimentIds);
        experimentPageQuery.setNamespace(request.getNamespace());
        experimentPageQuery.setTagNames(request.getTagNames());
        if(!CollectionUtil.isNullOrEmpty(request.getStates())) {
            request.getStates().forEach(stateEnum -> {
                if (ExperimentStateEnum.FINISHED.equals(stateEnum)) {
                    experimentPageQuery.addStateValues(ExperimentStateEnum.READY);
                } else {
                    experimentPageQuery.addStateValues(stateEnum);
                }
            });
        } else if (null != request.getState()) {
            //兼容旧的数据结构
            if (ExperimentStateEnum.FINISHED.equals(request.getState())) {
                experimentPageQuery.addStateValues(ExperimentStateEnum.READY);
            } else {
                experimentPageQuery.addStateValues(request.getState());
            }
        }
        experimentPageQuery.setRunResults(request.getResults());
        experimentPageQuery.setPartName(request.getSearchKey());
        IPage<ExperimentDO> experimentDOIPage = experimentRepository.findByPage(experimentPageQuery, page, size);

        List<UserExperiment> contentList = experimentDOIPage.getRecords()
                .stream().map(experimentDO -> convert(experimentDO, permission, user))
                .collect(Collectors.toList());
        PageableResponse<UserExperiment> pageableResponse = PageableResponse.of(experimentDOIPage.getCurrent(),
                experimentDOIPage.getSize(), contentList, experimentDOIPage.getPages(), experimentDOIPage.getTotal());

        return WorkspaceExperimentPageableQueryResponse.builder()
                .permission(permission)
                .pageableResponse(pageableResponse)
                .build();
    }

    @Override
    public PermissionResult getWorkspaceExperimentTaskPermission(ChaosUser chaosUser, String experimentTaskId) {
        Optional<ExperimentTaskDO> optional = experimentTaskRepository.findById(experimentTaskId);
        if (!optional.isPresent()) {
            return PermissionResult.NONE;
        }

        String experimentId = optional.get().getExperimentId();
        return getWorkspacePermission(chaosUser, experimentId);
    }

    @Override
    public PermissionResult getWorkspacePermission(ChaosUser user, String experimentId) {
//        if (administratorManager.isAdmin(user)) {
//            return PermissionResult.of(PermissionTypes.ALL);
//        }

        WorkspaceRelationQuery userWorkspaceRelationQuery = new WorkspaceRelationQuery();
        userWorkspaceRelationQuery.setOuterIds(Lists.newArrayList(user.getCurrentUserId()));
//        userWorkspaceRelationQuery.setUserRole(WorkspaceUserRoles.getWorkspaceUserRole(user));
        userWorkspaceRelationQuery.setRelationType(WorkspaceRelationTypes.USER);
        List<WorkspaceRelationDO> userRelationDOList = workspaceRelationRepository.find(userWorkspaceRelationQuery);

        if (CollectionUtils.isEmpty(userRelationDOList)) {
            return PermissionResult.of(PermissionTypes.NONE);
        }

        List<String> workspaceIdList = userRelationDOList.stream().map(WorkspaceRelationDO::getWorkspaceId).collect(
                Collectors.toList());
        WorkspaceRelationQuery workspaceRelationQuery = new WorkspaceRelationQuery();
        workspaceRelationQuery.setWorkspaceIds(workspaceIdList);
        workspaceRelationQuery.setRelationType(WorkspaceRelationTypes.EXPERIMENT);
        workspaceRelationQuery.setOuterIds(Collections.singletonList(experimentId));
        List<WorkspaceRelationDO> relationDOList = workspaceRelationRepository.find(workspaceRelationQuery);

        if (CollectionUtils.isEmpty(relationDOList)) {
            return PermissionResult.of(PermissionTypes.NONE);
        }

        Set<String> workspaceIdSet = relationDOList.stream().map(r -> r.getWorkspaceId()).collect(Collectors.toSet());

        Optional<Integer> first = userRelationDOList.stream()
                .filter(r -> workspaceIdSet.contains(r.getWorkspaceId()))
                .map(WorkspaceRelationDO::getPermission).max(Comparator.naturalOrder());

        if (first.isPresent()) {
            return PermissionResult.of(first.get());
        }

        return PermissionResult.of(PermissionTypes.NONE);
    }

    private UserExperiment convert(ExperimentDO experimentDO, Integer permission, ChaosUser chaosUser) {
        UserExperiment userExperiment = experimentDOTOUserExperimentConverter.convert(experimentDO);
        if (Objects.equals(chaosUser.getCurrentUserId(), experimentDO.getCreator().getCurrentUserId())) {
            userExperiment.setPermission(PermissionTypes.ALL);
        } else {
            userExperiment.setPermission(permission);
        }
        return userExperiment;
    }

    @Override
    public List<String> listExperimentTagsInWorkspace(String workspaceId, String namespace) {
        WorkspaceRelationQuery workspaceRelationQuery = new WorkspaceRelationQuery();
        workspaceRelationQuery.setWorkspaceIds(Lists.newArrayList(workspaceId));
        workspaceRelationQuery.setRelationType(WorkspaceRelationTypes.EXPERIMENT);
        workspaceRelationQuery.setNamespace(namespace);
        List<WorkspaceRelationDO> relationDOList = workspaceRelationRepository.find(workspaceRelationQuery);
        List<String> experimentIds = relationDOList.stream().map(WorkspaceRelationDO::getOuterId).collect(
                Collectors.toList());

        if (CollectionUtils.isEmpty(experimentIds)) {
            return Collections.emptyList();
        }

        return experimentTagRepository.findDistinctTags(experimentIds);
    }

    @Override
    public AddExperimentResponse addWorkspaceExperiment(ChaosUser user, WorkspaceExperimentRequest request) {
        return ChangeLogExecutor.executeWithChangeLog(new Supplier<AddExperimentResponse>() {
            @Override
            public AddExperimentResponse get() {
                List<WorkspaceExperiment> duplicate = new ArrayList<>();
                for (WorkspaceExperiment r : request.getWorkspaceExperimentList()) {
                    WorkspaceRelationQuery workspaceRelationQuery = new WorkspaceRelationQuery();
                    workspaceRelationQuery.setWorkspaceIds(Lists.newArrayList(request.getWorkspaceId()));
                    workspaceRelationQuery.setRelationType(WorkspaceRelationTypes.EXPERIMENT);
                    workspaceRelationQuery.setOuterIds(Collections.singletonList(r.getExperimentId()));
                    workspaceRelationQuery.setNamespace(request.getNamespace());
                    List<WorkspaceRelationDO> relationDOList = workspaceRelationRepository.find(workspaceRelationQuery);
                    if (CollectionUtils.isNotEmpty(relationDOList)) {
                        duplicate.add(r);
                        continue;
                    }
                    WorkspaceRelationDO relation = new WorkspaceRelationDO();
                    relation.setRelationType(WorkspaceRelationTypes.EXPERIMENT);
                    relation.setOuterId(r.getExperimentId());
                    relation.setNamespace(request.getNamespace());
                    relation.setOuterDescription(r.getExperimentName());
                    relation.setWorkspaceId(request.getWorkspaceId());
                    relation.setIsDelete(false);
                    relation.setExperimentCreator(user.getCurrentUserId());
                    workspaceRelationRepository.add(relation);
                }
                return AddExperimentResponse.builder()
                        .success(true)
                        .duplicateExperiments(duplicate)
                        .build();
            }
        }, new Consumer<AddExperimentResponse>() {
            @Override
            public void accept(AddExperimentResponse addExperimentResponse) {
                if (addExperimentResponse.getSuccess()) {
                    trackers.track(
                            ChangelogTypes.ChangeActionType.Update,
                            ChangelogTypes.ChangeOperatorType.USER,
                            user.getCurrentUserId(),
                            ChangelogTypes.ChangeTargetType.WORKSPACE,
                            request.getWorkspaceId(),
                            ChangelogTypes.ChangePropertyType.EXPERIMENT,
                            request.getWorkspaceExperimentList().stream().map(WorkspaceExperiment::getExperimentId)
                                    .collect(Collectors.joining(",")),
                            null
                    );
                }
            }
        });

    }

    @Override
    public void addWorkspaceExperiments(String experimentId, String name, String namespace, List<String> workspaceIds) {
        WorkspaceRelationQuery workspaceRelationQuery = new WorkspaceRelationQuery();
        workspaceRelationQuery.setOuterIds(Collections.singletonList(experimentId));
        workspaceRelationQuery.setRelationType(WorkspaceRelationTypes.EXPERIMENT);
        workspaceRelationQuery.setNamespace(namespace);
        workspaceRelationRepository.deleteWorkspaceRelation(workspaceRelationQuery, false);
        if (workspaceIds != null) {
            workspaceIds.forEach(new Consumer<String>() {
                @Override
                public void accept(String workspaceId) {
                    WorkspaceRelationDO workspaceRelationDO = new WorkspaceRelationDO();
                    workspaceRelationDO.setOuterId(experimentId);
                    workspaceRelationDO.setNamespace(namespace);
                    workspaceRelationDO.setOuterDescription(name);
                    workspaceRelationDO.setWorkspaceId(workspaceId);
                    workspaceRelationDO.setRelationType(WorkspaceRelationTypes.EXPERIMENT);
                    workspaceRelationRepository.add(workspaceRelationDO);
                }
            });
        }

    }

    @Override
    public List<WorkspaceShortInfo> getWorkspacesShortInfoByExperimentId(String experimentId) {
        WorkspaceRelationQuery workspaceRelationQuery = new WorkspaceRelationQuery();
        workspaceRelationQuery.setOuterIds(Lists.newArrayList(experimentId));
        workspaceRelationQuery.setRelationType(WorkspaceRelationTypes.EXPERIMENT);
        return searchWorkspaces(workspaceRelationQuery);
    }


    public List<WorkspaceShortInfo> searchWorkspaces(WorkspaceRelationQuery workspaceRelationQuery) {
        List<WorkspaceRelationDO> relations = workspaceRelationRepository.find(workspaceRelationQuery);
        if (CollectionUtil.isNullOrEmpty(relations)) {
            return Lists.newArrayList();
        }
        WorkspaceQuery query = new WorkspaceQuery();
        query.setWorkspaceIds(relations.stream().map(WorkspaceRelationDO::getWorkspaceId).collect(Collectors.toList()));
        List<WorkspaceDO> workspaces = workspaceRepository.find(query);
        if (CollectionUtil.isNullOrEmpty(workspaces)) {
            return Lists.newArrayList();
        }
        Map<String, WorkspaceDO> mapping = workspaces.stream()
                .collect(Collectors.toMap(
                        WorkspaceDO::getWorkspaceId,
                        Function.identity(),
                        (k1, k2) -> k2
                ));
        return relations.stream()
                .map(workspaceRelationDO -> {
                    WorkspaceShortInfo workspaceShortInfo = new WorkspaceShortInfo();
                    workspaceShortInfo.setRelationType(workspaceRelationDO.getRelationType());
                    workspaceShortInfo.setWorkspaceId(workspaceRelationDO.getWorkspaceId());
                    WorkspaceDO workspace = mapping.get(workspaceRelationDO.getWorkspaceId());
                    if (null != workspace) {
                        workspaceShortInfo.setName(workspace.getName());
                        return workspaceShortInfo;
                    } else {
                        return null;
                    }

                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public List<WorkspaceBaseInfo> getWorkspaceByExperimentId(ChaosUser user, WorkspaceQueryRequest request) {
        WorkspaceRelationQuery workspaceRelationQuery = new WorkspaceRelationQuery();
        workspaceRelationQuery.setOuterIds(Lists.newArrayList(request.getExperimentId()));
        workspaceRelationQuery.setRelationType(WorkspaceRelationTypes.EXPERIMENT);
        workspaceRelationQuery.setNamespace(request.getNamespace());
        List<WorkspaceRelationDO> relationDOList = workspaceRelationRepository.find(workspaceRelationQuery);

        if (CollectionUtils.isEmpty(relationDOList)) {
            return Collections.emptyList();
        }

        WorkspaceQuery workspaceQuery = WorkspaceQuery.builder().type(WorkspaceTypes.GENERAL).workspaceIds(
                relationDOList.stream()
                        .map(WorkspaceRelationDO::getWorkspaceId).collect(Collectors.toList())).build();
        List<WorkspaceDO> workspaceDOList = workspaceRepository.find(workspaceQuery);

        List<WorkspaceBaseInfo> workspaceBaseInfoList = workspaceDOList.stream()
                .map(r -> WorkspaceBaseInfo.builder().name(r.getName()).workspaceId(r.getWorkspaceId()).build())
                .collect(Collectors.toList());

        return workspaceBaseInfoList;
    }
}
