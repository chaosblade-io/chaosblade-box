package com.alibaba.chaosblade.box.common.app.sdk.annotations;


import com.alibaba.chaosblade.box.common.app.sdk.argument.ArgumentTypeConverter;

import java.lang.annotation.*;

/**
 * @author sunju
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Documented
public @interface ChaosArgs {

    String alias() default "";

    String name() default "";

    String description() default "";

    Class<? extends ArgumentTypeConverter>[] converters() default {};

    Class<?> type() default void.class;





}
