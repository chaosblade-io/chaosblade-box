package com.alibaba.chaosblade.box.auth;

import com.alibaba.chaosblade.box.filter.CrossFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/** @author haibin */
@Slf4j
@Configuration
public class ChaosWebMvcConfigurationSupport extends WebMvcConfigurationSupport {
  @Override
  protected void addResourceHandlers(ResourceHandlerRegistry registry) {
    super.addResourceHandlers(registry);
    registry
        .addResourceHandler(
            "/index.bundle.js",
            "*.js",
            "*.css",
            "*.html",
            "*.ico",
            "*.png",
            "*.jpg",
            "*.gif",
            "*.svg")
        .addResourceLocations("classpath:/build/");
  }

  @Override
  public RequestMappingHandlerMapping requestMappingHandlerMapping() {
    RequestMappingHandlerMapping requestMappingHandlerMapping =
        super.requestMappingHandlerMapping();
    requestMappingHandlerMapping.setOrder(Ordered.LOWEST_PRECEDENCE);
    return requestMappingHandlerMapping;
  }

  @Override
  protected void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
    LoginUserResolver loginUserResolver = getApplicationContext().getBean(LoginUserResolver.class);
    argumentResolvers.add(new UserWebArgumentResolver(loginUserResolver));
  }

  @Bean
  FilterRegistrationBean crossFilter() {
    FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
    filterRegistrationBean.setFilter(new CrossFilter());
    filterRegistrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE + 1);
    filterRegistrationBean.addUrlPatterns("/*");
    filterRegistrationBean.setName("crossFilter");
    return filterRegistrationBean;
  }

  @Override
  protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
    List<HttpMessageConverter<?>> httpMessageConverters = new ArrayList<>();
    for (HttpMessageConverter<?> converter : converters) {
      if (converter.getSupportedMediaTypes().stream()
          .anyMatch(MediaType.APPLICATION_JSON::equals)) {
        log.info(
            "exclude HttpMessageConverter:{},support:{},use fastjson",
            converter.getClass().getName(),
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
