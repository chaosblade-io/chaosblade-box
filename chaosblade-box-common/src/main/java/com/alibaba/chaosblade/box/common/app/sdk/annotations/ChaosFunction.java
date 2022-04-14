package com.alibaba.chaosblade.box.common.app.sdk.annotations;

import com.alibaba.chaosblade.box.common.app.sdk.InvokeMode;
import com.alibaba.chaosblade.box.common.app.sdk.SupportScope;
import com.alibaba.chaosblade.box.common.app.sdk.constants.PhaseType;

import java.lang.annotation.*;

/**
 * @author sunju
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface ChaosFunction {

    String code();

    String name();

    String description() default "";

    boolean agentRequired() default false;

    InvokeMode mode() default InvokeMode.EACH;

    /**
     * 小程序的前后依赖
     *
     * @return
     */
    ChaosDependency[] dependencies() default {};

    /**
     * 支持的阶段
     *
     * @return
     */
    PhaseType[] phases() default {};

    String[] categories() default {};

    SupportScope[] scopes() default {};

}
