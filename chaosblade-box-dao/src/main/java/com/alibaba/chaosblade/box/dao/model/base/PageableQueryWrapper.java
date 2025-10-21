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

package com.alibaba.chaosblade.box.dao.model.base;

import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

/** @author sunju */
@Data
@Accessors(fluent = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor(staticName = "of")
public class PageableQueryWrapper<T> {

  private static final int DEFAULT_PAGE_NUMBER = 1;
  private static final int DEFAULT_PAGE_SIZE = 50;
  public static final int TYPE_ORDER_BY = 0;
  public static final int TYPE_GROUP_BY = 1;

  /** raw query conditions */
  final T query;

  /** page number, default value is 1 */
  int pageNumber;

  /** page size, default value is 50 */
  int pageSize;

  /** order by asc or plainText */
  boolean asc;

  /** columns for order */
  String[] orderBy;

  /** columns for groupBy */
  String[] groupBy;

  public int pageNumber() {
    if (this.pageNumber <= 0L) {
      return DEFAULT_PAGE_NUMBER;
    }
    return this.pageNumber;
  }

  public int pageSize() {
    if (this.pageSize <= 0L) {
      return DEFAULT_PAGE_SIZE;
    }
    return this.pageSize;
  }

  public int offset() {
    return (pageNumber() - 1) * pageSize();
  }

  /**
   * add column for order
   *
   * @param column column name
   */
  public PageableQueryWrapper<T> column(int type, String column) {
    if (TYPE_ORDER_BY == type) {
      this.orderBy = append(this.orderBy, column);
    }
    if (TYPE_GROUP_BY == type) {
      this.groupBy = append(this.groupBy, column);
    }
    return this;
  }

  private String[] append(String[] array, String element) {
    if (null == array) {
      return new String[] {element};
    } else {
      String[] copied = new String[array.length + 1];

      System.arraycopy(array, 0, copied, 0, array.length);
      copied[copied.length - 1] = element;

      return copied;
    }
  }
}
