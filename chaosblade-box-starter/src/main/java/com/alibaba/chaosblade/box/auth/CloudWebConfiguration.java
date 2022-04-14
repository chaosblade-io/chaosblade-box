package com.alibaba.chaosblade.box.auth;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.handler.MappedInterceptor;

/**
 * @author haibin
 *
 *
 */
public class CloudWebConfiguration extends ChaosWebMvcConfigurationSupport {

    @Bean
    MappedInterceptor adminOperationHandlerInterceptor() {
        return new MappedInterceptor(null,
            new AdminOperationHandlerInterceptor(getApplicationContext().getBean(LoginUserResolver.class)));
    }

    @Bean
    @ConditionalOnMissingBean
    RequestMappingAdvice requestMappingAdvice() {
        return new RequestMappingAdvice();
    }

}
