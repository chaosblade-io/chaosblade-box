package com.alibaba.chaosblade.box.common.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author haibin
 *
 *
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiParam {

    enum ApiOperation {
        /**
         * 新建
         */
        CREATE,
        /**
         * 编辑，包括更新，删除
         */
        EDIT,

        /**
         * 任意操作都需要
         */
        ALL,

        /**
         * 随意
         */
        ANY

    }

    /**
     * 当前变量非必须
     *
     * @return
     */
    boolean required() default true;

    /**
     * 表明当前字段在什么时候才需要
     *
     * @return
     */
    ApiOperation operation() default ApiOperation.ALL;

}
