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

package com.alibaba.chaosblade.box.common.sdk.transport;

/**
 * Thread Safe
 *
 * @author changjun.xcj
 */
public abstract class RequestInterceptorChain implements RequestInterceptor {
  private RequestInterceptor interceptor;

  /// **
  // * Reverse chain
  // *
  // * @param requestInterceptor
  // * @return
  // */
  // public static RequestInterceptor reverse(RequestInterceptor requestInterceptor) {
  //    if (requestInterceptor == null) {
  //        return requestInterceptor;
  //    }
  //    if (!(requestInterceptor instanceof RequestInterceptorChain)) {
  //        return requestInterceptor;
  //    }
  //    RequestInterceptorChain requestInterceptorChain =
  // (RequestInterceptorChain)requestInterceptor;
  //    if (requestInterceptorChain.getInterceptor() == null) {
  //        return requestInterceptor;
  //    }
  //    RequestInterceptorChain head = requestInterceptorChain;
  //    Stack<RequestInterceptorChain> stack = new Stack<RequestInterceptorChain>();
  //    stack.push(head);
  //
  //    RequestInterceptor next = null;
  //    while ((next = head.getInterceptor()) != null) {
  //        if (next instanceof RequestInterceptorChain) {
  //            head = (RequestInterceptorChain)next;
  //            stack.push(head);
  //        } else {
  //            break;
  //        }
  //    }
  //
  //    RequestInterceptorChain newHead = stack.pop();
  //    RequestInterceptorChain newNext = newHead;
  //    while (!stack.empty()) {
  //        RequestInterceptorChain pop = stack.pop();
  //        newNext.setInterceptor(pop);
  //        newNext = pop;
  //    }
  //    newNext.setInterceptor(next);
  //    return newHead;
  // }

  public RequestInterceptor setInterceptor(RequestInterceptor interceptor) {
    this.interceptor = interceptor;
    return interceptor;
  }

  public RequestInterceptor getInterceptor() {
    return interceptor;
  }

  @Override
  public void handle(Request request) throws RequestException {
    doHandle(request);
    if (interceptor != null) {
      interceptor.handle(request);
    }
  }

  @Override
  public void invoke(Request request) throws RequestException {
    doInvoke(request);
    if (interceptor != null) {
      interceptor.invoke(request);
    }
  }

  /**
   * Real handle method implementation of The interceptor
   *
   * @param request
   * @throws RequestException
   */
  public abstract void doHandle(Request request) throws RequestException;

  /**
   * Real invoke method implementation of The interceptor
   *
   * @param request
   * @throws RequestException
   */
  public abstract void doInvoke(Request request) throws RequestException;
}
