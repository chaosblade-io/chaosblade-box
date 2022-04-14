package com.alibaba.chaosblade.box.dao.infrastructure.model.script;

import lombok.Data;

/**
 * @author haibin
 *
 *
 */
@Data
public class ScriptRollbackRequest {

    private String userId;

    private String scriptId;

    private Integer fromVersion;

    private Integer targetVersion;

}
