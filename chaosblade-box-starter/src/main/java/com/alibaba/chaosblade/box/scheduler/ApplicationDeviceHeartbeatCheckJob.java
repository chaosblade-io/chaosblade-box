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

package com.alibaba.chaosblade.box.scheduler;

import com.alibaba.chaosblade.box.dao.repository.ApplicationDeviceRepository;
import com.alibaba.chaosblade.box.dao.scheduler.SchedulerJob;
import com.alibaba.chaosblade.box.dao.scheduler.quartz.BaseJob;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

/**
 * ApplicationDevice 心跳超时检查定时任务
 * 定期检查 ApplicationDevice（包括Pod）的心跳时间，将超时的设备设置为离线状态
 *
 * @author changjun.xcj
 */
@Slf4j
@DisallowConcurrentExecution
@SchedulerJob(name = "ApplicationDeviceHeartbeatCheckJob", cronExpression = "0 */1 * * * ?")
public class ApplicationDeviceHeartbeatCheckJob extends BaseJob implements Job {

  /** 心跳超时时间（毫秒），默认5分钟 */
  @Value("${chaosblade.application.device.heartbeat.expire.time:300000}")
  private Long heartbeatExpireTime;

  @Autowired private ApplicationDeviceRepository applicationDeviceRepository;

  @Override
  public void execute(JobExecutionContext context) throws JobExecutionException {
    long startTime = System.currentTimeMillis();
    log.debug("[ApplicationDeviceHeartbeatCheckJob] start, heartbeatExpireTime: {}ms", heartbeatExpireTime);

    try {
      // 将心跳超时的 ApplicationDevice 设置为离线
      Integer offlineCount =
          applicationDeviceRepository.setStatusOffLineWhenHealthTimeIntervalGt(heartbeatExpireTime);

      if (offlineCount != null && offlineCount > 0) {
        log.info(
            "[ApplicationDeviceHeartbeatCheckJob] set {} ApplicationDevices to offline due to heartbeat timeout",
            offlineCount);
      } else {
        log.debug("[ApplicationDeviceHeartbeatCheckJob] no ApplicationDevices need to be set offline");
      }
    } catch (Exception e) {
      log.error("[ApplicationDeviceHeartbeatCheckJob] execute failed", e);
    } finally {
      long duration = System.currentTimeMillis() - startTime;
      log.debug("[ApplicationDeviceHeartbeatCheckJob] completed in {}ms", duration);
    }
  }
}
