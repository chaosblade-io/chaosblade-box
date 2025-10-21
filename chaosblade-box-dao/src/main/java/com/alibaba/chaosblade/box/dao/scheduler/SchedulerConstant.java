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

package com.alibaba.chaosblade.box.dao.scheduler;

/** @author haibin */
public final class SchedulerConstant {

  public static Integer BUSINESS_TYPE_EXPERIMENT_TASK_GUARD = 10;

  public static Integer BUSINESS_TYPE_EXPERIMENT_TASK_STABLE = 11;

  public static Integer BUSINESS_TYPE_JAVA_AGENT = 20;

  public static Integer BUSINESS_TYPE_ACTIVITY_ASYNC = 21;

  public static Integer BUSINESS_TYPE_AUTO_RECOVERY = 30;

  public static Integer BUSINESS_TYPE_EXPERIMENT_STATE_SCHEDULER = 40;

  public static Integer BUSINESS_TYPE_CHAOSBLADE_TASL_AUTO_RECOVERY = 51;

  public static Integer BUSINESS_TYPE_APPLICATION_CHANGE_DISABLED_FALSE = 60;

  public static Integer BUSINESS_TYPE_APPLICATION_CHANGE_DISABLED_TRUE = 70;

  public static String SCHEDULER_PARAM_KET_JOB = "job";

  public static String SCHEDULER_PARAM_KET_JOB_ID = "jobId";
}
