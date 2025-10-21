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

package com.alibaba.chaosblade.box.common.infrastructure.constant;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import java.util.Arrays;
import lombok.Getter;

/** @author sunju */
public enum SystemVersions {
  LINUX(new String[] {"2.6.3", "3.0", "4.0", "5.0"}),

  WINDOWS(new String[] {"Windows XP", "Windows 7", "Windows 8", "Windows 10"});

  @Getter private String[] versions;

  SystemVersions(String[] versions) {
    this.versions = versions;
  }

  public JSONObject json() {
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("systemName", this.name());
    jsonObject.put("versions", new JSONArray(Arrays.asList(this.getVersions())));
    return jsonObject;
  }

  public static JSON toJson() {
    JSONArray jsonArray = new JSONArray();
    for (SystemVersions value : SystemVersions.values()) {
      jsonArray.add(value.json());
    }
    return jsonArray;
  }

  public static String getLowestVersion(SystemVersions systemVersions) {
    String[] versions = systemVersions.getVersions();
    if (null != versions) {
      return Strings.nullToEmpty(versions[0]);
    }
    return "";
  }
}
