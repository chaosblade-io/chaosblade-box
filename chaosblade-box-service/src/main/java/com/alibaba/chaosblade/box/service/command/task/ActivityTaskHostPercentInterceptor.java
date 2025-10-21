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

package com.alibaba.chaosblade.box.service.command.task;

import com.alibaba.chaosblade.box.common.app.sdk.scope.Host;
import com.alibaba.chaosblade.box.common.common.enums.CommonErrorCode;
import com.alibaba.chaosblade.box.common.infrastructure.constant.HostSelectTypes;
import com.alibaba.chaosblade.box.common.infrastructure.domain.Hosts;
import com.alibaba.chaosblade.box.common.infrastructure.exception.ChaosException;
import com.alibaba.chaosblade.box.common.infrastructure.util.CollectionUtil;
import com.alibaba.chaosblade.box.dao.model.ApplicationDeviceDO;
import com.alibaba.chaosblade.box.dao.model.ExperimentActivityDO;
import com.alibaba.chaosblade.box.dao.model.MiniFlowDO;
import com.alibaba.chaosblade.box.dao.model.MiniFlowGroupDO;
import com.alibaba.chaosblade.box.dao.repository.ApplicationDeviceRepository;
import com.alibaba.chaosblade.box.dao.repository.ExperimentMiniFlowGroupRepository;
import com.alibaba.chaosblade.box.dao.repository.ExperimentMiniFlowRepository;
import com.alibaba.chaosblade.box.service.infrastructure.convertor.ApplicationDeviceConvertor;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** @author sunpeng */
@Component
public class ActivityTaskHostPercentInterceptor implements ActivityTaskHostPercentIntercept {

  @Autowired private ExperimentMiniFlowGroupRepository experimentMiniFlowGroupRepository;

  @Autowired private ExperimentMiniFlowRepository experimentMiniFlowRepository;

  @Autowired private ApplicationDeviceRepository applicationDeviceRepository;

  @Autowired private ApplicationDeviceConvertor applicationDeviceConvertor;

  @Override
  public List<ExperimentActivityDO> hostPercent(List<ExperimentActivityDO> activityDOS) {
    List<ExperimentActivityDO> result = new ArrayList<>();
    Map<String, List<ExperimentActivityDO>> stringListMap =
        activityDOS.stream().collect(Collectors.groupingBy(ExperimentActivityDO::getFlowId));
    stringListMap.forEach(
        (s, activities) -> {
          MiniFlowDO miniFlowDO = experimentMiniFlowRepository.findById(s).get();
          MiniFlowGroupDO miniFlowGroupDO =
              experimentMiniFlowGroupRepository.findById(miniFlowDO.getGroupId()).orElseGet(null);
          if (HostSelectTypes.SELECT_TYPE_PERCENT.equals(
              miniFlowGroupDO.getHosts().getSelectType())) {
            int count = getHostCountByPercent(miniFlowGroupDO.getHosts());
            List<Host> hostList = selectHost(count, miniFlowGroupDO.getHosts());
            activities.forEach(
                activityDO -> {
                  activityDO.getActivityDefinition().setScope(hostList);
                });
          }
          result.addAll(activities);
        });
    return result;
  }

  private int getHostCountByPercent(Hosts hosts) {
    int deviceCount = getHostCount(hosts);
    Integer hostPercent = hosts.getHostPercent();
    if (null == hostPercent || hostPercent == 0) {
      return 0;
    }
    float percent = getPercent(hostPercent);
    return getCount(deviceCount, percent);
  }

  private int getHostCount(Hosts hosts) {
    // 只有应用 才会有百分比
    String appId = hosts.getAppId();
    return applicationDeviceRepository.countDeviceByAppIdAndGroupNames(appId, hosts.getAppGroups());
  }

  private float getPercent(Integer hostPercent) {
    BigDecimal a = new BigDecimal(hostPercent);
    BigDecimal b = new BigDecimal(100);
    return a.divide(b).floatValue();
  }

  private int getCount(int hostCount, float percent) {
    BigDecimal a = new BigDecimal(hostCount);
    BigDecimal b = new BigDecimal(percent);
    return a.multiply(b).setScale(0, BigDecimal.ROUND_UP).intValue();
  }

  private List<Host> selectHost(int size, Hosts hosts) {
    // 百分比执行必选要有应用和分组信息，否则执行范围过大
    if (null == hosts.getAppId() || CollectionUtil.isNullOrEmpty(hosts.getAppGroups())) {
      throw new ChaosException(CommonErrorCode.B_PERCENT_HOST_APP_OR_GROUP_NOT_FOUND);
    }
    List<ApplicationDeviceDO> applicationDeviceDOS =
        applicationDeviceRepository.findByAppIdAndGroups(hosts.getAppId(), hosts.getAppGroups());
    if (CollectionUtil.isNullOrEmpty(applicationDeviceDOS)) {
      return new ArrayList<>();
    }

    List<Host> hostList =
        applicationDeviceDOS.stream()
            .map(deviceDO -> applicationDeviceConvertor.convert(deviceDO))
            .collect(Collectors.toList());

    if (size > hostList.size()) {
      return hostList;
    }
    Collections.shuffle(hostList);
    return hostList.subList(0, size);
  }
}
