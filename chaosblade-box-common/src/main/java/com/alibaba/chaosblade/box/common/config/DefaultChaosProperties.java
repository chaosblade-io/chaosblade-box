package com.alibaba.chaosblade.box.common.config;

import com.alibaba.chaosblade.box.common.config.converter.StringToIntegerArgumentConverter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author haibin.lhb
 *
 *
 */
@Component
public class DefaultChaosProperties implements InitializingBean {

    @Autowired
    private ChaosSettings chaosSettings;

    public Integer getMiniAppMaxBatchSize() {
        return miniAppMaxBatchSize;
    }

    @ChaosSettingDescriptor(group = "common", description = "小程序并发执行的最大数目",
        name = "chaos.miniapp.execution.batch.size",
        converters = StringToIntegerArgumentConverter.class)
    private Integer miniAppMaxBatchSize = Runtime.getRuntime().availableProcessors() * 7;

    @Override
    public void afterPropertiesSet() throws Exception {
        this.chaosSettings.registerSettings(this);
    }
}
