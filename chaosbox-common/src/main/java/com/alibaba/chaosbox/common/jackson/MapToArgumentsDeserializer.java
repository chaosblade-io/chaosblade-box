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

package com.alibaba.chaosbox.common.jackson;

import cn.hutool.core.collection.CollUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author yefei
 */
public class MapToArgumentsDeserializer extends JsonSerializer<Map<String, String>> {

    @Override
    public void serialize(Map<String, String> value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value == null) {
            gen.writeObject(CollUtil.newArrayList());
        } else {
            List<Map<Object, Object>> maps = value.keySet().stream().map(k -> {
                        Map<Object, Object> map = new HashMap<>();
                        map.put("name", k);
                        map.put("value", value.get(k));
                        return map;
                    }
            ).collect(Collectors.toList());
            gen.writeObject(maps);
        }
    }

}
