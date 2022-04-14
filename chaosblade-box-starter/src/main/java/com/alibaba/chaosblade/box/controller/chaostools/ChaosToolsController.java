package com.alibaba.chaosblade.box.controller.chaostools;

import com.alibaba.chaosblade.box.annotation.LoginUser;
import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import com.alibaba.chaosblade.box.controller.BaseController;
import com.alibaba.chaosblade.box.model.RestResponseUtil;
import com.alibaba.chaosblade.box.service.ChaosToolsService;
import com.alibaba.chaosblade.box.service.model.RestResponse;
import com.alibaba.chaosblade.box.common.infrastructure.domain.chaostools.ChaosToolsRequest;
import com.alibaba.chaosblade.box.common.infrastructure.domain.chaostools.ToolsOverview;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author yefei
 */
@RestController
public class ChaosToolsController extends BaseController {

    @Autowired
    private ChaosToolsService chaosToolsService;

    /**
     * 演练工具列表
     *
     * @return
     */
    @PostMapping(value = "GetChaosToolsOverviewList")
    public RestResponse<List<ToolsOverview>> toolsOverviewList(@RequestBody ChaosToolsRequest chaosToolsRequest) {
        return RestResponseUtil.okWithData(chaosToolsService.toolsOverviewList(chaosToolsRequest));
    }

    /**
     * 安装演练工具
     *
     * @return
     */
    @PostMapping(value = "InstallChaosTools")
    public RestResponse<?> installChaosTools(@RequestBody ChaosToolsRequest chaosToolsRequest, @LoginUser ChaosUser chaosUser) {
        chaosToolsRequest.setUserId(chaosUser.getUserId());
        return RestResponseUtil.initWithServiceResponse(chaosToolsService.installChaosTools(chaosToolsRequest));
    }

    /**
     * 卸载演练工具
     *
     * @return
     */
    @PostMapping(value = "UninstallChaosTools")
    public RestResponse<?> uninstallChaosTools(@RequestBody ChaosToolsRequest chaosToolsRequest, @LoginUser ChaosUser chaosUser) {
        chaosToolsRequest.setUserId(chaosUser.getUserId());
        return RestResponseUtil.initWithServiceResponse(chaosToolsService.uninstallChaosTools(chaosToolsRequest));
    }

}
