package com.alibaba.chaosblade.box.common.app.sdk.annotations;


import com.alibaba.chaosblade.box.common.app.sdk.constants.ChaosAppType;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface ChaosApplication {

    String code();

    String name();

    String description() default "";

    String version() default "1.0.2";

    ChaosAppType type() default ChaosAppType.CHAOS_APP;



}
