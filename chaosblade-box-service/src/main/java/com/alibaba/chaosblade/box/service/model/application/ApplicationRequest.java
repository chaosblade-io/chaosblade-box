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

package com.alibaba.chaosblade.box.service.model.application;

import com.alibaba.chaosblade.box.dao.page.PageQuery;
import lombok.Data;

/**
 * @author yefei
 */
@Data
public class ApplicationRequest extends PageQuery {

    private Long appId;

    private Long groupId;

    private String appName;

    private String groupName;

    private String hostname;

    private String ip;

    private byte type;

    private Boolean chaosed;

    private String original;
}
