package com.alibaba.chaosblade.box.common.infrastructure.lock;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author haibin.lhb
 *
 * 
 */

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface DistributeLock {
    /**
     * Lock name.
     */
    String name() default "";

    /**
     * How long the lock should be kept in case the machine which obtained the lock died before releasing it.
     * This is just a fallback, under normal circumstances the lock is released as soon the tasks finishes.
     *
     * Can be either time with suffix like 10s or ISO8601 duration as described in {@link
     * java.time.Duration#parse(CharSequence)}, for example PT30S.
     */
    String lockAtMostFor() default "";

    /**
     * The lock will be held at least for given duration. Can be used if you really need to execute the task
     * at most once in given period of time. If the duration of the task is shorter than clock difference between nodes,
     * the task can
     * be theoretically executed more than once (one node after another). By setting this parameter, you can make sure
     * that the
     * lock will be kept at least for given period of time.
     *
     * Can be either time with suffix like 10s or ISO8601 duration as described in {@link
     * java.time.Duration#parse(CharSequence)}, for example PT30S.
     */
    String lockAtLeastFor() default "";

    /**
     * lock description
     *
     * @return
     */
    String desc() default "";
}

