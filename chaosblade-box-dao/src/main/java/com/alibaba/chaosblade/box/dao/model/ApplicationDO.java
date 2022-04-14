package com.alibaba.chaosblade.box.dao.model;

import com.alibaba.chaosblade.box.common.common.enums.AppType;
import com.alibaba.chaosblade.box.dao.model.base.ChaosApplicationDO;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author haibin
 *
 *
 */
@TableName("t_chaos_application")
@Data
public class ApplicationDO extends ChaosApplicationDO {
	
	public boolean isK8S() {
		return AppType.CLUSTER.getType() == this.getAppType();
	}
	
}
