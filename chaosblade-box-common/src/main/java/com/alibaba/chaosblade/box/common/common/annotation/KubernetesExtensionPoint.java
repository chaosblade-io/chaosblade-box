package com.alibaba.chaosblade.box.common.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.stereotype.Component;

/** @author haibin.lhb */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ExtensionPoint
@Component
public @interface KubernetesExtensionPoint {}
