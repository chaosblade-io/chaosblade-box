package com.alibaba.chaosblade.box.common.config;

import com.alibaba.chaosblade.box.common.app.sdk.argument.ArgumentTypeConverter;
import lombok.Data;

import java.lang.reflect.Field;
import java.util.Objects;

/**
 * @author haibin.lhb
 * 
 * 
 */
@Data
public class ChaosSettingInfo {

    /**
     * 当前值
     */
    private String value;

    private String preValue;

    /**
     * 默认值
     */
    private String defaultValue;

    /**
     * 描述信息
     */
    private String description;

    /**
     * 名称
     */
    private String name;

    /**
     * 默认启用
     */
    private boolean enabled = true;

    private String group;

    private String priority;

    private transient OwnerInfo owner;

    /**
     * 不是动态的参数不会更新
     */
    private boolean dynamic;

    public boolean valueChanged() {
        return !Objects.equals(value, preValue);
    }

    public void updateOwnerValue() throws IllegalAccessException {
        if (owner == null) { return; }
        Object object = owner.object;
        Field filed = owner.field;
        if (object == null || filed == null) { return; }
        Object result = value;
        if (owner.converter != null && value != null) {
            result = owner.converter.convert(value);
        }
        filed.setAccessible(true);
        filed.set(object, result);
    }

    @Data
    public static class OwnerInfo {

        private Object object;

        private Field field;

        private ArgumentTypeConverter converter;
    }
}
