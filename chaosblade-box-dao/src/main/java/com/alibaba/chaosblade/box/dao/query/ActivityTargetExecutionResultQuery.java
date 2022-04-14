package com.alibaba.chaosblade.box.dao.query;

import lombok.Data;

/**
 * @author haibin
 *
 * 
 */
@Data
public class ActivityTargetExecutionResultQuery {

    private String appCode;

    private String hostIp;

    private String activityTaskId;

    private String executableAppCode;
}
