package com.alibaba.chaosblade.box.common.common.enums;

/**
 * 一键转移中涉及的进程
 */
public enum MigrationProgressEnum implements MigrationProgress {

    EXPERIMENT_PROGRESS("演练数据迁移","experiment"),
    EXPERITISE_PROGRESS("演练经验库迁移","expertise"),
    AGENT_PROGRESS("探针迁移","agent");

    /**
     * 名称
     */
    private String name;
    /**
     * 类型
     */
    private String type;

    MigrationProgressEnum(String name, String type) {
        this.name = name;
        this.type = type;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getType() {
        return this.type;
    }
}
