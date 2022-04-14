package com.alibaba.chaosblade.box.service.model.scope;

import com.alibaba.chaosblade.box.common.common.enums.ResultEnum;
import com.alibaba.chaosblade.box.common.common.enums.StateEnum;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author haibin
 *
 *
 */
@Data
public class ScopeExperimentTask implements Serializable {

    @JSONField(name = "experiment_id")
    private String experimentId;

    @JSONField(name = "task_id")
    private String taskId;

    @JSONField(name = "start_time")
    private Date startTime;

    @JSONField(name = "end_time")
    private Date endTime;

    @JSONField(name = "state")
    private StateEnum state;

    @JSONField(name = "result")
    private ResultEnum result;

    private String name;

}
