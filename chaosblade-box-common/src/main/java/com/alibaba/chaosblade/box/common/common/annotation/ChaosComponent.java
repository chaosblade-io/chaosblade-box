package com.alibaba.chaosblade.box.common.common.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @author haibin
 *
 *
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface ChaosComponent {

}
