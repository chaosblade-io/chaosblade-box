package com.alibaba.chaosblade.box.dao.model;

import com.alibaba.chaosblade.box.dao.model.base.BaseDO;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author haibin
 * 
 *
 */
@TableName("t_chaos_script_history")
@Data
public class ScriptHistoryDO extends BaseDO {

    private String scriptId;

    private String userId;

    private String subUserId;

    private Integer version;

    private String scriptContent;

    private String signature;

}
