package com.alibaba.chaosblade.box.dao.model;

import com.alibaba.chaosblade.box.dao.model.base.BaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * t_chaos_application_account_relation
 * @author 
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_chaos_application_account_relation")
public class ApplicationAccountRelationDO extends BaseDO implements Serializable {

    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 修改时间
     */
    private Date gmtModified;

    /**
     * 关联关系ID
     */
    @TableField("relation_id")
    @TableId(type = IdType.ID_WORKER_STR)
    private String relationId;

    /**
     * 演练应用ID
     */
    private Long applicationId;

    /**
     * 账号ID
     */
    private String accountId;

    /**
     * 授权标识，用户细分权限粒度，暂时可能用不到
     */
    private String policy;

    /**
     * 正常/禁用
     */
    private Byte status;

    /**
     * 账号类型
     */
    private String accountType;

    /**
     * 是否删除
     */
    private Byte isDelete;

    private static final long serialVersionUID = 1L;
}