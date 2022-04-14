package com.alibaba.chaosblade.box.dao.scheduler.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.*;

/**
 * @author haibin
 *
 *
 */

@Data
public class SchedulerJobCreateRequest implements Serializable {

    /**
     * cron表达式
     */
    private String cron;

    /**
     * cron表达式偏移
     */
    private int cronDataOffset;

    /**
     * 定时任务名字
     */
    private String name;

    /**
     * 业务类型
     */
    private Integer businessType;

    /**
     * 业务ID
     */
    private String businessId;

    /**
     * 类名
     */
    private String className;

    private Date startTime;

    public SchedulerJobCreateRequest(String cron, int cronDataOffset, String name, Integer businessType,
        String businessId, String className) {
        this.cron = cron;
        this.cronDataOffset = cronDataOffset;
        this.name = name;
        this.businessType = businessType;
        this.businessId = businessId;
        this.className = className;
    }

    private String executeMode;

    /**
     * 负责人
     */
    private String userId;

    /**
     * 通知人员
     */
    private List<String> notificationUsers = new ArrayList<>();

    private Map<String, String> parameters = new HashMap<>();

}
