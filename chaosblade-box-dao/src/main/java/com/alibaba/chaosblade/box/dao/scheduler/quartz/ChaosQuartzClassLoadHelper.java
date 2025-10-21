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

package com.alibaba.chaosblade.box.dao.scheduler.quartz;

import com.alibaba.chaosblade.box.dao.model.SchedulerJobDO;
import java.util.HashMap;
import java.util.Map;
import org.quartz.simpl.CascadingClassLoadHelper;

/** @author haibin.lhb */
public class ChaosQuartzClassLoadHelper extends CascadingClassLoadHelper {

  private static Map<String, Class<?>> namesConvertor = new HashMap<>();

  static {
    addJobClass(SchedulerJobDO.class);
  }

  public static void addJobClass(Class<?> clazz) {
    namesConvertor.put(clazz.getSimpleName(), clazz);
  }

  public static void addJobClass(String name, Class<?> clazz) {
    namesConvertor.put(name, clazz);
  }

  public static Class<?> getJobClassName(String beanClass) {
    for (Map.Entry<String, Class<?>> classEntry : namesConvertor.entrySet()) {
      if (beanClass.endsWith(classEntry.getKey())) {
        return classEntry.getValue();
      }
    }
    return null;
  }

  @Override
  public void initialize() {
    super.initialize();
  }

  @Override
  public Class<?> loadClass(String name) throws ClassNotFoundException {
    for (Map.Entry<String, Class<?>> classEntry : namesConvertor.entrySet()) {
      if (name.endsWith(classEntry.getKey())) {
        return classEntry.getValue();
      }
    }
    return super.loadClass(name);
  }
}
