package com.alibaba.chaosblade.box.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author haibin
 *
 *
 */

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginUser {
}
