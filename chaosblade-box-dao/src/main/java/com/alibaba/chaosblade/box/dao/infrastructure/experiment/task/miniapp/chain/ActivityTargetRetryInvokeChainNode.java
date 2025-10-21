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

package com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.miniapp.chain;

import com.alibaba.chaosblade.box.common.infrastructure.chain.ChainNode;

/**
 * activity target实际运行
 *
 * @author haibin
 */
public abstract class ActivityTargetRetryInvokeChainNode
    extends ChainNode<Boolean, ActivityTargetRetryInvokeContext> {

  @Override
  public Boolean invoke(ActivityTargetRetryInvokeContext context) throws Exception {
    if (context.getInvokeReference().isInvoked()) {
      return false;
    }
    return internalExecute(context);
  }

  /**
   * 执行node
   *
   * @param activityTargetRetryInvokeContext
   * @return
   */
  protected abstract boolean internalExecute(
      ActivityTargetRetryInvokeContext activityTargetRetryInvokeContext);
}
