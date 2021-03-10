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

package com.alibaba.chaosblade.box.common.jackson;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yefei
 */
public class ArgumentsToMapDeserializer extends JsonDeserializer<Map<String, String>> {

    @Override
    public Map<String, String> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        if (p.currentToken().isStructStart()) {
            TreeNode treeNode = p.readValueAsTree();
            if (treeNode instanceof ArrayNode) {
                ArrayNode arrayNode = (ArrayNode) treeNode;
                Map<String, String> map = new HashMap<>();
                for (JsonNode jsonNode : arrayNode) {
                    if (jsonNode.get("value") != null && !jsonNode.get("value").isNull()) {
                        String value = jsonNode.get("value").asText();
                        if (StrUtil.isBlank(value)) {
                            value = jsonNode.get("value").toString();
                        }
                        if (StrUtil.isNotBlank(value)) {
                            map.put(jsonNode.get("name").asText(), value);
                        }
                    }
                }
                return map;
            }
        }
        return new HashMap<>(0);
    }
}
