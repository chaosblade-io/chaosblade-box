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

package com.alibaba.chaosblade.box.service.infrastructure.configuration;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/** @author haibin.lhb */
@Component
public class FileDefaultApplicationConfigurationLoader
    implements DefaultApplicationConfigurationLoader, InitializingBean {

  private String PATH = "application/application_configuration.json";

  private List<ApplicationConfiguration> applicationConfigurations = new ArrayList<>();

  @Override
  public List<ApplicationConfiguration> loadConfigurations() {
    return applicationConfigurations;
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    try (InputStream inputStream =
        Thread.currentThread().getContextClassLoader().getResourceAsStream(PATH)) {
      assert inputStream != null;
      applicationConfigurations =
          JSON.parseObject(
              inputStream, new TypeReference<List<ApplicationConfiguration>>() {}.getType());
      // check not exist,if not equal,has duplicate items,error
      int totalSize =
          applicationConfigurations.stream()
              .map(ApplicationConfiguration::getAlias)
              .collect(Collectors.toSet())
              .size();
      assert totalSize == applicationConfigurations.size();
    }
  }
}
