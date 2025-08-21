package com.alibaba.chaosblade.box.controller;

import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import com.alibaba.chaosblade.box.annotation.LoginUser;
import com.alibaba.chaosblade.box.dao.model.base.PageableResponse;
import com.alibaba.chaosblade.box.model.RestResponseUtil;
import com.alibaba.chaosblade.box.service.LoadTestDefinitionService;
import com.alibaba.chaosblade.box.service.model.RestResponse;
import com.alibaba.chaosblade.box.service.model.loadtest.LoadTestDefinitionCreateRequest;
import com.alibaba.chaosblade.box.service.model.loadtest.LoadTestDefinitionQueryRequest;
import com.alibaba.chaosblade.box.service.model.loadtest.LoadTestDefinitionUpdateRequest;
import com.alibaba.chaosblade.box.service.model.loadtest.LoadTestDefinitionVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * 压测定义控制器
 *
 * @author ZhengMingZhuo
 */
@Slf4j
@RestController
@Api(description = "压测定义管理")
public class LoadTestDefinitionController  {

    @Resource
    private LoadTestDefinitionService loadTestDefinitionService;

    @PostMapping("/CreateLoadTestDefinition")
    @ApiOperation(value = "创建压测定义")
    public RestResponse<String> createLoadTestDefinition(
            @LoginUser ChaosUser user,
            @Valid @RequestBody LoadTestDefinitionCreateRequest request) {
        
        request.setUser(user);
        return RestResponseUtil.initWithServiceResponse(
                loadTestDefinitionService.createLoadTestDefinition(request));
    }

    @PostMapping("/UpdateLoadTestDefinition")
    @ApiOperation(value = "更新压测定义")
    public RestResponse<Void> updateLoadTestDefinition(
            @LoginUser ChaosUser user,
            @Valid @RequestBody LoadTestDefinitionUpdateRequest request) {
        
        request.setUser(user);
        return RestResponseUtil.initWithServiceResponse(
                loadTestDefinitionService.updateLoadTestDefinition(request));
    }

    @PostMapping("/DeleteLoadTestDefinition")
    @ApiOperation(value = "删除压测定义")
    public RestResponse<Void> deleteLoadTestDefinition(
            @LoginUser ChaosUser user,
            @ApiParam(value = "压测定义ID", required = true) @RequestParam String id,
            @ApiParam(value = "命名空间") @RequestParam(required = false, defaultValue = "default") String namespace) {
        
        return RestResponseUtil.initWithServiceResponse(
                loadTestDefinitionService.deleteLoadTestDefinition(id, user.getUserId(), namespace));
    }

    @PostMapping("/GetLoadTestDefinition")
    @ApiOperation(value = "根据ID查询压测定义")
    public RestResponse<LoadTestDefinitionVO> getLoadTestDefinition(
            @LoginUser ChaosUser user,
            @ApiParam(value = "压测定义ID", required = true) @RequestParam String id,
            @ApiParam(value = "命名空间") @RequestParam(required = false, defaultValue = "default") String namespace) {
        
        return RestResponseUtil.initWithServiceResponse(
                loadTestDefinitionService.getLoadTestDefinition(id, user.getUserId(), namespace));
    }

    @PostMapping("/QueryLoadTestDefinitions")
    @ApiOperation(value = "分页查询压测定义")
    public RestResponse<PageableResponse<LoadTestDefinitionVO>> queryLoadTestDefinitions(
            @LoginUser ChaosUser user,
            @RequestBody LoadTestDefinitionQueryRequest request) {
        
        request.setUser(user);
        return RestResponseUtil.initWithServiceResponse(
                loadTestDefinitionService.queryLoadTestDefinitions(request));
    }

    @PostMapping("/ListAllLoadTestDefinitions")
    @ApiOperation(value = "查询所有压测定义（不分页）")
    public RestResponse<List<LoadTestDefinitionVO>> listAllLoadTestDefinitions(
            @LoginUser ChaosUser user,
            @ApiParam(value = "命名空间") @RequestParam(required = false, defaultValue = "default") String namespace) {
        
        return RestResponseUtil.initWithServiceResponse(
                loadTestDefinitionService.listAllLoadTestDefinitions(user.getUserId(), namespace));
    }
}
