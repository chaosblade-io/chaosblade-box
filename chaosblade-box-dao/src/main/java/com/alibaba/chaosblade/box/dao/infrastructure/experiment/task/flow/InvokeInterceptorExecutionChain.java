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

package com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow;

import com.alibaba.chaosblade.box.common.experiment.task.flow.step.OnceInvoke;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.step.InvokeContext;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.interceptor.InvokeInterceptor;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

/** @author haibin */
@Slf4j
public class InvokeInterceptorExecutionChain<
    T extends InvokeInterceptor<Re, Rs>, Re extends InvokeContext, Rs> {

  private List<T> interceptorList;

  private int interceptorIndex = -1;

  public void addInterceptor(T t) {
    initInterceptorList().add(t);
  }

  public void addInterceptors(List<T> interceptors) {
    if (interceptors != null) {
      initInterceptorList().addAll(interceptors);
    }
  }

  private List<T> initInterceptorList() {
    if (this.interceptorList == null) {
      this.interceptorList = new ArrayList<T>();
    }
    return this.interceptorList;
  }

  boolean applyPreHandle(Re re, Rs rs) {
    if (!CollectionUtils.isEmpty(interceptorList)) {
      for (int i = 0; i < interceptorList.size(); i++) {
        T interceptor = interceptorList.get(i);
        if (!interceptor.preHandle(re, rs)) {
          applyAfterHandle(re, rs, null);
          return false;
        }

        this.interceptorIndex = i;
      }
    }
    return true;
  }

  void applyPostHandle(OnceInvoke<Re, Rs> onceInvoke, Re re, Rs rs) {
    if (!CollectionUtils.isEmpty(interceptorList)) {
      for (int i = interceptorList.size() - 1; i >= 0; i--) {
        T interceptor = interceptorList.get(i);
        interceptor.postHandle(onceInvoke, re, rs);
      }
    }
  }

  void applyAfterHandle(Re re, Rs rs, Throwable throwable) {
    if (!CollectionUtils.isEmpty(interceptorList)) {
      for (int i = interceptorList.size() - 1; i >= 0; i--) {
        T interceptor = interceptorList.get(i);
        try {
          interceptor.afterHandle(re, rs, throwable);
        } catch (Throwable throwable2) {
          interceptor.afterHandle(re, rs, throwable2);
        }
      }
    }
  }
}
