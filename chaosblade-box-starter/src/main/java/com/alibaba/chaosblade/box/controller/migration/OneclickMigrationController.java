package com.alibaba.chaosblade.box.controller.migration;

import com.alibaba.chaosblade.box.annotation.LoginUser;
import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import com.alibaba.chaosblade.box.controller.BaseController;
import com.alibaba.chaosblade.box.model.RestResponseUtil;
import com.alibaba.chaosblade.box.service.OneclickMigrationService;
import com.alibaba.chaosblade.box.service.model.RestResponse;
import com.alibaba.chaosblade.box.service.model.migration.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OneclickMigrationController extends BaseController {
    @Autowired
    private OneclickMigrationService oneclickMigrationService;

    @PostMapping("GetMigrationConfiguration")
    public RestResponse<MigrationConfigurationResponse> getMigrationConfiguration(@LoginUser ChaosUser chaosUser) {
        MigrationConfigurationResponse migrationConfigurationResponse = oneclickMigrationService.getMigrationConfiguration(chaosUser);
        if (migrationConfigurationResponse == null) {
            return RestResponseUtil.failed(null);
        }

        return RestResponseUtil.okWithData(migrationConfigurationResponse);
    }

    @PostMapping("CheckDbAccount")
    public RestResponse<Boolean> checkDbAccount(@RequestBody CheckAccountRequest checkAccountRequest) {
        Boolean result = oneclickMigrationService.checkDbAccount(checkAccountRequest);
        if (result.equals(true)){
            return RestResponseUtil.okWithData(true);
        }

        return RestResponseUtil.failed(null);
    }

    @PostMapping("SaveMigrationConfiguration")
    public RestResponse<Boolean> saveMigrationConfiguration(@LoginUser ChaosUser chaosUser, @RequestBody SaveMigrationConfigurationRequest saveMigrationConfigurationRequest) {
        Boolean result = oneclickMigrationService.saveMigrationConfiguration(chaosUser, saveMigrationConfigurationRequest);
        if (result.equals(true)){
            return RestResponseUtil.okWithData(result);
        }

        return RestResponseUtil.failed(null);
    }

    @PostMapping("StartMigration")
    public RestResponse<Boolean> startMigration(@LoginUser ChaosUser chaosUser, @RequestBody MigrationProcessRequest migrationProcessRequest) {
        Boolean result = oneclickMigrationService.startMigration(chaosUser, migrationProcessRequest);
        if (result.equals(true)){
            return RestResponseUtil.okWithData(true);
        }

        return RestResponseUtil.failed(null);
    }

    @PostMapping("QueryMigrationResult")
    public RestResponse<QueryMigrationResponse> queryMigrationResult(@LoginUser ChaosUser chaosUser, @RequestBody MigrationProcessRequest migrationProcessRequest) {
        QueryMigrationResponse result = oneclickMigrationService.queryMigrationResult(chaosUser, migrationProcessRequest);
        if (result == null){
            return RestResponseUtil.failed(null);
        }

        return RestResponseUtil.okWithData(result);
    }

}
