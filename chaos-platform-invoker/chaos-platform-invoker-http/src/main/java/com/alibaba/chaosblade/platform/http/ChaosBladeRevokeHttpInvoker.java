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

package com.alibaba.chaosblade.platform.http;

import com.alibaba.chaosblade.platform.cmmon.enums.DeviceType;
import com.alibaba.chaosblade.platform.http.constant.Blade;
import com.alibaba.chaosblade.platform.http.model.reuest.HttpChannelRequest;
import com.alibaba.chaosblade.platform.invoker.ChaosInvokerStrategy;
import com.alibaba.chaosblade.platform.invoker.ResponseCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

import static com.alibaba.chaosblade.platform.http.constant.Header.CMD;

/**
 * @author yefei
 */
@Slf4j
@Component
@ChaosInvokerStrategy(deviceType = DeviceType.HOST, sceneCode = "chasobalde.revoke")
public class ChaosBladeRevokeHttpInvoker extends AbstractHttpInvoker {

    @Override
    public CompletableFuture<ResponseCommand> invoke(HttpChannelRequest requestCommand) {
        requestCommand.setTimeout(5 * 60 * 1000L);

        requestCommand.addParam(CMD, Blade.REVOKE + " " + requestCommand.getName());
        return super.invoke(requestCommand);
    }
}
