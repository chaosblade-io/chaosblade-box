package com.alibaba.chaosblade.box.dao.model;

import com.alibaba.chaosblade.box.common.common.constant.ChaosFunctionConstant;
import com.alibaba.chaosblade.box.dao.infrastructure.model.TreeNode;
import com.alibaba.chaosblade.box.dao.model.base.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Author: sunju
 *
 * Date:   2019/11/7
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@TableName("t_chaos_scene_function_category")
public class SceneFunctionCategoryDO extends BaseDO implements TreeNode<SceneFunctionCategoryDO> {

    /**
     * 默认类目
     */
    public static final int DEFAULT_CATEGORY_TYPE = 0;

    /**
     * 监控策略
     */
    public static final int GLOBAL_MONITOR_CATEGORY_TYPE = 1;

    /**
     * 恢复策略类型
     */
    public static final int GLOBAL_GUARD_ROOT_CATEGORY_TYPE = 2;

    String categoryId;

    String name;
    Integer phase;
    String parentId;
    Integer type;
    Boolean supportHost;
    Boolean supportK8s;
    /**
     * 是否云资源
     */
    Boolean supportCloud;
    Boolean isDelete;
    @TableField(exist = false)
    List<SceneFunctionCategoryDO> children;

    @TableField
    List<Integer> supportOsTypes = new ArrayList<>();

    public void setSupportScopeTypeList(List<Integer> supportScopeTypes) {
        this.supportHost = false;
        this.supportK8s = false;
        if (null != supportScopeTypes && !supportScopeTypes.isEmpty()) {
            supportScopeTypes.forEach(type -> {
                if (Objects.equals(type, ChaosFunctionConstant.SUPPORT_SCOPE_TYPE_HOST)) {
                    this.supportHost = true;
                }
                if (Objects.equals(type, ChaosFunctionConstant.SUPPORT_SCOPE_TYPE_K8S)) {
                    this.supportK8s = true;
                }
                if (Objects.equals(type, ChaosFunctionConstant.SUPPORT_SCOPE_TYPE_CLOUD)) {
                    this.supportCloud = true;
                }
            });
        }
    }

    public static SceneFunctionCategoryDO deepClone(SceneFunctionCategoryDO category) {
        SceneFunctionCategoryDO clone = new SceneFunctionCategoryDO();
        clone.setId(category.getId());
        clone.setGmtCreate(category.getGmtCreate());
        clone.setGmtModified(category.getGmtModified());
        clone.setCategoryId(category.getCategoryId());
        clone.setName(category.getName());
        clone.setPhase(category.getPhase());
        clone.setParentId(category.getParentId());
        clone.setType(category.getType());
        clone.setSupportHost(category.getSupportHost());
        clone.setSupportK8s(category.getSupportK8s());
        clone.setSupportCloud(category.getSupportCloud());
        clone.setIsDelete(category.getIsDelete());
        clone.setChildren(category.getChildren());
        return clone;
    }

}
