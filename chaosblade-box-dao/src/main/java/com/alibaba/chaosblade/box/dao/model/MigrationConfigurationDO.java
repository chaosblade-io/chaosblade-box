package com.alibaba.chaosblade.box.dao.model;

import com.alibaba.chaosblade.box.dao.model.base.BaseDO;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_chaos_migration_configuration")
public class MigrationConfigurationDO extends BaseDO {
    private String userId;

    private String cloudAk;

    private String cloudSk;

    private Integer metricMigration;

    private String dbUrl;

    private String dbAccount;

    private String dbPassword;

    private String migrationResult;
}
