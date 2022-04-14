package com.alibaba.chaosblade.box.common.app.sdk.annotations;

import java.lang.annotation.*;

/**
 * @author sunpeng
 *
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface ChaosDependency {

    boolean required() default false;

    String code();

    int phase();

    /**
     * 0： 前置依赖、  1：后置依赖
     * @link
     * @return
     */
    int type();

}
