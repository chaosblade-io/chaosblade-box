package com.alibaba.chaosblade.box.dao.model;

import com.alibaba.chaosblade.box.dao.model.base.BaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.List;

/**
 * @author haibin
 *
 *
 */
@Data
@TableName(value = "t_chaos_notification")
public class ExperimentNotificationDO extends BaseDO {

    public static String FIELD_EXPERIMENT_ID = "experiment_id";

    /**
     * 钉钉
     */
    public static final int TYPE_DINGDING = 1 << 0;

    /**
     * 邮件
     */
    public static final int TYPE_EMAIL = 1 << 1;

    /**
     * 演练任务开始
     */
    public static final int TYPE_CHANGEFREE_START = 1 << 2;

    /**
     * 演练任务结束
     */
    public static final int TYPE_CHANGEFREE_END = 1 << 3;

    /**
     * 通知ID
     */
    @TableId(type = IdType.ID_WORKER_STR)
    private String notificationId;

    /**
     * 通知标志位
     */
    private Integer notificationFlag;

    /**
     * 需要通知的人员，多个人员用 ; 分割
     */
    private List<String> notificationEmpIds;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 演练ID
     */
    private String experimentId;

    public boolean isSupport(int notificationType) {
        return (this.notificationFlag & notificationType) == notificationType;
    }

    public void support(int notificationType) {
        if (notificationFlag == null) {
            notificationFlag = notificationType;
        } else {
            notificationFlag |= notificationType;
        }
    }

    public void notSupport(int notificationType) {
        if (notificationFlag != null) {
            notificationFlag &= ~notificationType;
        }
    }
}
