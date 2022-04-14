package com.alibaba.chaosblade.box.dao.infrastructure.model.script;

import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import lombok.Data;

/**
 * @author haibin
 *
 *
 */
@Data
public class ScriptCreateRequest {

    private ChaosUser user;

    private String scriptName;

    private String scriptContent;

    private String scriptId;

    private String language;

    private String appCode;

    private String functionId;

    /**
     * 是否编译
     */
    private boolean compiled;

}
