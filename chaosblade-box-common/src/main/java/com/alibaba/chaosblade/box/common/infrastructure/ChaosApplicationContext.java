package com.alibaba.chaosblade.box.common.infrastructure;

import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import com.alibaba.chaosblade.box.common.experiment.task.flow.util.HashMapSettings;
import lombok.Data;

/**
 * @author haibin
 *
 *
 */
@Data
public class ChaosApplicationContext extends HashMapSettings {

    private String requestId;

    private ChaosUser loginUser;

    public String getMainUserId() {
        if (loginUser == null) { return null; }
        return loginUser.getUserId();
    }
}
