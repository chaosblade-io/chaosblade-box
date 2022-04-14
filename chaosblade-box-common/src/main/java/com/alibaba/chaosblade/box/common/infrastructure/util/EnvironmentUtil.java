package com.alibaba.chaosblade.box.common.infrastructure.util;

import com.alibaba.chaosblade.box.common.app.sdk.constants.EnvironmentEnum;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.config.ConfigFileApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * @author haibin
 *
 *
 */
@Component
@Order(value = Ordered.HIGHEST_PRECEDENCE + 1)
public class EnvironmentUtil implements InitializingBean {

    protected EnvironmentEnum environmentEnum;

    @Autowired
    @Value("${chaos.env}")
    public String env;

    @Autowired
    Environment environment;

    @Autowired
    private ApplicationStartUpConfig applicationStartUpConfig;

    public EnvironmentEnum getEnv() {
        return environmentEnum;
    }

    public boolean isTestProfile() {
        return "test".equals(environment.getProperty(ConfigFileApplicationListener.ACTIVE_PROFILES_PROPERTY));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        environmentEnum = EnvironmentEnum.of(env);
        if (environmentEnum == null) {
            throw new IllegalArgumentException("chaos.env is illegal,current env:" + env);
        }
    }

}
