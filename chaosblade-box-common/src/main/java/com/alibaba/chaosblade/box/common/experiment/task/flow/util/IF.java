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

package com.alibaba.chaosblade.box.common.experiment.task.flow.util;

import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Lambda-styled 'IF' syntax implementation.
 *
 * <p>Use like: <code>
 *     IF.on(someObject)
 *     .when(obj -> {
 *         //do something
 *         return true; // or false
 *     })
 *     .isTrue(obj -> {
 *         //do something if true
 *     })
 *     .orElse(obj -> {
 *         //do something if false
 *     });
 * </code>
 *
 * @author sunju
 */
public final class IF<T> {

  private final T object;

  private boolean isTrue;

  private IF(T object) {
    this.object = object;
  }

  public static <T> IF<T> on(T object) {
    return new IF<>(object);
  }

  public IF<T> when(Predicate<T> predicate) {
    isTrue = predicate.test(this.object);
    return this;
  }

  public IF<T> isTrue(Consumer<T> consumer) {
    if (isTrue) {
      consumer.accept(this.object);
    }
    return this;
  }

  public void orElse(Consumer<T> consumer) {
    if (!isTrue) {
      consumer.accept(this.object);
    }
  }
}
