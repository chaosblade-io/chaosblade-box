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

package com.alibaba.chaosblade.platform.http.parser;

import com.alibaba.chaosblade.platform.http.constant.Blade;
import com.alibaba.chaosblade.platform.http.constant.Env;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author Changjun Xiao
 */
public class OssYamlService extends BaseYamlService {
    private HttpURLConnection conn;
    private String regionId;
    private String env;
    private String version;

    public OssYamlService(String regionId, String env, String version) {
        this.regionId = regionId;
        this.env = env;
        this.version = version;
    }

    @Override
    public InputStream getInputStream(String source) {
        try {
            conn = request();
            if (conn != null) {
                inputStream = conn.getInputStream();
            }
        } catch (Exception e) {
        }
        return inputStream;
    }

    @Override
    public void close() {
        super.close();
        if (conn != null) {
            conn.disconnect();
        }
    }

    private HttpURLConnection request() throws Exception {
        // "https://%s.oss-%s.aliyuncs.com/server/spec/%s/%s";
        String ossUrl = String.format(Env.OSS_PATTERN, getBucketByEnv(env), regionId, regionId, version,
            Blade.SPEC_FILE);
        HttpURLConnection conn;
        try {
            URL url = new URL(ossUrl);
            conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(1000);
            conn.setReadTimeout(5000);
            conn.setInstanceFollowRedirects(true);
            return conn;
        } catch (Exception e) {
            throw e;
        }
    }

    private String getBucketByEnv(String env) {
        if (Env.PROD.equalsIgnoreCase(env)) {
            return Env.PROD_PREFIX;
        }
        if (Env.PRE.equalsIgnoreCase(env)) {
            return Env.PRE_PREFIX;
        }
        return Env.PROD_PREFIX;
    }
}
