package com.alibaba.chaosblade.box.service.impl;

import com.alibaba.chaosblade.box.common.commands.CommandBus;
import com.alibaba.chaosblade.box.common.common.constant.ChaosFunctionConstant;
import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import com.alibaba.chaosblade.box.common.common.enums.CommonErrorCode;
import com.alibaba.chaosblade.box.common.infrastructure.constant.PermissionTypes;
import com.alibaba.chaosblade.box.common.infrastructure.domain.scene.SceneAuthorizedQueryRequest;
import com.alibaba.chaosblade.box.common.infrastructure.domain.scene.SceneFunctionCreateRequest;
import com.alibaba.chaosblade.box.common.infrastructure.domain.scene.SceneQueryRequest;
import com.alibaba.chaosblade.box.common.infrastructure.exception.ChaosException;
import com.alibaba.chaosblade.box.common.infrastructure.util.CollectionUtil;
import com.alibaba.chaosblade.box.common.infrastructure.util.RetryUtil;
import com.alibaba.chaosblade.box.common.infrastructure.util.SceneFunctionUtils;
import com.alibaba.chaosblade.box.dao.infrastructure.app.SceneFunctionUpdateInternalOperator;
import com.alibaba.chaosblade.box.dao.infrastructure.app.function.SceneSynchronousHelper;
import com.alibaba.chaosblade.box.dao.infrastructure.app.function.UserSceneFunctionFilter;
import com.alibaba.chaosblade.box.dao.infrastructure.app.function.sync.ChaosBladeSynchronizer;
import com.alibaba.chaosblade.box.dao.infrastructure.event.ChaosEventDispatcher;
import com.alibaba.chaosblade.box.dao.infrastructure.manager.SceneFunctionCategoryManager;
import com.alibaba.chaosblade.box.dao.infrastructure.service.ChangelogService;
import com.alibaba.chaosblade.box.dao.infrastructure.service.MetricService;
import com.alibaba.chaosblade.box.dao.model.SceneAuthorizedDO;
import com.alibaba.chaosblade.box.dao.model.SceneFunctionDO;
import com.alibaba.chaosblade.box.dao.model.SceneFunctionRelationDO;
import com.alibaba.chaosblade.box.dao.model.base.PageableQueryWrapper;
import com.alibaba.chaosblade.box.dao.model.base.PageableResponse;
import com.alibaba.chaosblade.box.dao.repository.*;
import com.alibaba.chaosblade.box.service.SceneFunctionService;
import com.alibaba.chaosblade.box.service.SceneService;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * Author: sunju
 *
 * Date:   2019/01/09
 */
@Slf4j
@Service
public class SceneFunctionServiceImpl implements SceneFunctionService {

    private static final String MAGIC_PARAMETER_ALIAS = "metric";

    @Resource
    private SceneFunctionRepository sceneFunctionRepository;

    @Resource
    private SceneFunctionParameterRepository sceneFunctionParameterRepository;

    @Resource
    private SceneFunctionRelationRepository sceneFunctionRelationRepository;

    @Resource
    private SceneFunctionParameterRelationRepository sceneFunctionParameterRelationRepository;

    @Resource
    private ActivityTargetExecutionResultRepository activityTargetExecutionResultRepository;

    @Resource
    private SceneAuthorizedRepository sceneAuthorizedRepository;

    @Resource
    private ChangelogService changelogService;

    @Autowired
    private UserSceneFunctionFilter userSceneFunctionFilter;

    @Resource
    private ChaosBladeSynchronizer chaosBladeSynchronizer;

    @Resource
    private SceneService sceneService;
    @Autowired
    private SceneFunctionUpdateInternalOperator sceneFunctionUpdateInternalOperator;

    @Resource(name = "cloudMetricService")
    private MetricService metricService;

    @Resource
    private CommandBus commandBus;

    @Autowired
    private ChaosEventDispatcher chaosEventDispatcher;

    @Autowired
    private SceneFunctionCategoryManager sceneFunctionCategoryManager;

    @Autowired
    private SceneSynchronousHelper sceneSynchronousHelper;

    private final Executor refreshCacheWorker = new ThreadPoolExecutor(
        1,
        1,
        0L,
        TimeUnit.SECONDS,
        new ArrayBlockingQueue<>(100),
        Executors.defaultThreadFactory(),
        new ThreadPoolExecutor.AbortPolicy()
    );

    private AtomicBoolean init = new AtomicBoolean(false);

