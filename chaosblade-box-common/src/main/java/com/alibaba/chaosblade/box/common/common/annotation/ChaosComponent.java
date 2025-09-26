package com.alibaba.chaosblade.box.common.common.annotation;

import java.lang.annotation.*;
import org.springframework.stereotype.Component;

/** @author haibin */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface ChaosComponent {}
