package com.alibaba.chaosblade.box.common.config;


import com.alibaba.chaosblade.box.common.app.sdk.argument.ArgumentTypeConverter;

import java.lang.annotation.*;

/**
 * @author haibin.lhb
 *
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ChaosSettingDescriptor {

    enum SettingPriority {
        P1("非常重要"),
        P2("重要"),
        P3("一般");

        SettingPriority(String desc) {

        }
    }

    String name();

    String group();

    /**
     * 描述信息
     *
     * @return
     */
    public String description();

    boolean enabled() default true;

    /**
     * 是否动态
     *
     * @return
     */
    boolean dynamic() default true;

    SettingPriority priority() default SettingPriority.P2;

    Class<? extends ArgumentTypeConverter>[] converters() default {};

}
