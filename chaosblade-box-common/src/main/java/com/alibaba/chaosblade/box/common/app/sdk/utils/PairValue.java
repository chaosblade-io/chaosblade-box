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

package com.alibaba.chaosblade.box.common.app.sdk.utils;

/** @author haibin */
public class PairValue<Left, Right> {

  private Left left;

  private Right right;

  public PairValue(Left left, Right right) {
    this.left = left;
    this.right = right;
  }

  public Left getLeft() {
    return left;
  }

  public Right getRight() {
    return right;
  }

  public void setRight(Right right) {
    this.right = right;
  }
}
