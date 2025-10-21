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

import lombok.experimental.UtilityClass;

/**
 * @author sunju
 *     <p>Modified by jiumu at 2019-07-22 add Scene Log
 */
@UtilityClass
public class ChangelogTypes {

  public interface ChangeType {
    String getName();

    String getDesc();
  }

  /** 变更操作 */
  public static enum ChangeActionType implements ChangeType {
    ADD("add", "新增"),
    Update("update", "编辑"),
    DELETE("delete", "删除"),
    RUN("run", "运行"),
    PUSH("push", "推进"),
    RETRY("retry", "重试"),
    STOP("stop", "停止"),
    PURCHASE("purchase", "购买"),
    Trial("trial", "领取体验包");

    private String name;

    private String desc;

    ChangeActionType(String name, String desc) {
      this.name = name;
      this.desc = desc;
    }

    @Override
    public String getName() {
      return this.name;
    }

    @Override
    public String getDesc() {
      return this.desc;
    }
  }

  /** 变更对象 */
  public static enum ChangeTargetType implements ChangeType {
    WORKSPACE("workspace", "演练空间"),
    EXPERIMENT("experiment", "演练"),
    EXPERIMENT_TASK("experiment_task", "演练任务"),
    EXPERTISE("expertise", "演练经验"),
    SCENE("scene", "场景集"),
    ORDER("order", "订单"),
    SCENE_FUNCTION("scene_function", "演练场景");
    private String name;

    private String desc;

    ChangeTargetType(String name, String desc) {
      this.name = name;
      this.desc = desc;
    }

    @Override
    public String getName() {
      return this.name;
    }

    @Override
    public String getDesc() {
      return this.desc;
    }
  }

  /** 变更人员 */
  public static enum ChangeOperatorType implements ChangeType {
    USER("user", "用户"),
    SCHEDULER("scheduler", "定时任务"),
    SYSTEM("system", "系统");
    private String name;

    private String desc;

    ChangeOperatorType(String name, String desc) {
      this.name = name;
      this.desc = desc;
    }

    @Override
    public String getName() {
      return this.name;
    }

    @Override
    public String getDesc() {
      return this.desc;
    }
  }

  /** 变更对象的属性 */
  public static enum ChangePropertyType implements ChangeType {
    OWNER("owner", "归属人"),
    MEMBER("member", "成员"),
    ACTIVITY_TASK("activity_task", "节点"),
    EXPERIMENT("experiment", "演练"),
    EXPERIMENT_TASK("experiment_task", "演练任务"),
    PARAMETER("parameter", "参数"),
    AUTHORIZED("authorized", "权限");
    private String name;

    private String desc;

    ChangePropertyType(String name, String desc) {
      this.name = name;
      this.desc = desc;
    }

    @Override
    public String getName() {
      return this.name;
    }

    @Override
    public String getDesc() {
      return this.desc;
    }
  }
}
