package com.alibaba.chaosblade.box.service.model.scope;

import com.alibaba.chaosblade.box.common.common.enums.StateEnum;
import lombok.Data;

/**
 * @author haibin
 *
 *
 */
@Data
public class ScopeRunningInfo {
    private StateEnum state;
    private String experimentTaskId;
    private Integer total;

}
