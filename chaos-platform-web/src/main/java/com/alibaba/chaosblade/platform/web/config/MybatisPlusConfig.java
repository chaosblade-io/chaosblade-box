/*
 * Copyright 1999-2021 Alibaba Group Holding Ltd.
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

package com.alibaba.chaosblade.platform.web.config;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.apache.ibatis.reflection.MetaObject;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement(proxyTargetClass = true)
@Configuration
@MapperScan("com.alibaba.chaosblade.platform.dao.mapper")
public class MybatisPlusConfig {

    private final static String IS_DELETED = "is_deleted";

    private final static String GMT_CREATE = "gmtCreate";

    private final static String GMT_MODIFIED = "gmtModified";

    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

    @Bean
    public MetaObjectHandler autoFillMetaObjectHandler() {
        return new MetaObjectHandler() {
            @Override
            public void insertFill(MetaObject metaObject) {
                if (metaObject.hasGetter(IS_DELETED)) {
                    this.setFieldValByName(IS_DELETED, 0, metaObject);
                }
                if (metaObject.hasGetter(GMT_CREATE) && metaObject.getValue(GMT_CREATE) == null) {
                    this.setFieldValByName(GMT_CREATE, DateUtil.date(), metaObject);
                }
                if (metaObject.hasGetter(GMT_MODIFIED) && metaObject.getValue(GMT_MODIFIED) == null) {
                    this.setFieldValByName(GMT_MODIFIED, DateUtil.date(), metaObject);
                }
            }

            @Override
            public void updateFill(MetaObject metaObject) {
                if (metaObject.hasGetter(GMT_MODIFIED) && metaObject.getValue(GMT_MODIFIED) == null) {
                    this.setFieldValByName(GMT_MODIFIED, DateUtil.date(), metaObject);
                }
            }
        };
    }

}
