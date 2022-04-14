package com.alibaba.chaosblade.box.dao.scheduler;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @author haibin
 *
 *
 */
@Component
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SchedulerJob {

    String name();

    String cronExpression();

    boolean randomStartDelay() default false;

}
