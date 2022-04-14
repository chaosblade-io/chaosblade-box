package com.alibaba.chaosblade.box.service.model.migration;

import lombok.Data;

@Data
public class SaveMigrationConfigurationRequest {
    // cloud
    private String cloudAccount;

    private String cloudPassword;

    // metric
    private Boolean experimentFlag;

    private Boolean expertiseFlag;

    // mysql
    private String dbAccount;

    private String dbPassword;

    private String dbHost;

    private String dbPort;
}
