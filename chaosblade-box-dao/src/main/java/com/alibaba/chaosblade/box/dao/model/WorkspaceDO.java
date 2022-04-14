package com.alibaba.chaosblade.box.dao.model;

import com.alibaba.chaosblade.box.dao.model.base.BaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * Data object of workspace.
 *
 * @author sunju
 * 
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@TableName("chaos_workspace")
public class WorkspaceDO extends BaseDO {

    /**
     * Id of workspace
     */
    @TableField("workspace_id")
    @TableId(type = IdType.ID_WORKER_STR)
    String workspaceId;

    /**
     * Name of workspace
     */
    @TableField("name")
    String name;

    /**
     * Description of workspace
     */
    @TableField("description")
    String description;

    /**
     * Owner's id of workspace
     */
    @TableField("user_id")
    String userId;

    /**
     * Type of workspace
     *
     * @see WorkspaceTypes
     */
    @TableField("type")
    Integer type;

    /**
     * Flag of workspace which mark it enable or not
     * <p>
     * 0 - enable
     * 1 - disable
     */
    @TableField("is_delete")
    Boolean isDelete;

    
}
