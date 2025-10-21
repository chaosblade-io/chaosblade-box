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

package com.alibaba.chaosblade.box.service.impl;

import com.alibaba.chaosblade.box.common.app.sdk.scope.Host;
import com.alibaba.chaosblade.box.dao.model.ApplicationDeviceDO;
import com.alibaba.chaosblade.box.dao.repository.ApplicationDeviceRepository;
import com.alibaba.chaosblade.box.service.MiniAppParamOptionsService;
import com.alibaba.chaosblade.box.service.infrastructure.convertor.ApplicationDeviceConvertor;
import com.alibaba.chaosblade.box.service.infrastructure.miniapp.MiniAppParamOption;
import com.alibaba.chaosblade.box.service.infrastructure.miniapp.MiniAppParamOptionsProvider;
import com.alibaba.chaosblade.box.service.infrastructure.miniapp.options.DiskMiniAppParamsOptionsProvider;
import com.alibaba.chaosblade.box.service.infrastructure.miniapp.options.NetworkMiniAppParamsOptionsProvider;
import com.alibaba.chaosblade.box.service.model.param.ParamOptionsQueryRequest;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

/** @author haibin */
@Slf4j
@Component
public class CloudMiniAppParamOptionsServiceImpl implements MiniAppParamOptionsService {

  private static Integer MAX_HOST_LIMIT = 20;

  @Autowired private List<MiniAppParamOptionsProvider> miniAppParamOptionsProviders;

  @Autowired private ApplicationDeviceRepository applicationDeviceRepository;

  @Autowired private ApplicationDeviceConvertor applicationDeviceConverter;

  @Override
  public Set<String> queryNetworkDevice(ParamOptionsQueryRequest paramOptionsQueryRequest) {
    paramOptionsQueryRequest.setAlias(NetworkMiniAppParamsOptionsProvider.PARAMS_NETWORK_INTERFACE);
    return queryParamOptions(paramOptionsQueryRequest);
  }

  @Override
  public Set<String> queryDiskDevice(ParamOptionsQueryRequest paramOptionsQueryRequest) {
    paramOptionsQueryRequest.setAlias(DiskMiniAppParamsOptionsProvider.PARAMS_DISK_PATH);
    return queryParamOptions(paramOptionsQueryRequest);
  }

  @Override
  public Set<String> queryDiskBlockDevice(ParamOptionsQueryRequest paramOptionsQueryRequest) {
    return new HashSet<>();
  }

  @Override
  public Set<String> queryParamOptions(ParamOptionsQueryRequest paramOptionsQueryRequest) {
    if (paramOptionsQueryRequest.getHosts().isEmpty()) {
      getHostsById(paramOptionsQueryRequest);
    }
    // 这里需要处理一下,用户没有选择分组机器的情况
    paramOptionsQueryRequest.setHosts(
        limitHosts(paramOptionsQueryRequest.getHosts(), paramOptionsQueryRequest.getAppId()));
    List<MiniAppParamOption> result =
        miniAppParamOptionsProviders.stream()
            .filter(
                miniAppParamOptionsProvider ->
                    miniAppParamOptionsProvider.isSupported(paramOptionsQueryRequest))
            .flatMap(
                (Function<MiniAppParamOptionsProvider, Stream<MiniAppParamOption>>)
                    miniAppParamOptionsProvider ->
                        Optional.ofNullable(
                            miniAppParamOptionsProvider.provide(paramOptionsQueryRequest))
                            .orElse(new ArrayList<>()).stream())
            .collect(Collectors.toList());
    return result.stream().map(MiniAppParamOption::getOption).collect(Collectors.toSet());
  }

  private List<Host> limitHosts(List<Host> hosts, String appId) {
    if (CollectionUtils.isEmpty(hosts) && !Strings.isNullOrEmpty(appId)) {
      ApplicationDeviceDO applicationDeviceDO =
          applicationDeviceRepository.findAnyOneByAppId(appId);
      if (applicationDeviceDO == null) {
        return new ArrayList<>();
      }
      Host host = new Host();
      host.setAppConfigurationId(applicationDeviceDO.getConfigurationId());
      host.setClusterId(applicationDeviceDO.getClusterId());
      host.setDeviceConfigurationId(applicationDeviceDO.getHostConfigurationId());
      host.setKubNamespace(applicationDeviceDO.getKubNamespace());
      host.setK8s(applicationDeviceDO.isPod());
      host.setIp(applicationDeviceDO.getPrivateIp());
      return Lists.newArrayList(host);
    }
    return hosts.stream().limit(MAX_HOST_LIMIT).collect(Collectors.toList());
  }

  private void getHostsById(ParamOptionsQueryRequest paramOptionsQueryRequest) {
    List<ApplicationDeviceDO> applicationDeviceDOs =
        applicationDeviceRepository.findByConfigurationIds(
            paramOptionsQueryRequest.getConfigurationIds());
    paramOptionsQueryRequest.setHosts(
        applicationDeviceDOs.stream()
            .map(applicationDeviceDO -> applicationDeviceConverter.convert(applicationDeviceDO))
            .filter(Objects::nonNull)
            .collect(Collectors.toList()));
  }
}
