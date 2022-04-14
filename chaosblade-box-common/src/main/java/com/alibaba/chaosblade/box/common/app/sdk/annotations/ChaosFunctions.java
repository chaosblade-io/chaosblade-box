package com.alibaba.chaosblade.box.common.app.sdk.annotations;


import com.alibaba.chaosblade.box.common.app.sdk.InvokeMode;

import java.lang.annotation.*;

/**
 * @author haibin.lhb
 * 
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface ChaosFunctions {

    ChaosFunction[] values();

    String description() default "";

    boolean agentRequired() default false;

    InvokeMode mode() default InvokeMode.EACH;
}
