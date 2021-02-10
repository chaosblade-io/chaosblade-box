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
import com.alibaba.chaosblade.platform.http.constant.Blade;
import com.alibaba.chaosblade.platform.http.util.Base64Util;
import lombok.Data;

import java.nio.charset.Charset;
import java.util.Map;

/**
 * @author yefei
 */
@Data
public class DestroyWithoutUidRequest extends ModelRequest {

    @Override
    public String buildCommand() {
        StringBuilder sb = new StringBuilder(Blade.DESTROY);
        if ("node".equalsIgnoreCase(getScope()) ||
                "pod".equalsIgnoreCase(getScope()) ||
                "container".equalsIgnoreCase(getScope()) ||
                "cplus".equalsIgnoreCase(getTarget()) ||
                "tengine".equalsIgnoreCase(getTarget()) ||
                getTarget().startsWith("node-")) {
            throw new UnsupportedOperationException("not support the scope kind");
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
                // 单独处理 jvm script --script-content 参数，需要 base64 编码
                if ("script".equalsIgnoreCase(getAction()) &&
                        "script-content".equalsIgnoreCase(entry.getKey())) {
                    value = Base64Util.encode(value.getBytes(Charset.forName("UTF-8")), false);
                }
                sb.append("--").append(entry.getKey().trim()).append(" ")
                        .append("'").append(base64encodeScriptContent(entry.getKey(), value).trim()).append("'").append(
                        " ");
            }
        }
        return sb.toString();
    }
}
