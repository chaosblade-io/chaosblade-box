package com.alibaba.chaosblade.box.controller;

import com.alibaba.chaosblade.box.annotation.LoginUser;
import com.alibaba.chaosblade.box.annotation.SwaggerDoc;
import com.alibaba.chaosblade.box.common.app.sdk.scope.Host;
import com.alibaba.chaosblade.box.common.common.domain.PageQueryResponse;
import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.common.common.domain.experiment.BaseExperimentRequest;
import com.alibaba.chaosblade.box.common.common.domain.task.BaseExperimentTask;
import com.alibaba.chaosblade.box.common.common.domain.task.BaseExperimentTaskRequest;
import com.alibaba.chaosblade.box.common.common.domain.task.ExperimentTask;
import com.alibaba.chaosblade.box.common.common.domain.task.ExperimentTaskStopOption;
import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import com.alibaba.chaosblade.box.common.common.enums.CommonErrorCode;
import com.alibaba.chaosblade.box.common.experiment.clientobject.ExperimentStat;
import com.alibaba.chaosblade.box.common.experiment.clientobject.ExperimentTaskResult;
import com.alibaba.chaosblade.box.common.experiment.request.ExperimentPageableQueryRequest;
import com.alibaba.chaosblade.box.common.experiment.request.ExperimentTaskRequest;
import com.alibaba.chaosblade.box.common.experiment.request.OrderQueryRequest;
import com.alibaba.chaosblade.box.common.experiment.request.UserExperimentStatRequest;
import com.alibaba.chaosblade.box.common.experiment.task.flow.util.Pair;
import com.alibaba.chaosblade.box.common.infrastructure.constant.PermissionTypes;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.ExperimentTaskControl;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.ExperimentTaskLog;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.ExperimentTaskSimple;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.guard.ExperimentTaskGuardsResult;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.response.ExperimentTaskSummary;
import com.alibaba.chaosblade.box.common.infrastructure.exception.ChaosException;
import com.alibaba.chaosblade.box.common.infrastructure.exception.PermissionDeniedException;
import com.alibaba.chaosblade.box.common.infrastructure.util.CollectionUtil;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.definition.ExperimentFlowDefinitionManager;
import com.alibaba.chaosblade.box.model.RestResponseUtil;
import com.alibaba.chaosblade.box.service.ExperimentReadService;
import com.alibaba.chaosblade.box.service.ExperimentTaskService;
import com.alibaba.chaosblade.box.service.auth.perimission.PermissionResult;
import com.alibaba.chaosblade.box.service.auth.perimission.UserPermissionService;
import com.alibaba.chaosblade.box.service.model.RestResponse;
import com.google.common.base.Strings;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author haibin
 *
 *
 */
@RestController
@Api(description = "演练任务")
@SwaggerDoc
public class ExperimentTaskController extends BaseController {

    @Resource
    private ExperimentReadService experimentReadService;

    @Resource
    private ExperimentTaskService experimentTaskService;

    @Resource
    private UserPermissionService userPermissionService;

    @Autowired
    protected ExperimentFlowDefinitionManager experimentFlowDefinitionManager;

    @ApiOperation(value = "演练任务详情")
    @PostMapping(value = "QueryExperimentTask")
    public RestResponse<ExperimentTaskSummary> getExperimentTask(@LoginUser ChaosUser chaosUser,
                                                                 @RequestBody BaseExperimentTaskRequest baseExperimentTaskRequest) throws PermissionDeniedException {
        String experimentTaskId = baseExperimentTaskRequest.getTaskId();
        PermissionResult permissionResult = userPermissionService.checkExperimentTaskPermission(PermissionTypes.R,
                chaosUser, experimentTaskId);
        Response<ExperimentTaskSummary> experimentTaskSimpleResponse = experimentTaskService.getExperimentTaskSummary(
            experimentTaskId);
        if (experimentTaskSimpleResponse.isSuccess()) {
            experimentTaskSimpleResponse.getResult().setPermission(permissionResult.getPermission());
        }
        return RestResponseUtil.initWithServiceResponse(experimentTaskSimpleResponse);
    }

    @ApiOperation(value = "演练任务概要")
    @PostMapping(value = "QueryExperimentTaskBasic")
    public RestResponse<ExperimentTaskSimple> getSimpleExperimentTaskInfo(@LoginUser ChaosUser chaosUser,
                                                                          @RequestBody BaseExperimentTaskRequest baseExperimentTaskRequest) {
        String experimentTaskId = baseExperimentTaskRequest.getTaskId();
        userPermissionService.checkExperimentTaskPermission(PermissionTypes.R, chaosUser, experimentTaskId);
        return RestResponseUtil.initWithServiceResponse(
            experimentTaskService.getExperimentTaskSimpleInfo(experimentTaskId)
        );
    }

