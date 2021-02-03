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

package com.alibaba.chaosblade.platform.cmmon.model;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author yefei
 */
public class PrometheusValueDeserializer extends JsonDeserializer<List<PrometheusValue>> {

    @Override
    public List<PrometheusValue> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        if (p.currentToken().isStructStart()) {
            TreeNode treeNode = p.readValueAsTree();
            if (treeNode instanceof ArrayNode) {
                ArrayNode arrayNode = (ArrayNode) treeNode;
                List<PrometheusValue> values = new ArrayList<>();
                for (JsonNode jsonNode : arrayNode) {
                    ArrayNode value = (ArrayNode) jsonNode;
                    String timestamp = value.get(0).asText();
                    String data = value.get(1).asText();
                    PrometheusValue prometheusValue = new PrometheusValue(new BigDecimal(timestamp).longValue() * 1000, data);
                    values.add(prometheusValue);
                }
                return values;
            }
        }
        return Collections.emptyList();
    }

}
