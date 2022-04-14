package com.alibaba.chaosblade.box.service.model.application;

import com.alibaba.chaosblade.box.common.common.domain.task.BaseExperimentTask;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author haibin
 *
 *
 */
@Data
public class ApplicationBasicInfo implements Serializable {

    @JSONField(name = "app_name")
    private String appName;

    @JSONField(name = "app_groups")
    private List<String> appGroups;

    @JSONField(name = "experiment_task_count")
    private Integer experimentTaskCount;

    @JSONField(name = "machine_count")
    private Integer machineCount;

    @JSONField(name = "task")
    private BaseExperimentTask task;

    @JSONField(name = "app_type")
    private Integer appType;

    @JSONField(name = "app_id")
    private String appId;

}