    @ApiOperation(value = "根据演练ID查询演练任务")
    @PostMapping(value = "QueryExperimentTasksByExperimentId")
    public RestResponse<List<ExperimentTask>> queryExperimentTasksByExperimentId(@LoginUser ChaosUser chaosUser,
                                                                                 @RequestBody BaseExperimentRequest baseExperimentRequest) {
        String experimentId = baseExperimentRequest.getExperimentId();
        userPermissionService.checkExperimentPermission(PermissionTypes.R, chaosUser, experimentId,
            baseExperimentRequest.getNamespace(), null);
        return RestResponseUtil.initWithServiceResponse(
            experimentTaskService.getExperimentTasksByExperimentId(experimentId)
        );
    }

    @ApiOperation(value = "停止某个演练")
    @PostMapping(value = "StopExperimentTask")
    public RestResponse<Void> stopExperimentTaskId(@LoginUser ChaosUser chaosUser,
        @RequestBody BaseExperimentTaskRequest baseExperimentTaskRequest) throws PermissionDeniedException {
        String experimentTaskId = baseExperimentTaskRequest.getTaskId();
        userPermissionService.checkExperimentTaskPermission(PermissionTypes.X, chaosUser, experimentTaskId);
        return RestResponseUtil.initWithServiceResponse(
            experimentTaskService.stopTask(chaosUser, experimentTaskId, new ExperimentTaskStopOption())
        );
    }

    @ApiOperation(value = "停止所有演练")
    @PostMapping(value = "/StopAllExperimentTasks")
    public RestResponse<Void> stopUserExperimentTaskId(@LoginUser ChaosUser chaosUser,
        @RequestBody BaseExperimentTaskRequest baseExperimentTaskRequest) {
        return RestResponseUtil.initWithServiceResponse(
            experimentTaskService.stopAllTasks(chaosUser, baseExperimentTaskRequest)
        );
    }

    @ApiOperation(value = "获取用户演练统计信息")
    @PostMapping(value = "/ExperimentTaskOverview")
    public RestResponse<ExperimentStat> experimentTaskStatistic(@LoginUser ChaosUser chaosUser,
                                                                @RequestBody BaseExperimentTaskRequest baseExperimentTaskRequest) {
        String namespace = baseExperimentTaskRequest.getNamespace();

        UserExperimentStatRequest userExperimentTaskStatRequest = new UserExperimentStatRequest();
        userExperimentTaskStatRequest.setNamespace(namespace);
        userExperimentTaskStatRequest.setUser(chaosUser);

        return RestResponseUtil.initWithServiceResponse(
            experimentReadService.getUserExperimentStatistics(userExperimentTaskStatRequest)
        );

    }

    @ApiOperation(value = "获取用户演练统计信息,不区分namespace")
    @PostMapping(value = "/ExperimentTaskOverviewIgnoreNamespace")
    public RestResponse<ExperimentStat> experimentTaskStatisticIgnoreNamespace(@LoginUser ChaosUser chaosUser) {
        UserExperimentStatRequest userExperimentTaskStatRequest = new UserExperimentStatRequest();
        userExperimentTaskStatRequest.setUser(chaosUser);
        return RestResponseUtil.initWithServiceResponse(
            experimentReadService.getUserExperimentStatistics(userExperimentTaskStatRequest)
        );

    }

    @ApiOperation(value = "获取演练任务列表")
    @PostMapping(value = "/QueryExperimentTasksSimpleByExperimentId")
    public RestResponse<List<ExperimentTaskSimple>> getExperimentTasksSimpleByExperimentId(@LoginUser ChaosUser chaosUser,
        @RequestBody ExperimentTaskRequest experimentTaskRequest) {
        String experimentId = experimentTaskRequest.getExperimentId();
        userPermissionService.checkExperimentTaskPermission(PermissionTypes.R, chaosUser, experimentId);
        return RestResponseUtil.initWithServiceResponse(
            experimentTaskService.getExperimentTasksSimpleByExperimentId(experimentTaskRequest)
        );
    }

    @ApiOperation(value = "统计演练结果")
    @PostMapping(value = "/QueryExperimentTasksResultByExperimentId")
    public RestResponse<ExperimentTaskResult> queryExperimentTasksResultByExperimentId(@LoginUser ChaosUser chaosUser,
                                                                                       @RequestBody ExperimentTaskRequest experimentTaskRequest) {
        String experimentId = experimentTaskRequest.getExperimentId();
        userPermissionService.checkExperimentTaskPermission(PermissionTypes.R, chaosUser, experimentId);
        return RestResponseUtil.initWithServiceResponse(
            experimentTaskService.getExperimentTasksResultByExperimentId(experimentTaskRequest)
        );
    }

    @ApiOperation(value = "获取演练任务执行结果")
    @PostMapping(value = "/ExperimentTaskResultCount")
    public RestResponse<Map<String, Integer>> experimentTaskResultCount(@LoginUser ChaosUser chaosUser,
        @RequestBody ExperimentTaskRequest experimentTaskRequest) {
        String experimentId = experimentTaskRequest.getExperimentId();
        userPermissionService.checkExperimentTaskPermission(PermissionTypes.R, chaosUser, experimentId);
        return RestResponseUtil.initWithServiceResponse(
            experimentTaskService.getExperimentTaskResultCount(experimentTaskRequest)
        );
    }

