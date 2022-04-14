package com.alibaba.chaosblade.box.auth;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * @author haibin
 *
 *
 */
@Slf4j
@Configuration
public class ChaosWebMvcConfigurationSupport extends WebMvcConfigurationSupport {

    @Override
    protected void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        LoginUserResolver loginUserResolver = getApplicationContext().getBean(LoginUserResolver.class);
        argumentResolvers.add(new UserWebArgumentResolver(loginUserResolver));
    }

    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        List<HttpMessageConverter<?>> httpMessageConverters = new ArrayList<>();
        for (HttpMessageConverter<?> converter : converters) {
            if (converter.getSupportedMediaTypes().stream().anyMatch(
                MediaType.APPLICATION_JSON::equals)) {
                log.info("exclude HttpMessageConverter:{},support:{},use fastjson", converter.getClass().getName(),
                    MediaType.APPLICATION_JSON_VALUE);
            } else {
                httpMessageConverters.add(converter);
            }
        }
        FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(SerializerFeature.DisableCircularReferenceDetect);
        fastJsonHttpMessageConverter.setFastJsonConfig(fastJsonConfig);
        httpMessageConverters.add(httpMessageConverters.size(), fastJsonHttpMessageConverter);
        converters.clear();
        converters.addAll(httpMessageConverters);
    }

}
