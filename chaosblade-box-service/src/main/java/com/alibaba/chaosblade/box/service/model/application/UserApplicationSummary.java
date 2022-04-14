package com.alibaba.chaosblade.box.service.model.application;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author haibin
 *
 *
 */
@Data
public class UserApplicationSummary {

    @JSONField(name = "app_name")
    private String appName;

    private Boolean isDefault;

    @JSONField(name = "app_type")
    private Integer appType;

    @JSONField(name = "app_id")
    private String appId;

    @JSONField(name = "experiment_task_count")
    private Integer experimentTaskCount;

    @JSONField(name = "machine_count")
    private Integer machineCount;

    /**
     * 场景数目
     */
    @JSONField(name = "scene_function_count")
    private Integer sceneFunctionCount;

}
