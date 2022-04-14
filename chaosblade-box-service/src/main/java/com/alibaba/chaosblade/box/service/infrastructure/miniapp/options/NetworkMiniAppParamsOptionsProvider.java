package com.alibaba.chaosblade.box.service.infrastructure.miniapp.options;

import com.alibaba.chaosblade.box.common.app.sdk.scope.Host;
import com.alibaba.chaosblade.box.dao.infrastructure.app.chaosblade.ChaosBladeInvoker;
import com.alibaba.chaosblade.box.common.common.domain.chaosblade.requst.ChaosBladeHostQueryRequest;
import com.alibaba.chaosblade.box.common.common.domain.response.Response;
import com.alibaba.chaosblade.box.service.infrastructure.miniapp.MiniAppParamOption;
import com.alibaba.chaosblade.box.service.infrastructure.miniapp.MiniAppParamOptionsProvider;
import com.alibaba.chaosblade.box.service.model.param.ParamOptionsQueryRequest;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author haibin.lhb
 * 
 *
 */
@Component
@Slf4j
public class NetworkMiniAppParamsOptionsProvider implements MiniAppParamOptionsProvider {

    public static String PARAMS_NETWORK_INTERFACE = "network-interface";

    @Autowired
    private ChaosBladeInvoker chaosBladeInvoker;

    @Override
    public List<MiniAppParamOption> provide(ParamOptionsQueryRequest paramOptionsQueryRequest) {
        return loopInHosts(paramOptionsQueryRequest.getHosts(), (Function<Host, List<MiniAppParamOption>>) host -> {
            ChaosBladeHostQueryRequest chaosBladeHostQueryRequest = new ChaosBladeHostQueryRequest(host);
            Response<String[]> response = chaosBladeInvoker.getNetworkDevice(
                    chaosBladeHostQueryRequest);
            if (response.isSuccess()) {
                log.info("invoke chaosBlade for getNetworkDeviceForCloud success,result:{}",
                        JSON.toJSONString(response));
                return Sets.newHashSet(response.getResult()).stream().map(
                        MiniAppParamOption::new).collect(Collectors.toList());
            } else {
                log.error("invoke chaosBlade for getNetworkDeviceForCloud failed,code:{},error:{},host:{}",
                        response.getCode(),
                        response.getError(), host.getIp());
                return Lists.newArrayList();
            }
        });
    }

    @Override
    public boolean isSupported(ParamOptionsQueryRequest paramOptionsQueryRequest) {
        return PARAMS_NETWORK_INTERFACE.equals(paramOptionsQueryRequest.getAlias());
    }
}
