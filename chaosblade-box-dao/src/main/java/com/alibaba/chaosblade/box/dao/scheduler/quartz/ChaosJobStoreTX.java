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

package com.alibaba.chaosblade.box.dao.scheduler.quartz;

import java.sql.Connection;
import java.sql.SQLException;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDetail;
import org.quartz.JobPersistenceException;
import org.quartz.TriggerKey;
import org.quartz.spi.OperableTrigger;
import org.springframework.scheduling.quartz.LocalDataSourceJobStore;

/** @author haibin.lhb */
@Slf4j
public class ChaosJobStoreTX extends LocalDataSourceJobStore {

  @Override
  public OperableTrigger retrieveTrigger(TriggerKey triggerKey) throws JobPersistenceException {
    return super.retrieveTrigger(triggerKey);
  }

  @Override
  protected RecoverMisfiredJobsResult recoverMisfiredJobs(Connection conn, boolean recovering)
      throws JobPersistenceException, SQLException {
    return super.recoverMisfiredJobs(conn, recovering);
  }

  @Override
  protected void storeTrigger(
      Connection conn,
      OperableTrigger newTrigger,
      JobDetail job,
      boolean replaceExisting,
      String state,
      boolean forceState,
      boolean recovering)
      throws JobPersistenceException {
    try {
      super.storeTrigger(conn, newTrigger, job, replaceExisting, state, forceState, recovering);
    } catch (Exception exception) {
      log.warn("store retrieve trigger error", exception);
    }
  }

  @Override
  protected OperableTrigger retrieveTrigger(Connection conn, TriggerKey key)
      throws JobPersistenceException {
    try {
      OperableTrigger operableTrigger = super.retrieveTrigger(conn, key);
      if (operableTrigger == null) {
        return null;
      }
      return operableTrigger;
    } catch (Exception exception) {
      log.error("quartz retrieve trigger error", exception);
      return null;
    }
  }

  @Override
  protected boolean updateMisfiredTrigger(
      Connection conn, TriggerKey triggerKey, String newStateIfNotComplete, boolean forceState)
      throws JobPersistenceException {
    return super.updateMisfiredTrigger(conn, triggerKey, newStateIfNotComplete, forceState);
  }
}
