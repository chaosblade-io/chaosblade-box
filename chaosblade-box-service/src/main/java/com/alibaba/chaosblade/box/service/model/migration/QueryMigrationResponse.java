package com.alibaba.chaosblade.box.service.model.migration;

import com.alibaba.chaosblade.box.common.common.enums.MigrationStateEnum;
import lombok.Data;

import java.util.List;

@Data
public class QueryMigrationResponse {
//    private List<MigrationResultDetail> metricMigrationResult;
//
//    private MigrationResultDetail agentMigrationResult;

    private List<MigrationProgressDetail> progress;

    private MigrationStateEnum status;
}
