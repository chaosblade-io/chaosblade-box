/*
 * Copyright 1999-2021 Alibaba Group Holding Ltd.
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

package com.alibaba.chaosblade.platform.toolsmgr.api;

import com.alibaba.chaosblade.platform.cmmon.DeviceMeta;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yefei
 */
@Component
public class ChaosToolsMgrStrategyContext implements ChaosToolsMgr, BeanPostProcessor {

    private final Map<String, ChaosToolsMgr<Request>> map = new HashMap<>();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof ChaosToolsMgr) {
            ChannelStrategy strategy = bean.getClass().getAnnotation(ChannelStrategy.class);
            if (strategy != null) {
                map.put(strategy.value().name(), (ChaosToolsMgr) bean);
            }
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Response<List<DeviceMeta>> listHosts(Request request) {
        ChaosToolsMgr<Request> chaosToolsMgr = map.get(request.getChannel());
        return chaosToolsMgr.listHosts(request);
    }

    @Override
    public Response<String> deployAgent(Request request) {
        ChaosToolsMgr<Request> chaosToolsMgr = map.get(request.getChannel());
        return chaosToolsMgr.deployAgent(request);
    }

    @Override
    public Response<String> unDeployAgent(Request request) {
        ChaosToolsMgr<Request> chaosToolsMgr = map.get(request.getChannel());
        return chaosToolsMgr.unDeployAgent(request);
    }

    @Override
    public Response<String> deployTools(Request request) {
        ChaosToolsMgr<Request> chaosToolsMgr = map.get(request.getChannel());
        return chaosToolsMgr.deployTools(request);
    }

    @Override
    public Response<String> unDeployTools(Request request) {
        ChaosToolsMgr<Request> chaosToolsMgr = map.get(request.getChannel());
        return chaosToolsMgr.unDeployTools(request);
    }
}
