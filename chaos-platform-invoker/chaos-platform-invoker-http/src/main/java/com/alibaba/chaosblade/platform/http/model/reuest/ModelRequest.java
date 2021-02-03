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

package com.alibaba.chaosblade.platform.http.model.reuest;

import cn.hutool.core.util.StrUtil;
import com.alibaba.chaosblade.platform.http.util.Base64Util;
import lombok.Data;
import lombok.experimental.Accessors;

import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author Changjun Xiao
 */
@Data
@Accessors(chain = true)
public class ModelRequest extends HttpChannelRequest implements ChaosBladeRequest {

    public final static String SUB_COMMAND = "create";

    /**
     * 实验准备类型
     */
    private String target;

    /**
     * 实验准备子类型，比如 kubernetes jvm， target 是 kubernetes，subTarget 是 jvm
     */
    private String subTarget;

    /**
     * 执行命令的目标信息类型
     */
    private String machineType;

    /**
     * 实验名称
     */
    private String action;

    @Override
    public String buildCommand() {
        StringBuilder sb = new StringBuilder(SUB_COMMAND);
        if ("node".equalsIgnoreCase(getScope()) ||
                "pod".equalsIgnoreCase(getScope()) ||
                "container".equalsIgnoreCase(getScope())) {
            sb.append(" ").append("k8s ").append(getTarget()).append(" ");
        } else {
            sb.append(" ").append(getTarget().trim()).append(" ");
            if (getSubTarget() != null) {
                sb.append(getSubTarget().trim()).append(" ");
            }
        }
        if (getAction() != null) {
            sb.append(getAction().trim()).append(" ");
        }
        // add action flags
        Map<String, String> flags = getArguments();
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
        return sb.toString();
    }

    public String base64encodeScriptContent(String key, String value) {
        if ("filter-script-content".equals(key) || "script-content".equals(key)) {
            return Base64Util.encode(value.getBytes(StandardCharsets.UTF_8), false);
        }
        return value;
    }

}
