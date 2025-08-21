package com.alibaba.chaosblade.box.controller;

import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import com.alibaba.chaosblade.box.annotation.LoginUser;
import com.alibaba.chaosblade.box.dao.model.base.PageableResponse;
import com.alibaba.chaosblade.box.model.RestResponseUtil;
import com.alibaba.chaosblade.box.service.LoadTestStrategyService;
import com.alibaba.chaosblade.box.service.model.RestResponse;
import com.alibaba.chaosblade.box.service.model.loadtest.LoadTestStrategyCreateRequest;
import com.alibaba.chaosblade.box.service.model.loadtest.LoadTestStrategyQueryRequest;
import com.alibaba.chaosblade.box.service.model.loadtest.LoadTestStrategyUpdateRequest;
import com.alibaba.chaosblade.box.service.model.loadtest.LoadTestStrategyVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * 压测策略控制器
 *
 * @author ZhengMingZhuo
 */
@Slf4j
@RestController
@Api(description = "压测策略管理")
public class LoadTestStrategyController {

    @Resource
    private LoadTestStrategyService loadTestStrategyService;

    @PostMapping("/CreateLoadTestStrategy")
    @ApiOperation(value = "创建压测策略")
    public RestResponse<String> createLoadTestStrategy(
            @LoginUser ChaosUser user,
            @Valid @RequestBody LoadTestStrategyCreateRequest request) {
        
        request.setUser(user);
        return RestResponseUtil.initWithServiceResponse(
                loadTestStrategyService.createLoadTestStrategy(request));
    }

    @PostMapping("/UpdateLoadTestStrategy")
    @ApiOperation(value = "更新压测策略")
    public RestResponse<Void> updateLoadTestStrategy(
            @LoginUser ChaosUser user,
            @Valid @RequestBody LoadTestStrategyUpdateRequest request) {
        
        request.setUser(user);
        return RestResponseUtil.initWithServiceResponse(
                loadTestStrategyService.updateLoadTestStrategy(request));
    }

    @PostMapping("/DeleteLoadTestStrategy")
    @ApiOperation(value = "删除压测策略")
    public RestResponse<Void> deleteLoadTestStrategy(
            @LoginUser ChaosUser user,
            @ApiParam(value = "策略ID", required = true) @RequestParam String id,
            @ApiParam(value = "命名空间") @RequestParam(required = false, defaultValue = "default") String namespace) {
        
        return RestResponseUtil.initWithServiceResponse(
                loadTestStrategyService.deleteLoadTestStrategy(id, user.getUserId(), namespace));
    }

    @PostMapping("/GetLoadTestStrategy")
    @ApiOperation(value = "根据ID查询压测策略")
    public RestResponse<LoadTestStrategyVO> getLoadTestStrategy(
            @LoginUser ChaosUser user,
            @ApiParam(value = "策略ID", required = true) @RequestParam String id,
            @ApiParam(value = "命名空间") @RequestParam(required = false, defaultValue = "default") String namespace) {
        
        return RestResponseUtil.initWithServiceResponse(
                loadTestStrategyService.getLoadTestStrategy(id, user.getUserId(), namespace));
    }

    @PostMapping("/GetLoadTestStrategyByExperimentId")
    @ApiOperation(value = "根据实验ID查询压测策略")
    public RestResponse<LoadTestStrategyVO> getLoadTestStrategyByExperimentId(
            @LoginUser ChaosUser user,
            @ApiParam(value = "实验ID", required = true) @RequestParam String experimentId,
            @ApiParam(value = "命名空间") @RequestParam(required = false, defaultValue = "default") String namespace) {
        
        return RestResponseUtil.initWithServiceResponse(
                loadTestStrategyService.getLoadTestStrategyByExperimentId(experimentId, user.getUserId(), namespace));
    }

    @PostMapping("/GetLoadTestStrategiesByDefinitionId")
    @ApiOperation(value = "根据压测定义ID查询压测策略列表")
    public RestResponse<List<LoadTestStrategyVO>> getLoadTestStrategiesByDefinitionId(
            @LoginUser ChaosUser user,
            @ApiParam(value = "压测定义ID", required = true) @RequestParam String definitionId,
            @ApiParam(value = "命名空间") @RequestParam(required = false, defaultValue = "default") String namespace) {
        
        return RestResponseUtil.initWithServiceResponse(
                loadTestStrategyService.getLoadTestStrategiesByDefinitionId(definitionId, user.getUserId(), namespace));
    }

    @PostMapping("/QueryLoadTestStrategies")
    @ApiOperation(value = "分页查询压测策略")
    public RestResponse<PageableResponse<LoadTestStrategyVO>> queryLoadTestStrategies(
            @LoginUser ChaosUser user,
            @RequestBody LoadTestStrategyQueryRequest request) {
        
        request.setUser(user);
        return RestResponseUtil.initWithServiceResponse(
                loadTestStrategyService.queryLoadTestStrategies(request));
    }

    @PostMapping("/ListAllLoadTestStrategies")
    @ApiOperation(value = "查询所有压测策略（不分页）")
    public RestResponse<List<LoadTestStrategyVO>> listAllLoadTestStrategies(
            @LoginUser ChaosUser user,
            @ApiParam(value = "命名空间") @RequestParam(required = false, defaultValue = "default") String namespace) {
        
        return RestResponseUtil.initWithServiceResponse(
                loadTestStrategyService.listAllLoadTestStrategies(user.getUserId(), namespace));
    }
}
