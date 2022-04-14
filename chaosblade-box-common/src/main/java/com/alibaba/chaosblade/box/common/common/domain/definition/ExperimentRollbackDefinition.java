package com.alibaba.chaosblade.box.common.common.domain.definition;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * @author sunju
 *
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExperimentRollbackDefinition {

    @JSONField(name = "force_recover")
    Boolean forceRecover;

    @JSONField(name = "force_reboot")
    Boolean forceReboot;

    @JSONField(name = "force_offline")
    Boolean forceOffline;

}
