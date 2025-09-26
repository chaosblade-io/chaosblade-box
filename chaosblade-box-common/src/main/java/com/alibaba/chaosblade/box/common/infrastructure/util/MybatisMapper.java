package com.alibaba.chaosblade.box.common.infrastructure.util;

import java.lang.annotation.*;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * Combine Mybatis @Mapper and Spring @Repository for avoid Jetbrains IDEA display error where NONE
 * spring bean mapper used.
 *
 * @author sunju
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Mapper
@Repository
public @interface MybatisMapper {}
