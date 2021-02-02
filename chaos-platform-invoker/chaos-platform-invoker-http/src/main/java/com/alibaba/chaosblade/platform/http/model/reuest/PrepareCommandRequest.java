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
import lombok.Data;

import java.util.Map;

/**
 * @author Changjun Xiao
 */
@Data
public class PrepareCommandRequest extends HttpChannelRequest implements ChaosBladeRequest {

    /**
     * jvm
     */
    private String type;

    @Override
    public String buildCommand() {
        StringBuilder sb = new StringBuilder(Blade.PREPARE);
        sb.append(" ").append(getType()).append(" ");
        Map<String, String> flags = getArguments();
        if (flags != null) {
            for (Map.Entry<String, String> entry : flags.entrySet()) {
                String value = entry.getValue();
                if (StrUtil.isBlank(value) || value.equalsIgnoreCase("false")) {
                    continue;
                }
                sb.append("--").append(entry.getKey().trim()).append(" '")
                        .append(entry.getValue().trim()).append("' ");
            }
        }
        return sb.toString();
    }
}
