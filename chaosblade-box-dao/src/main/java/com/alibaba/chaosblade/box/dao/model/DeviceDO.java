package com.alibaba.chaosblade.box.dao.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Date;

/**
 * @author sunju
 *
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@TableName("t_chaos_device")
public class DeviceDO extends com.alibaba.chaosblade.box.dao.model.base.DeviceDO {

    public static int STATUS_ENABLED = 2;

    /**
     *
     */
    Boolean experimentStatus;

    /**
     * 最近一次演练时间
     */
    Date lastExperimentTime;

    private String deviceRole;

    public boolean isAlive() {
        return STATUS_ENABLED == getStatus();
    }

}
