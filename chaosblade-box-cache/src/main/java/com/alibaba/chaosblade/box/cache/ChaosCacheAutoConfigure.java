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

package com.alibaba.chaosblade.box.cache;

import com.alibaba.chaosblade.box.cache.spring.SpringChaosCacheManager;
import com.alibaba.chaosblade.box.cache.templates.ChaosDistributedCacheTemplate;
import com.alibaba.chaosblade.box.cache.templates.ChaosLocalCacheTemplate;
import com.alibaba.chaosblade.box.cache.templates.distributed.RedisCacheTemplate;
import com.alibaba.chaosblade.box.cache.templates.local.HeapCacheTemplate;
import com.alibaba.chaosblade.box.cache.templates.local.OffHeapMemoryCacheTemplate;
import java.io.Serializable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableConfigurationProperties({ChaosCacheProperties.class})
@ConditionalOnProperty(name = "chaos.cache.enable", havingValue = "true")
public class ChaosCacheAutoConfigure {

  @Autowired private ChaosCacheProperties cacheProperties;

  @Bean
  @ConditionalOnMissingBean
  public ChaosCacheManager chaosCacheManager() {
    return new ChaosCacheManager(cacheProperties);
  }

  @Bean
  @ConditionalOnMissingBean({ChaosLocalCacheTemplate.class})
  @ConditionalOnProperty(name = "chaos.cache.local-cache.name", havingValue = "heap")
  public HeapCacheTemplate heapCacheTemplate() {
    return new HeapCacheTemplate(cacheProperties);
  }

  @Bean
  @ConditionalOnMissingBean({ChaosLocalCacheTemplate.class})
  @ConditionalOnProperty(name = "chaos.cache.local-cache.name", havingValue = "off-heap")
  public OffHeapMemoryCacheTemplate offHeapMemoryCacheTemplate() {
    return new OffHeapMemoryCacheTemplate(cacheProperties);
  }

  @Bean
  @ConditionalOnMissingBean({ChaosDistributedCacheTemplate.class})
  @ConditionalOnProperty(name = "chaos.cache.distributed-cache.name", havingValue = "redis")
  public RedisCacheTemplate redisCacheTemplate() {
    return new RedisCacheTemplate(cacheProperties);
  }

  @Bean
  @ConditionalOnProperty(name = "chaos.cache.distributed-cache.name", havingValue = "redis")
  public RedisTemplate<String, Serializable> redisTemplate(
      RedisConnectionFactory redisConnectionFactory) {
    RedisTemplate<String, Serializable> template = new RedisTemplate<>();
    template.setConnectionFactory(redisConnectionFactory);
    template.setKeySerializer(new StringRedisSerializer());
    template.setValueSerializer(new JdkSerializationRedisSerializer());
    return template;
  }

  @Bean
  @ConditionalOnProperty(name = "chaos.cache.spring.enable", havingValue = "true")
  public CacheManager cacheManager() {
    return new SpringChaosCacheManager();
  }
}
