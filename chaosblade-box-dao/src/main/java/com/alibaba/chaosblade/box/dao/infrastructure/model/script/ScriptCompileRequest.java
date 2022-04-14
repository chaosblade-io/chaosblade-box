package com.alibaba.chaosblade.box.dao.infrastructure.model.script;

import lombok.Data;

/**
 * @author haibin
 *
 *
 */
@Data
public class ScriptCompileRequest {

    private String functionId;

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
}
