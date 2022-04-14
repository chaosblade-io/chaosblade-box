package com.alibaba.chaosblade.box.common.app.sdk.annotations;

import java.lang.annotation.*;

/**
 * @author sunju
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface ChaosListener {

    String code();

    String name() default "";

    String description() default "";

}
