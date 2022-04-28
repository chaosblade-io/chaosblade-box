package com.alibaba.chaosblade.box.controller.agent;


import com.alibaba.chaosblade.box.annotation.LoginUser;
import com.alibaba.chaosblade.box.common.common.domain.ChaosError;
import com.alibaba.chaosblade.box.common.common.domain.response.Response;
import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import com.alibaba.chaosblade.box.common.common.enums.CommonErrorCode;
import com.alibaba.chaosblade.box.common.common.enums.DeviceOsType;
import com.alibaba.chaosblade.box.common.common.enums.InstallMode;
import com.alibaba.chaosblade.box.controller.BaseController;
import com.alibaba.chaosblade.box.model.RestResponseUtil;
import com.alibaba.chaosblade.box.service.SettingService;
import com.alibaba.chaosblade.box.service.model.RestResponse;
import com.alibaba.chaosblade.box.service.model.agent.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
class SettingController extends BaseController {

    @Autowired
    private SettingService settingService;

    @RequestMapping(value = "QueryInstallCommand", method = RequestMethod.POST)
    public RestResponse<Map<String, String>> queryInstallCommand(@LoginUser ChaosUser user,
                                                                 @RequestBody SettingQueryInstallRequest settingQueryInstallRequest) {
        String userId = user.getUserId();

        InstallMode installMode = InstallMode.getByName(settingQueryInstallRequest.getMode());
        DeviceOsType deviceOsType = DeviceOsType.getByType(settingQueryInstallRequest.getOsType());
        if (installMode == null) {
            return RestResponseUtil.failed(ChaosError.withCode(CommonErrorCode.P_ARGUMENT_ILLEGAL));
        }
        Map<String, String> returnT = settingService.queryAgentInstallCommandByMode(userId, settingQueryInstallRequest.getNamespace(), installMode, deviceOsType,
                settingQueryInstallRequest.getHelmVersion(), settingQueryInstallRequest.getLang());

        return RestResponseUtil.okWithData(returnT);
    }

    @RequestMapping(value = "QueryHelmPackageAddress", method = RequestMethod.POST)
    public RestResponse<String> queryHelmPackageAddress(@LoginUser ChaosUser user, HttpServletRequest request) {
        String userId = user.getUserId();
        String returnT = settingService.queryHelmAgentInstallPackageAddress();


        return RestResponseUtil.okWithData(returnT);
    }

    @ApiOperation(value = "查询卸载命令")
    @PostMapping("QueryUninstallCommand")
    public RestResponse<Map<String, String>> queryUninstallCommand(@LoginUser ChaosUser user, @RequestBody SettingRequest settingRequest) {

        String userId = user.getUserId();
        Map<String, String> uninstallCommand = settingService.queryAgentUnInstallCommand(userId,
                settingRequest.getNamespace(),
                settingRequest.getConfigurationId());
        return RestResponseUtil.okWithData(uninstallCommand);
    }

    @ApiOperation(value = "主机探针卸载")
    @PostMapping("Uninstall")
    public RestResponse<Boolean> uninstall(@LoginUser ChaosUser user, @RequestBody SettingRequest settingRequest) {

        String userId = user.getUserId();
        Boolean returnT = settingService.uninstallAgent(userId, settingRequest);

        return RestResponseUtil.okWithData(returnT);
    }


    @ApiOperation(value = "主机探针安装")
    @PostMapping("InstallPlugin")
    public Response<String> installPlugin(@LoginUser ChaosUser user, @RequestBody InstallAgentForHostRequest installAgentForHostRequest) {

        return settingService.installAgentForHost(user.getLicense(), installAgentForHostRequest);
    }

    @ApiOperation(value = "主机探针卸载")
    @PostMapping("UninstallPlugin")
    public RestResponse<Boolean> uninstallPlugin(@LoginUser ChaosUser user, @RequestBody SettingRequest settingRequest) {

        String userId = user.getUserId();
        Boolean returnT = settingService.uninstallAgentForHost(userId, settingRequest);
        if (returnT) {
            return RestResponseUtil.okWithData(returnT);
        }

        return RestResponseUtil.failed(ChaosError.withCode(CommonErrorCode.B_CHAOS_TOOLS_UNINSTALL_ERROR));
    }

    @PostMapping(value = "QueryPluginStatus")
    public RestResponse<PluginDTO> queryPluginStatus(@LoginUser ChaosUser user, @RequestBody SettingQueryPluginStatus settingQueryPluginStatus) {
        String userId = user.getUserId();
        PluginDTO pluginDTOReturnT = settingService.queryAgentPluginDetail(userId, settingQueryPluginStatus.getNamespace(), settingQueryPluginStatus.getInstanceId());
        if (pluginDTOReturnT == null) {
            return RestResponseUtil.failed(ChaosError.withCode(CommonErrorCode.B_CHAOS_TOOLS_UNINSTALL_ERROR));
        }

        return RestResponseUtil.okWithData(pluginDTOReturnT);
    }

}