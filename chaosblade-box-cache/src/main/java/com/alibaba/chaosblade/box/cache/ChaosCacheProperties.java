package com.alibaba.chaosblade.box.cache;

import com.alibaba.chaosblade.box.cache.config.DistributedCacheConfig;
import com.alibaba.chaosblade.box.cache.config.LocalCacheConfig;
import java.util.Map;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Author: sunju
 *
 * <p>Date: 2019/11/7
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@ConfigurationProperties(prefix = "chaos.cache")
public class ChaosCacheProperties {

  boolean enable;
  boolean autoSync;
  boolean suppressException = true;
  LocalCacheConfig localCache;
  DistributedCacheConfig distributedCache;

  /**
   * Spring cache configuration. Such as enable, etc. e.g Set 'chaos.cache.spring.enable' as 'true'
   * to enable spring cache supported.
   */
  Map<String, String> spring;
}
