package com.alibaba.chaosblade.box.common.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.apache.ibatis.reflection.MetaObject;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Date;

/**
 * @author haibin
 *
 * 
 */
@EnableTransactionManagement
@Configuration
@MapperScan(basePackages = {
    "com.alibaba.chaosblade.box.dao.mapper",
})
public class MybatisPlusConfig {

    private static String DELETED = "deleted";

    private static String GMT_GREATE = "gmtCreate";

    private static String GMT_MODIFIED = "gmtModified";

    /**
     * 分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        //取消mybatis plus自带的分页size最大值500的限制
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
