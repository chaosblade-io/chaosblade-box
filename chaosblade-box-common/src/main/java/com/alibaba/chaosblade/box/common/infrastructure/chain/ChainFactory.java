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

package com.alibaba.chaosblade.box.common.infrastructure.chain;

import com.alibaba.chaosblade.box.common.infrastructure.util.SpringUtils;
import java.util.Iterator;
import java.util.List;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;

/** @author haibin */
public class ChainFactory {

  /**
   * 生成chain，顺序越小，调用越靠前
   *
   * @param chainClass
   * @param <T>
   * @return
   */
  public static <T extends ChainNode> T createSpringChain(
      ApplicationContext applicationContext, Class<T> chainClass) {
    List<T> chainNodes = SpringUtils.getBeans(applicationContext, chainClass);
    AnnotationAwareOrderComparator.sort(chainNodes);
    return createChain(chainNodes);
  }

  public static <T extends ChainNode> T createChain(List<T> chainNodes) {
    T chainNode = null;
    Iterator<T> iterator = chainNodes.iterator();
    T preNode = null;
    while (iterator.hasNext()) {
      T node = iterator.next();
      if (chainNode == null) {
        chainNode = node;
      } else {
        preNode.setNextChainNode(node);
      }
      preNode = node;
    }
    return chainNode;
  }
}
