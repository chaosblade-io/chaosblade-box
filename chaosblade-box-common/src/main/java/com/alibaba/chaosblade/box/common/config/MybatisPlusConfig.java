/*
 * Copyright 2025 The ChaosBlade Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alibaba.chaosblade.box.common.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import java.util.Date;
import org.apache.ibatis.reflection.MetaObject;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/** @author haibin */
@EnableTransactionManagement
@Configuration
@MapperScan(
    basePackages = {
      "com.alibaba.chaosblade.box.dao.mapper",
    })
public class MybatisPlusConfig {

  private static String DELETED = "deleted";

  private static String GMT_GREATE = "gmtCreate";

  private static String GMT_MODIFIED = "gmtModified";

  /** 分页插件 */
  @Bean
  public PaginationInterceptor paginationInterceptor() {
    PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
    // 取消mybatis plus自带的分页size最大值500的限制
    paginationInterceptor.setLimit(-1);
    return paginationInterceptor;
  }

  @Bean
  public OptimisticLockerInterceptor optimisticLockerInterceptor() {
    return new OptimisticLockerInterceptor();
  }

  @Bean
  public MetaObjectHandler autoFillMetaObjectHandler() {
    return new MetaObjectHandler() {

      @Override
      public void insertFill(MetaObject metaObject) {
        if (metaObject.hasGetter(DELETED)) {
          this.setFieldValByName(DELETED, 0, metaObject);
        }
        if (metaObject.hasGetter(GMT_GREATE) && metaObject.getValue(GMT_GREATE) == null) {
          this.setFieldValByName(GMT_GREATE, new Date(), metaObject);
        }
        if (metaObject.hasGetter(GMT_MODIFIED) && metaObject.getValue(GMT_MODIFIED) == null) {
          this.setFieldValByName(GMT_MODIFIED, new Date(), metaObject);
        }
      }

      @Override
      public void updateFill(MetaObject metaObject) {
        if (metaObject.hasGetter(GMT_MODIFIED) && metaObject.getValue(GMT_MODIFIED) == null) {
          this.setFieldValByName(GMT_MODIFIED, new Date(), metaObject);
        }
      }
    };
  }
}
