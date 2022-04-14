package com.alibaba.chaosblade.box.dao.model;

import com.alibaba.chaosblade.box.common.common.constant.ChaosFunctionConstant;
import com.alibaba.chaosblade.box.dao.model.base.BaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.google.common.collect.Lists;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author sunju
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@TableName("t_chaos_scene_authorized")
public class SceneAuthorizedDO extends BaseDO {

    @TableId(type = IdType.ID_WORKER_STR)
    String authorizedId;

    String functionId;
    String functionName; //冗余字段，与SceneFunctionDO#name保持一致
    String functionCode; //冗余字段，与SceneFunctionDO#code保持一致
    String grantFrom;
    String grantTo;
    Integer permission;
    Integer enabled; //冗余字段，与SceneFunctionDO#enabled保持一致
    Integer phase; //冗余字段，与SceneFunctionDO#phase保持一致
    Integer source; //冗余字段，与SceneFunctionDO#source保持一致
    Date functionCreateTime; // 冗余字段，与SceneFunctionDO#gmtCreate保持一致

    @TableField(exist = false)
    @Deprecated
    List<String> categories; // 冗余字段，与SceneFunctionDO#categories保持一致

    @TableField(exist = false)
    @Deprecated
    String supportScopeTypes; //冗余字段，与SceneFunctionDO#supportScopeTypes保持一致

    Boolean supportHost;
    Boolean supportK8s;

    Boolean isPublic;
    Boolean isDelete;

    public List<Integer> getSupportScopeTypeList() {
        List<Integer> supportScopeTypes = Lists.newArrayList();
        if (this.supportHost) {
            supportScopeTypes.add(ChaosFunctionConstant.SUPPORT_SCOPE_TYPE_HOST);
        }
        if (this.supportK8s) {
            supportScopeTypes.add(ChaosFunctionConstant.SUPPORT_SCOPE_TYPE_K8S);
        }
        return supportScopeTypes;
    }

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
            });
        }
    }

}
