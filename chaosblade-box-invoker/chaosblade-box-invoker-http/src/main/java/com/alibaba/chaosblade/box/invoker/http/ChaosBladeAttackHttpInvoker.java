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

package com.alibaba.chaosblade.box.invoker.http;

import cn.hutool.core.util.StrUtil;
import com.alibaba.chaosblade.box.common.constants.ChaosConstant;
import com.alibaba.chaosblade.box.common.enums.DeviceType;
import com.alibaba.chaosblade.box.common.utils.SceneCodeParseUtil;
import com.alibaba.chaosblade.box.invoker.ChaosInvokerStrategy;
import com.alibaba.chaosblade.box.invoker.ResponseCommand;
import com.alibaba.chaosblade.box.invoker.http.constant.Header;
import com.alibaba.chaosblade.box.invoker.http.model.reuest.HttpChannelRequest;
import com.alibaba.chaosblade.box.invoker.http.util.Base64Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @author yefei
 */
@Slf4j
@Component
@ChaosInvokerStrategy(deviceType = DeviceType.HOST, phase = ChaosConstant.PHASE_ATTACK)
public class ChaosBladeAttackHttpInvoker extends AbstractHttpInvoker {

    public final static String SUB_COMMAND = "create";

    @Override
    public CompletableFuture<ResponseCommand> invoke(HttpChannelRequest requestCommand) {
        String sceneCode = requestCommand.getSceneCode();
        String scope = requestCommand.getScope();
        String target = SceneCodeParseUtil.getTarget(sceneCode);
        String action = SceneCodeParseUtil.getAction(sceneCode);

        StringBuilder sb = new StringBuilder(SUB_COMMAND);
        if ("node".equalsIgnoreCase(scope) ||
                "pod".equalsIgnoreCase(scope) ||
                "container".equalsIgnoreCase(scope)) {
            sb.append(" ").append("k8s ").append(target).append(" ");
        } else {
            sb.append(" ").append(target.trim()).append(" ");
        }
        if (action != null) {
            sb.append(action.trim()).append(" ");
        }
        // add action flags
        Map<String, String> flags = requestCommand.getArguments();
        if (flags != null) {
            for (Map.Entry<String, String> entry : flags.entrySet()) {
                String value = entry.getValue();
                if (StrUtil.isBlank(value) || "false".equalsIgnoreCase(value)) {
                    continue;
                }
                sb.append("--").append(entry.getKey().trim()).append(" ")
                        .append("'").append(base64encodeScriptContent(entry.getKey(), value).trim()).append("'").append(
                        " ");
            }
        }

        requestCommand.addParam(Header.CMD, sb.toString());
        return super.invoke(requestCommand);
    }

    public String base64encodeScriptContent(String key, String value) {
        if ("filter-script-content".equals(key) || "script-content".equals(key)) {
            return Base64Util.encode(value.getBytes(StandardCharsets.UTF_8), false);
        }
        return value;
    }
}