    @Override
    public SceneFunctionDO querySceneFunctionByCode(String code) {
        Optional<SceneFunctionDO> sceneFunctionDOOptional = sceneFunctionRepository.findByCode(code);
        return sceneFunctionDOOptional.map(this::withParameter).orElseGet(() -> sceneFunctionDOOptional.orElse(null));
    }

    private SceneFunctionDO withParameter(SceneFunctionDO sceneFunctionDO) {
        sceneFunctionParameterRepository.findAllParamsByFunctionId(sceneFunctionDO.getFunctionId())
            .ifPresent(sceneFunctionDO::setParameters);
        return sceneFunctionDO;
    }

    @Override
    public String addSceneFunction(SceneFunctionCreateRequest request) throws ChaosException {
        //如果code已经存在，那么就报错
        Preconditions.checkArgument(request.getUser() != null, "user Required");
        Preconditions.checkArgument(request.getCode() != null, "code Required");
        if (sceneFunctionRepository.findByCode(request.getCode()).isPresent()) {
            throw new ChaosException(CommonErrorCode.B_MINIAPP_CODE_EXIST);
        }
        //先处理scene
        String sceneCode = SceneFunctionUtils.extractSceneCodeFronSunctionCode(request.getCode());
        String sceneId = sceneService.findOrCreateIfNotExistBySceneCode(request.getUser(), sceneCode);
        return createSceneFunction(sceneId, request);

    }

    @Override
    public Boolean queryIsFissionSceneFunction(String functionId) {
        SceneFunctionRelationDO relationDO = sceneFunctionRelationRepository.findByFunctionId(functionId);
        if (null != relationDO) {
            return !relationDO.getFunctionId().equals(relationDO.getOutFunctionId());
        }
        return false;
    }

    private String createSceneFunction(String sceneId, SceneFunctionCreateRequest sceneFunctionCreateRequest)
        throws ChaosException {
        SceneFunctionDO sceneFunctionDO = new SceneFunctionDO();
        sceneFunctionDO.setName(sceneFunctionCreateRequest.getName());
        sceneFunctionDO.setPhaseFlag(sceneFunctionCreateRequest.getPhaseFlag());
        sceneFunctionDO.setDescription(sceneFunctionCreateRequest.getDescription());
        sceneFunctionDO.setCode(sceneFunctionCreateRequest.getCode());
        sceneFunctionDO.setSceneId(sceneId);
        sceneFunctionDO.setSource(ChaosFunctionConstant.SOURCE_CUSTOM_APP_SCRIPT);
        //        sceneFunctionDO.setUserId(sceneFunctionCreateRequest.getUserId());
        if (null != sceneFunctionCreateRequest.getEnabled()) {
            sceneFunctionDO.setEnabled(sceneFunctionCreateRequest.getEnabled());
        } else {
            sceneFunctionDO.setEnabled(0);
        }
        sceneFunctionDO.setSystemVersionList(sceneFunctionCreateRequest.getSystemVersions());

        sceneFunctionRepository.add(sceneFunctionDO);

        this.addSceneFunctionAuthorizedRecord(sceneFunctionCreateRequest.getUser(), sceneFunctionDO);
        this.addSceneFunctionRelation(sceneFunctionDO, sceneFunctionDO);

        return sceneFunctionDO.getFunctionId();
    }

    private void addSceneFunctionAuthorizedRecord(ChaosUser user, SceneFunctionDO function) throws ChaosException {
        this.addSceneFunctionAuthorizedRecord(user.getUserId(), function);
    }

    private void addSceneFunctionAuthorizedRecord(String userId, SceneFunctionDO function) throws ChaosException {
        SceneAuthorizedDO authorized = new SceneAuthorizedDO();
        authorized.setFunctionId(function.getFunctionId());
        authorized.setFunctionName(function.getName());
        authorized.setFunctionCode(function.getCode());
        authorized.setGrantFrom(userId);
        authorized.setGrantTo(userId);
        authorized.setEnabled(function.getEnabled());
        authorized.setIsPublic(false);
        authorized.setPermission(PermissionTypes.ALL);
        authorized.setSource(function.getSource());
        authorized.setPhase(function.getPhaseFlag());
        if (null != function.getGmtCreate()) {
            authorized.setFunctionCreateTime(function.getGmtCreate());
        } else {
            authorized.setFunctionCreateTime(new Timestamp(System.currentTimeMillis()));
        }

        boolean result = RetryUtil.retryIfReturnFalse(() -> sceneAuthorizedRepository.add(authorized));
        if (!result) {
            throw new ChaosException(CommonErrorCode.B_CREATE_MINIAPP_FAILED,
                "Add scene authorized record failed.");
        }
    }

