package com.alibaba.chaosblade.box.common.app.sdk.annotations;

import java.lang.annotation.*;

/**
 * @author sunju
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@Documented
public @interface ChaosConfig {

    Class<?> type() default void.class;

}
