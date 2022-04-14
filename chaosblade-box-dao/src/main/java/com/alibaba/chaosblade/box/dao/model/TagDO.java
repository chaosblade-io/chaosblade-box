package com.alibaba.chaosblade.box.dao.model;

import com.alibaba.chaosblade.box.dao.model.base.BaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
@TableName("t_chaos_tag")
public class TagDO extends BaseDO {

    public static final int TAG_TYPE_EXPERIMENT = 0;

    public static final int TAG_TYPE_SCENE_FUNCTION = 1;

    public static final int TAG_TYPE_SCENE_FUNCTION_CATEGORY = 2;

    public static final int TAG_TYPE_EXPERTISE = 3;

    public static final int TAG_TYPE_APPLICATION_HOST = 4;

    @TableId(type = IdType.ID_WORKER_STR)
    String tagId;

    String name;

    String code;

    Integer type;

    String userId;

}