    @ApiOperation(value = "演练任务日志")
    @PostMapping(value = "/QueryExperimentTaskLog")
    public RestResponse<ExperimentTaskLog> getExperimentTaskLog(@LoginUser ChaosUser chaosUser,
                                                                @RequestBody BaseExperimentTaskRequest baseExperimentTaskRequest) throws PermissionDeniedException {
        String experimentTaskId = baseExperimentTaskRequest.getTaskId();
        userPermissionService.checkExperimentTaskPermission(PermissionTypes.R, chaosUser, experimentTaskId);
        return RestResponseUtil.initWithServiceResponse(experimentTaskService.getExperimentTaskLog(experimentTaskId));
    }

    @ApiOperation(value = "演练任务控件列表")
    @PostMapping(value = "/QueryControlList")
    public RestResponse<ExperimentTaskControl> getControlList(
        @RequestBody ExperimentTaskRequest experimentTaskRequest) {
        return RestResponseUtil.initWithServiceResponse(experimentTaskService.getControlList(experimentTaskRequest));
    }

    @ApiOperation(value = "获取演练次数")
    @PostMapping(value = "/ExperimentTaskCount")
    public RestResponse<Map<Long, Integer>> experimentTaskCount(
        @RequestBody ExperimentTaskRequest experimentTaskRequest) {
        return RestResponseUtil.initWithServiceResponse(
            experimentTaskService.getExperimentTaskCount(experimentTaskRequest)
        );
    }

    @ApiOperation(value = "获取演练耗")
    @PostMapping(value = "/ExperimentTaskCost")
    public RestResponse<Map<Long, Double>> experimentTaskCost(
        @RequestBody ExperimentTaskRequest experimentTaskRequest) {
        return RestResponseUtil.initWithServiceResponse(
            experimentTaskService.getExperimentTaskCost(experimentTaskRequest)
        );
    }

    @ApiOperation(value = "分页查询演练任务概要")
    @PostMapping(value = "/ExperimentTaskSummaryPageableQuery")
    public RestResponse<PageQueryResponse<BaseExperimentTask>> experimentTaskSummaryPageableQuery(@RequestBody
                                                                                                          ExperimentPageableQueryRequest experimentPageableQueryRequest) {
        return RestResponseUtil.okWithData(
            experimentTaskService.pageableQueryExperimentTaskSummary(experimentPageableQueryRequest)
        );
    }

    @ApiOperation(value = "查询演练任务全局节点信息")
    @PostMapping(value = "/QueryExperimentTaskGuardInfo")
    public RestResponse<ExperimentTaskGuardsResult> QueryExperimentTaskGuardInfoqueryExperimentTaskGuardInfo(
            @LoginUser ChaosUser chaosUser, @RequestBody BaseExperimentTaskRequest baseExperimentTaskRequest) {
        baseExperimentTaskRequest.setUser(chaosUser);
        Response<ExperimentTaskGuardsResult> response = experimentTaskService.queryExperimentTaskGuardInfo(baseExperimentTaskRequest);
        return RestResponseUtil.initWithServiceResponse(response);
    }

    @PostMapping("QueryExperimentAmount")
    public RestResponse<Map<String, Integer>> queryExperimentAmount(@LoginUser ChaosUser chaosUser,
                                                                    @RequestBody OrderQueryRequest request) {
        if (null == request || Strings.isNullOrEmpty(request.getExperimentId())) {
            throw new ChaosException(CommonErrorCode.P_ARGUMENT_ILLEGAL, "experimentId cannot be null or empty.");
        }

        Supplier<Pair<String, Integer>> forecastAmountSupplier = () -> Pair.of("forecastAmount",
                forecastExperimentAmount(request).getResult());

        return RestResponseUtil.okWithData(
                Stream.of(forecastAmountSupplier)
                        .parallel()
                        .map(Supplier::get)
                        .collect(Collectors.toMap(
                                Pair::getLeft,
                                Pair::getRight
                        ))
        );
    }

    @PostMapping("ForecastExperimentAmount")
    public RestResponse<Integer> forecastExperimentAmount(@RequestBody OrderQueryRequest request) {
        if (null == request || Strings.isNullOrEmpty(request.getExperimentId())) {
            throw new ChaosException(CommonErrorCode.P_ARGUMENT_ILLEGAL, "experimentId cannot be null or empty.");
        }

        List<Host> hosts = experimentFlowDefinitionManager.findAllHostsByExperimentId(request.getExperimentId());
        if (!CollectionUtil.isNullOrEmpty(hosts)) {
            return RestResponseUtil.okWithData(hosts.size());
        }

        return RestResponseUtil.okWithData(0);
    }

}
