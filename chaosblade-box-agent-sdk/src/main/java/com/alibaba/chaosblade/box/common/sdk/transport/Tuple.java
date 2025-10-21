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

/** @author changjun.xcj */
public class Tuple<L, R> {

  private L l;
  private R r;

  public Tuple(L l, R r) {
    this.l = l;
    this.r = r;
  }

  public L getL() {
    return l;
  }

  public void setL(L l) {
    this.l = l;
  }

  public R getR() {
    return r;
  }

  public void setR(R r) {
    this.r = r;
  }
}
