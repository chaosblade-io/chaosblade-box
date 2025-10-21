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

package com.alibaba.chaosblade.box.common.infrastructure;

import com.alibaba.chaosblade.box.common.common.enums.CommonErrorCode;
import com.alibaba.chaosblade.box.common.infrastructure.exception.ChaosException;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/** @author haibin.lhb */
@Component
public class DomainFactory implements ApplicationContextAware {
  private ApplicationContext context;

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.context = applicationContext;
  }

  /**
   * 通过无参数构造器实例化，并给Autowired注解的属性赋值
   *
   * @param tClass 待实例化对象类型
   * @param <T>
   * @return
   */
  public <T extends IChaosDomain> T getBean(Class<T> tClass) {
    try {
      T t = tClass.newInstance();
      return getBean(t);
    } catch (Exception e) {
      throw new ChaosException(
          CommonErrorCode.S_SYSTEM_ERROR,
          "Class new instance error. ClassName=" + tClass.getSimpleName(),
          e);
    }
  }

  /**
   * 给已经实例化对象的Autowired注解的属性赋值
   *
   * @param t
   * @return
   */
  public <T extends IChaosDomain> T getBean(T t) {
    try {
      context.getAutowireCapableBeanFactory().autowireBean(t);
      return t;
    } catch (Exception e) {
      throw new ChaosException(
          CommonErrorCode.S_SYSTEM_ERROR,
          "Field Autowired Error. ClassName=" + t.getClass().getSimpleName(),
          e);
    }
  }
}
