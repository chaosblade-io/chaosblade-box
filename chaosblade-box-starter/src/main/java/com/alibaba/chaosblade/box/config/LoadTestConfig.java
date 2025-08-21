package com.alibaba.chaosblade.box.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

/**
 * 压测相关配置
 *
 * @author ZhengMingZhuo
 */
@Configuration
@EnableAsync
@EnableScheduling
public class LoadTestConfig {

    /**
     * 压测引擎专用的RestTemplate
     */
    @Bean(name = "loadTestRestTemplate")
    public RestTemplate loadTestRestTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(10000); // 连接超时10秒
        factory.setReadTimeout(30000);    // 读取超时30秒
        
        return new RestTemplate(factory);
    }
}
