package com.alibaba.chaosblade.box.service.model.migration;

import lombok.Data;

@Data
public class MigrationProcessRequest {
    /**
     * can be userd:
     * all
     * experiment
     * expertise
     * agent
     */
    private String migrationFlag;
}
