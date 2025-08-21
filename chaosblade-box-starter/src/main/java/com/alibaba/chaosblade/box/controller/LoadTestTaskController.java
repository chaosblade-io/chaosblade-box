package com.alibaba.chaosblade.box.controller;

import com.alibaba.chaosblade.box.annotation.LoginUser;
import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import com.alibaba.chaosblade.box.dao.infrastructure.loadtest.model.LoadTestEventsResponse;
import com.alibaba.chaosblade.box.dao.infrastructure.loadtest.model.LoadTestResultResponse;
import com.alibaba.chaosblade.box.dao.infrastructure.loadtest.model.PerformanceTimeseries;
import com.alibaba.chaosblade.box.dao.model.base.PageableResponse;
import com.alibaba.chaosblade.box.service.LoadTestTaskService;
import com.alibaba.chaosblade.box.service.model.loadtest.LoadTestTaskQueryRequest;
import com.alibaba.chaosblade.box.service.model.loadtest.LoadTestTaskVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * 压测任务控制器
 *
 * @author ZhengMingZhuo
 */
@Slf4j
@RestController
@Api(description = "压测任务管理")
public class LoadTestTaskController {

    @Resource
    private LoadTestTaskService loadTestTaskService;

    @GetMapping("/GetLoadTestTask")
    @ApiOperation(value = "查询压测任务（支持压测任务ID或演练任务ID）")
    public Response<LoadTestTaskVO> getLoadTestTask(
            @LoginUser ChaosUser user,
            @ApiParam(value = "任务ID（可以是压测任务ID或演练任务ID）", required = true) @RequestParam String taskId,
            @ApiParam(value = "演练任务ID（可选，如果提供则优先使用此参数）") @RequestParam(required = false) String experimentTaskId,
            @ApiParam(value = "命名空间") @RequestParam(required = false, defaultValue = "default") String namespace) {

        // 如果提供了 experimentTaskId，优先使用演练任务ID查询
        if (experimentTaskId != null && !experimentTaskId.trim().isEmpty()) {
            return loadTestTaskService.getLoadTestTaskByExperimentTaskId(experimentTaskId, user.getUserId(), namespace);
        }

        // 否则尝试直接用 taskId 查询压测任务
        Response<LoadTestTaskVO> response = loadTestTaskService.getLoadTestTask(taskId, user.getUserId(), namespace);

        // 如果直接查询失败，尝试将 taskId 作为演练任务ID查询
        if (!response.isSuccess()) {
            response = loadTestTaskService.getLoadTestTaskByExperimentTaskId(taskId, user.getUserId(), namespace);
        }

        return response;
    }

    @GetMapping("/GetLoadTestTaskByExperimentTaskId")
    @ApiOperation(value = "根据演练任务ID查询压测任务")
    public Response<LoadTestTaskVO> getLoadTestTaskByExperimentTaskId(
            @LoginUser ChaosUser user,
            @ApiParam(value = "演练任务ID", required = true) @RequestParam String experimentTaskId,
            @ApiParam(value = "命名空间") @RequestParam(required = false, defaultValue = "default") String namespace) {

        return loadTestTaskService.getLoadTestTaskByExperimentTaskId(experimentTaskId, user.getUserId(), namespace);
    }

    @PostMapping("/QueryLoadTestTasks")
    @ApiOperation(value = "分页查询压测任务")
    public Response<PageableResponse<LoadTestTaskVO>> queryLoadTestTasks(
            @LoginUser ChaosUser user,
            @Valid @RequestBody LoadTestTaskQueryRequest request) {

        request.setUser(user);
        return loadTestTaskService.queryLoadTestTasks(request);
    }

    @GetMapping("/ListAllLoadTestTasks")
    @ApiOperation(value = "查询所有压测任务")
    public Response<List<LoadTestTaskVO>> listAllLoadTestTasks(
            @LoginUser ChaosUser user,
            @ApiParam(value = "命名空间") @RequestParam(required = false, defaultValue = "default") String namespace) {

        return loadTestTaskService.listAllLoadTestTasks(user.getUserId(), namespace);
    }

    @PostMapping("/StopLoadTestTask")
    @ApiOperation(value = "停止压测任务")
    public Response<Boolean> stopLoadTestTask(
            @LoginUser ChaosUser user,
            @ApiParam(value = "任务ID", required = true) @RequestParam String taskId,
            @ApiParam(value = "命名空间") @RequestParam(required = false, defaultValue = "default") String namespace) {

        return loadTestTaskService.stopLoadTestTask(taskId, user.getUserId(), namespace);
    }

    @GetMapping("/GetLoadTestResults")
    @ApiOperation(value = "获取压测结果")
    public Response<LoadTestResultResponse> getLoadTestResults(
            @LoginUser ChaosUser user,
            @ApiParam(value = "压测任务ID") @RequestParam(required = false) String taskId,
            @ApiParam(value = "演练任务ID") @RequestParam(required = false) String experimentTaskId,
            @ApiParam(value = "命名空间") @RequestParam(required = false, defaultValue = "default") String namespace) {

        return loadTestTaskService.getLoadTestResults(taskId, experimentTaskId, user.getUserId(), namespace);
    }

    @GetMapping("/GetLoadTestEvents")
    @ApiOperation(value = "获取压测事件流水")
    public Response<LoadTestEventsResponse> getLoadTestEvents(
            @LoginUser ChaosUser user,
            @ApiParam(value = "压测任务ID") @RequestParam(required = false) String taskId,
            @ApiParam(value = "演练任务ID") @RequestParam(required = false) String experimentTaskId,
            @ApiParam(value = "返回最近N条记录") @RequestParam(required = false, defaultValue = "100") Integer tail,
            @ApiParam(value = "命名空间") @RequestParam(required = false, defaultValue = "default") String namespace) {

        return loadTestTaskService.getLoadTestEvents(taskId, experimentTaskId, tail, user.getUserId(), namespace);
    }

    @PostMapping("/SyncLoadTestTaskStatus")
    @ApiOperation(value = "同步压测任务状态")
    public Response<LoadTestTaskVO> syncLoadTestTaskStatus(
            @LoginUser ChaosUser user,
            @ApiParam(value = "任务ID", required = true) @RequestParam String taskId,
            @ApiParam(value = "命名空间") @RequestParam(required = false, defaultValue = "default") String namespace) {

        return loadTestTaskService.syncLoadTestTaskStatus(taskId, user.getUserId(), namespace);
    }

    @GetMapping("/api/metrics/performance/{executionId}/series")
    @ApiOperation(value = "获取性能指标时序数据")
    public Response<PerformanceTimeseries> getPerformanceTimeseries(
            @LoginUser ChaosUser user,
            @ApiParam(value = "执行ID", required = true) @PathVariable String executionId,
            @ApiParam(value = "命名空间") @RequestParam(required = false, defaultValue = "default") String namespace) {

        return loadTestTaskService.getPerformanceTimeseries(executionId, user.getUserId(), namespace);
    }
}
