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

package com.alibaba.chaosblade.box.common.common.enums;

/** @author sunpeng */
public enum DeviceOsType {
  linux(0),

  windows(1);

  private int type;

  DeviceOsType(int type) {
    this.type = type;
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  public static DeviceOsType getByName(String osType) {
    for (DeviceOsType item : DeviceOsType.values()) {
      if (item.name().equalsIgnoreCase(osType)) {
        return item;
      }
    }
    return null;
  }

  public static DeviceOsType getByNameWithDefault(String osType) {
    for (DeviceOsType item : DeviceOsType.values()) {
      if (item.name().equalsIgnoreCase(osType)) {
        return item;
      }
    }
    return DeviceOsType.linux;
  }

  public static DeviceOsType getByType(Integer osType) {
    if (null == osType) {
      return null;
    }
    for (DeviceOsType item : DeviceOsType.values()) {
      if (osType.equals(item.getType())) {
        return item;
      }
    }
    return null;
  }

  public static DeviceOsType getByTypeDefaultLinux(Integer osType) {
    if (null == osType) {
      return DeviceOsType.linux;
    }
    for (DeviceOsType item : DeviceOsType.values()) {
      if (osType.equals(item.getType())) {
        return item;
      }
    }
    return DeviceOsType.linux;
  }

  public static boolean isLinux(String osType) {
    if (DeviceOsType.linux.name().equalsIgnoreCase(osType)) {
      return true;
    }
    return false;
  }

  public static boolean isLinux(Integer osType) {
    if (null == osType) {
      return false;
    }
    if (osType.equals(DeviceOsType.linux.getType())) {
      return true;
    }
    return false;
  }

  public static boolean isWindows(String osType) {
    if (DeviceOsType.windows.name().equalsIgnoreCase(osType)) {
      return true;
    }
    return false;
  }

  public static boolean isWindows(Integer osType) {
    if (null == osType) {
      return false;
    }
    if (osType.equals(DeviceOsType.windows.getType())) {
      return true;
    }
    return false;
  }
}
