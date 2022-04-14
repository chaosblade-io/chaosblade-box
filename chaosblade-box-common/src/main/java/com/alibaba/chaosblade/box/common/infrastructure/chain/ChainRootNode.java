package com.alibaba.chaosblade.box.common.infrastructure.chain;

import java.lang.annotation.*;

/**
 * @author haibin.lhb
 *
 *
 */
@Target({ ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ChainRootNode {
}
