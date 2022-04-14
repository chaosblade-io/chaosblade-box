package com.alibaba.chaosblade.box.dao.model;

import com.alibaba.chaosblade.box.dao.infrastructure.model.Script;
import com.alibaba.chaosblade.box.dao.model.base.BaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author haibin
 *
 *
 */
@TableName("t_chaos_script")
@Data
public class ScriptDO extends BaseDO {

    @TableId(type = IdType.ID_WORKER)
    private String scriptId;

    private String appCode;

    private String functionId;

    private String userId;

    private String subUserId;

    private String scriptContent;

    private Integer version;

    private Boolean isDelete;

    private String name;

    private String signature;

    private String language;

    public static Script toScript(ScriptDO scriptDO) {
        if (scriptDO == null) { return null; }
        return new Script(scriptDO.getScriptId(), scriptDO.getSignature(), scriptDO.getName(),
            scriptDO.getScriptContent(), scriptDO.getLanguage());
    }

}
