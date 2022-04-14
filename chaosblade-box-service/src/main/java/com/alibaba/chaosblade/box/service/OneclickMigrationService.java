package com.alibaba.chaosblade.box.service;


import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import com.alibaba.chaosblade.box.service.model.migration.*;

public interface OneclickMigrationService {
    public MigrationConfigurationResponse getMigrationConfiguration(ChaosUser chaosUser);

    public Boolean checkDbAccount(CheckAccountRequest checkAccountRequest);

    public Boolean saveMigrationConfiguration(ChaosUser chaosUser, SaveMigrationConfigurationRequest saveMigrationConfigurationRequest);

    public Boolean startMigration(ChaosUser chaosUser, MigrationProcessRequest migrationProcessRequest);

    public QueryMigrationResponse queryMigrationResult(ChaosUser chaosUser, MigrationProcessRequest migrationProcessRequest);
}