    private void addSceneFunctionRelation(SceneFunctionDO parent, SceneFunctionDO child) throws ChaosException {
        SceneFunctionRelationDO relationDO = new SceneFunctionRelationDO();
        relationDO.setRelationId(IdWorker.getIdStr());
        relationDO.setFunctionId(child.getFunctionId());
        relationDO.setOutFunctionId(parent.getFunctionId());
        boolean result = RetryUtil.retryIfReturnFalse(() -> sceneFunctionRelationRepository.add(relationDO));
        if (!result) {
            throw new ChaosException(CommonErrorCode.B_CREATE_MINIAPP_RELATION);
        }
    }

    @Override
    public PageableResponse<SceneFunctionDO> querySceneFunctions(int pageNo, int pageSize,
                                                                 SceneQueryRequest queryRequest) {
        ChaosUser user = queryRequest.getUser();

        SceneAuthorizedQueryRequest sceneAuthorizedQueryRequest = new SceneAuthorizedQueryRequest();
        if (null != user) {
            sceneAuthorizedQueryRequest.setGrantTo(user.getCurrentUserId());
        }
        sceneAuthorizedQueryRequest.setPermission(queryRequest.getPermission());
        sceneAuthorizedQueryRequest.setPhase(queryRequest.getPhase());
        sceneAuthorizedQueryRequest.setEnabled(queryRequest.getEnabled());
        sceneAuthorizedQueryRequest.setIsPublic(queryRequest.getIsPublic());
        sceneAuthorizedQueryRequest.setK8sResourceType(queryRequest.getK8sResourceType());
        sceneAuthorizedQueryRequest.setSupportScopeType(queryRequest.getSupportScopeType());

        if (!Strings.isNullOrEmpty(queryRequest.getCategoryId())) {
            List<String> functionIds = getFunctionIdByCategoryId(queryRequest.getCategoryId());
            if (!CollectionUtil.isNullOrEmpty(functionIds)) {
                sceneAuthorizedQueryRequest.setFunctionIds(functionIds);
            } else {
                return PageableResponse.of(pageNo,pageSize);
            }
        }
        List<SceneAuthorizedDO> sceneAuthorizedResponse = sceneAuthorizedRepository.getAuthorizedRecordsGroupBy(sceneAuthorizedQueryRequest);
        if (CollectionUtil.isNullOrEmpty(sceneAuthorizedResponse)) {
            return PageableResponse.of(pageNo,pageSize);
        }
        List<String> functionIds = sceneAuthorizedResponse
                .stream()
                .map(SceneAuthorizedDO::getFunctionId)
                .collect(Collectors.toList());

        queryRequest.setFunctionIds(functionIds);
        PageableResponse<SceneFunctionDO> sceneFunctionDOPageableResponse = sceneFunctionRepository.findByPage(PageableQueryWrapper.of(queryRequest)
                .pageNumber(pageNo)
                .pageSize(pageSize));

        if(CollectionUtil.isNullOrEmpty(sceneFunctionDOPageableResponse.getData())) {
            return sceneFunctionDOPageableResponse;
        }

        //执行用户过滤
        List<SceneFunctionDO> sceneFunctionDOS =  sceneFunctionDOPageableResponse.getData().stream().filter(
                new Predicate<SceneFunctionDO>() {
                    @Override
                    public boolean apply(SceneFunctionDO input) {
                        return userSceneFunctionFilter.filter(queryRequest.getUser(), input);
                    }
                }).collect(Collectors.toList());


        return PageableResponse.clone(
                sceneFunctionDOPageableResponse,
                sceneFunctionDOS
                        .stream()
                        .sorted((o1, o2) -> {
                            if (o1.getGmtCreate().toInstant().isBefore(o2.getGmtCreate().toInstant())) {
                                return 1;
                            }
                            if (o1.getGmtCreate().toInstant().isAfter(o2.getGmtCreate().toInstant())) {
                                return -1;
                            }
                            return 0;
                        })
                        .collect(Collectors.toList())
        );
    }

    @SuppressWarnings("unchecked")
    public List<String> getFunctionIdByCategoryId(String categoryId) {
        return sceneFunctionCategoryManager.getFunctionIdByCategoryId(categoryId);
    }

}
