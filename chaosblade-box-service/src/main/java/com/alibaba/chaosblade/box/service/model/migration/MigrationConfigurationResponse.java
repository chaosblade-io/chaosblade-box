package com.alibaba.chaosblade.box.service.model.migration;

import lombok.Data;

@Data
public class MigrationConfigurationResponse {
    private String userId;

    private String cloudAk;

    private String cloudSk;

    private Boolean experimentFlag;

    private Boolean expertiseFlag;

    private String dbHost;

    private String dbPort;

    private String dbAccount;

    private String dbPassword;
}
