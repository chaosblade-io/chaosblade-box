package com.alibaba.chaosblade.box.dao.scheduler;

import java.lang.annotation.*;
import org.springframework.stereotype.Component;

/** @author haibin */
@Component
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SchedulerJob {

  String name();

  String cronExpression();

  boolean randomStartDelay() default false;
}
