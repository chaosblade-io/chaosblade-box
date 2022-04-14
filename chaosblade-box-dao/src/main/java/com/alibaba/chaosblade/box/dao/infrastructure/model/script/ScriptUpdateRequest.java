package com.alibaba.chaosblade.box.dao.infrastructure.model.script;

import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import lombok.Data;

import java.io.Serializable;

/**
 * @author haibin
 *
 *
 */
@Data
public class ScriptUpdateRequest implements Serializable {

    private String functionId;

    /**
     * 用户
     */
    private ChaosUser user;

    /**
     * 脚本id
     */
    private String scriptId;

    /**
     * 脚本内容
     */
    private String scriptContent;

    /**
     * 脚本语言
     */
    private String language;

    /**
     * 是否编译
     */
    private boolean compile;
}
