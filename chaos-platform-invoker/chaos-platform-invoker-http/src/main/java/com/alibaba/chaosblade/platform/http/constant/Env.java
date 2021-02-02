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

package com.alibaba.chaosblade.platform.http.constant;

/**
 * @author Changjun Xiao
 */
public interface Env {

    /**
     * -Dchaosblade.env=prod
     */
    String ENV_KEY = "chaosblade.env";
    /**
     * 生成环境
     */
    String PROD = "prod";
    /**
     * 预发环境
     */
    String PRE = "pre";
    /**
     * 日常环境
     */
    String TEST = "test";
    /**
     * oss 文件下载地址 [bucket].oss-[regionId].aliyuncs.com/...[dir]/[file] 例如： ahas-temp-hangzhou.oss-cn-hangzhou
     * .aliyuncs.com/chaosblade/chaosblade-0.0.4-darwin.tar.gz
     */
    String OSS_PATTERN = "https://%s-%s.oss-%s.aliyuncs.com/server/spec/%s/%s";

    String PROD_PREFIX = "ahasoss";
    String PRE_PREFIX = "ahas";
    /**
     * 容器镜像下载地址 registry[-vpc].[regionId].aliyuncs.com/[namespace]/[repository]:[tag] tag 默认是 latest 例如：
     * registry-vpc.cn-hangzhou.aliyuncs.com/chaosblade/chaosblade registry.cn-hangzhou.aliyuncs
     * .com/chaosblade/chaosblade
     */
    String IMAGE_PATTERN = "registry%s.%s.aliyuncs.com/%s/%s:%s";

    String DEFAULT_IMAGE_TAG = "latest";

}
