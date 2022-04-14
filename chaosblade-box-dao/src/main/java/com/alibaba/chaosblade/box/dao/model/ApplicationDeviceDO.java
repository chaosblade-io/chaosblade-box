package com.alibaba.chaosblade.box.dao.model;

import com.alibaba.chaosblade.box.common.infrastructure.constant.ApplicationDimension;
import com.alibaba.chaosblade.box.common.infrastructure.constant.DeviceStatus;
import com.alibaba.chaosblade.box.dao.model.base.ChaosApplicationDeviceDO;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * @author haibin
 *
 *
 */
@TableName("t_chaos_application_device")
public class ApplicationDeviceDO extends ChaosApplicationDeviceDO {

    public boolean isAlive() {
        return DeviceStatus.ONLINE.getStatus() == this.getStatus();
    }

    public boolean isPod() {
        return ApplicationDimension.POD.getValue().equals(this.getDimension());
    }
}
