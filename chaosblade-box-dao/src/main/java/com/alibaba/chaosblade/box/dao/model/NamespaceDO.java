package com.alibaba.chaosblade.box.dao.model;

import com.alibaba.chaosblade.box.dao.model.base.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * @author sunju
 *
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@TableName("t_chaos_namespace")
public class NamespaceDO extends BaseDO {

    @TableField(value = "name")
    String name;
    String description;

    @TableField(value = "user_id")
    String userId;

    @TableField(value = "is_delete")
    Integer isDelete;

    String licenseKey;
    String secretKey;
}
