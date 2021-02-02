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

package com.alibaba.chaosblade.platform.invoker;

import cn.hutool.core.util.ArrayUtil;
import com.alibaba.chaosblade.platform.cmmon.constants.ChaosConstant;
import com.alibaba.chaosblade.platform.cmmon.enums.DeviceType;
import com.alibaba.chaosblade.platform.cmmon.exception.ExceptionMessageEnum;
import com.alibaba.chaosblade.platform.cmmon.utils.Preconditions;
import com.alibaba.chaosblade.platform.cmmon.utils.SceneCodeParseUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yefei
 */
@Component
public class ChaosInvokerStrategyContext implements ChaosInvoker<RequestCommand, ResponseCommand>, BeanPostProcessor {

    private Map<ChaosInvokerStrategy, ChaosInvoker> strategies = new ConcurrentHashMap<>();

    @Override
    public Object postProcessBeforeInitialization(Object o, String s) throws BeansException {
        if (o instanceof ChaosInvoker) {
            ChaosInvokerStrategy strategy = o.getClass().getAnnotation(ChaosInvokerStrategy.class);
            if(strategy != null) {
                strategies.put(strategy, (ChaosInvoker) o);
            }
        }
        return o;
    }

    @Override
    public CompletableFuture<ResponseCommand> invoke(RequestCommand requestCommand) {
        String original = SceneCodeParseUtil.getOriginal(requestCommand.getSceneCode());
        String phase = requestCommand.getPhase();
        String scope = requestCommand.getScope();

        ChaosInvoker<RequestCommand, ResponseCommand> invoker = null;
        for (ChaosInvokerStrategy strategy : strategies.keySet()) {
            if (invoker != null) {
                break;
            }
            if (strategy.value().getName().equals(original)) {
                invoker = strategies.get(strategy);
                if (invoker != null && ArrayUtil.isNotEmpty(strategy.phase())) {
                    if (!ChaosConstant.PHASE_ALL.equals(strategy.phase()[0])){
                        invoker = null;
                        for (String s : strategy.phase()) {
                            if (s.equals(phase)) {
                                invoker = strategies.get(strategy);
                                break;
                            }
                        }
                    }
                }
                if (invoker != null && ArrayUtil.isNotEmpty(strategy.deviceType())) {
                    invoker = null;
                    for (DeviceType deviceType : strategy.deviceType()) {
                        if (deviceType.name().equalsIgnoreCase(scope)) {
                            invoker = strategies.get(strategy);
                            break;
                        }
                    }
                }
            }
        }

        Preconditions.checkNotNull(invoker, ExceptionMessageEnum.INVOKER_NOT_EXISTS);

        return invoker.invoke(requestCommand);
    }
}
