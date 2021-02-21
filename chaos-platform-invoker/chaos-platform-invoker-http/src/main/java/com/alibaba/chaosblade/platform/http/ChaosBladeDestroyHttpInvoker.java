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

import cn.hutool.core.util.StrUtil;
import com.alibaba.chaosblade.platform.cmmon.constants.ChaosConstant;
import com.alibaba.chaosblade.platform.cmmon.enums.DeviceType;
import com.alibaba.chaosblade.platform.cmmon.utils.SceneCodeParseUtil;
import com.alibaba.chaosblade.platform.http.constant.Blade;
import com.alibaba.chaosblade.platform.http.model.reuest.HttpChannelRequest;
import com.alibaba.chaosblade.platform.http.util.Base64Util;
import com.alibaba.chaosblade.platform.invoker.ChaosInvokerStrategy;
import com.alibaba.chaosblade.platform.invoker.ResponseCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static com.alibaba.chaosblade.platform.http.constant.Header.CMD;

/**
 * @author yefei
 */
@Slf4j
@Component
@ChaosInvokerStrategy(deviceType = DeviceType.HOST, phase = ChaosConstant.PHASE_RECOVER)
public class ChaosBladeDestroyHttpInvoker extends AbstractHttpInvoker {

    @Override
    public CompletableFuture<ResponseCommand> invoke(HttpChannelRequest requestCommand) {
        StringBuilder sb = new StringBuilder(Blade.DESTROY);

        if (StrUtil.isNotBlank(requestCommand.getName())) {
            sb.append(" ").append(requestCommand.getName());
        } else {
            String sceneCode = requestCommand.getSceneCode();
            String scope = requestCommand.getScope();
            String target = SceneCodeParseUtil.getTarget(sceneCode);
            String action = SceneCodeParseUtil.getAction(sceneCode);
            if ("node".equalsIgnoreCase(scope) ||
                    "pod".equalsIgnoreCase(scope) ||
                    "container".equalsIgnoreCase(scope) ||
                    "cplus".equalsIgnoreCase(target) ||
                    "tengine".equalsIgnoreCase(target) ||
                    target.startsWith("node-")) {
                throw new UnsupportedOperationException("not support the scope kind");
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
                    // 单独处理 jvm script --script-content 参数，需要 base64 编码
                    if ("script".equalsIgnoreCase(action) &&
                            "script-content".equalsIgnoreCase(entry.getKey())) {
                        value = Base64Util.encode(value.getBytes(StandardCharsets.UTF_8), false);
                    }
                    sb.append("--").append(entry.getKey().trim()).append(" ")
                            .append("'").append(base64encodeScriptContent(entry.getKey(), value).trim()).append("'").append(
                            " ");
                }
            }
        }
        requestCommand.addParam(CMD, sb.toString());
        return super.invoke(requestCommand);
    }

    public String base64encodeScriptContent(String key, String value) {
        if ("filter-script-content".equals(key) || "script-content".equals(key)) {
            return Base64Util.encode(value.getBytes(StandardCharsets.UTF_8), false);
        }
        return value;
    }
}
