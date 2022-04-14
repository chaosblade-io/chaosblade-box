package com.alibaba.chaosblade.box.dao.model;

import com.alibaba.chaosblade.box.dao.model.base.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author haibin
 *
 *
 */
@TableName("t_chaos_user_operation_log")
@Data
public class UserOperationLogDO extends BaseDO {

    public static String TARGET_TYPE_EXPERIMENT = "experiment";

    public static String TARGET_TYPE_EXPERIMENT_TASK = "experiment_task";

    public static String OPERATION_CREATE = "create";

    public static String OPERATION_CLONE = "clone";

    public static String OPERATION_DELETE = "delete";

    public static String OPERATION_QUERY = "query";

    public static String OPERATION_RUN = "run";

    public static String OPERATION_UPDATE = "update";
    public static String OPERATION_STOP = "stop";


    @TableField(value = "user_id")
    private String userId;

    @TableField(value = "target")
    private String target;

    @TableField(value = "target_type")
    private String targetType;

    @TableField(value = "operation")
    private String operation;

}
