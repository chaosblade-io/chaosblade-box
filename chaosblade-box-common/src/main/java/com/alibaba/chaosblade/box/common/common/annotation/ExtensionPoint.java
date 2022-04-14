package com.alibaba.chaosblade.box.common.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author haibin.lhb
 *
 * 
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.SOURCE)
public @interface ExtensionPoint
{
}