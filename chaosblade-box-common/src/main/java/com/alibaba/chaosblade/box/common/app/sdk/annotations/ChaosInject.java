package com.alibaba.chaosblade.box.common.app.sdk.annotations;

import java.lang.annotation.*;

/**
 * @author haibin
 *
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface ChaosInject {

    public String beanName() default "";
}
