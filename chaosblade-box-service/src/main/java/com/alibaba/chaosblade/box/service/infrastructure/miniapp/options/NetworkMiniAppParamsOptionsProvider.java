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

package com.alibaba.chaosblade.box.service.infrastructure.miniapp.options;

import com.alibaba.chaosblade.box.common.app.sdk.scope.Host;
import com.alibaba.chaosblade.box.common.common.domain.chaosblade.requst.ChaosBladeHostQueryRequest;
import com.alibaba.chaosblade.box.common.common.domain.response.Response;
import com.alibaba.chaosblade.box.dao.infrastructure.app.chaosblade.ChaosBladeInvoker;
import com.alibaba.chaosblade.box.service.infrastructure.miniapp.MiniAppParamOption;
import com.alibaba.chaosblade.box.service.infrastructure.miniapp.MiniAppParamOptionsProvider;
import com.alibaba.chaosblade.box.service.model.param.ParamOptionsQueryRequest;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** @author haibin.lhb */
@Component
@Slf4j
public class NetworkMiniAppParamsOptionsProvider implements MiniAppParamOptionsProvider {

  public static String PARAMS_NETWORK_INTERFACE = "network-interface";

  @Autowired private ChaosBladeInvoker chaosBladeInvoker;

  @Override
  public List<MiniAppParamOption> provide(ParamOptionsQueryRequest paramOptionsQueryRequest) {
    return loopInHosts(
        paramOptionsQueryRequest.getHosts(),
        (Function<Host, List<MiniAppParamOption>>)
            host -> {
              ChaosBladeHostQueryRequest chaosBladeHostQueryRequest =
                  new ChaosBladeHostQueryRequest(host);
              Response<String[]> response =
                  chaosBladeInvoker.getNetworkDevice(chaosBladeHostQueryRequest);
              if (response.isSuccess()) {
                log.info(
                    "invoke chaosBlade for getNetworkDeviceForCloud success,result:{}",
                    JSON.toJSONString(response));
                return Sets.newHashSet(response.getResult()).stream()
                    .map(MiniAppParamOption::new)
                    .collect(Collectors.toList());
              } else {
                log.error(
                    "invoke chaosBlade for getNetworkDeviceForCloud failed,code:{},error:{},host:{}",
                    response.getCode(),
                    response.getError(),
                    host.getIp());
                return Lists.newArrayList();
              }
            });
  }

  @Override
  public boolean isSupported(ParamOptionsQueryRequest paramOptionsQueryRequest) {
    return PARAMS_NETWORK_INTERFACE.equals(paramOptionsQueryRequest.getAlias());
  }
}
